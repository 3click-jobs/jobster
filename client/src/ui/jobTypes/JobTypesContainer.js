import React from 'react';
import { connect } from 'react-redux'
import { actions as jobTypesActions } from '../../redux/actions/jobTypes'
import { jobTypesSelectors } from '../../redux/selectors/jobTypes'
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next'


const useStyles = makeStyles(theme => ({
  tabPaper: {
    maxWidth: 720,
    width: '100%',
    marginTop: 10
  },
  tabContainer: {
    display: "flex",
    justifyContent: 'center',
    alignItems: 'center',
  },
}));


export const JobTypesContainer = ({
    jobTypesAll,
    jobTypesIsLoading,
    jobTypesIsError,
    loadJobTypesAll,
    handleJobType,
    selectedJobType
    }) => {
    
    const [jobType, setJobType] = React.useState(null)
    const [initJobType, setInitJobType] = React.useState(false)
    const classes = useStyles();
    const { t } = useTranslation();

    React.useEffect(() => {
            setInitJobType(true)
    }, [selectedJobType])

    React.useEffect(() => {
        if (jobTypesAll.length === 0)
            loadJobTypesAll()
    }, [loadJobTypesAll, jobTypesAll])

    const handleJobTypeSelect = (newValue) => {
        if (newValue && newValue !== "" && newValue !== " ") {
            setJobType(newValue);
        } else {
            setJobType(null);
        }
        if (initJobType) 
            handleJobType(newValue)
    }


    return (
        <React.Fragment>
            { 
            (!jobTypesIsLoading && jobTypesAll) &&
            <div>
                <div className={classes.tabContainer}>
                    <div className={classes.tabPaper}>
                        <Autocomplete
                            id="combo-box"
                            options={jobTypesAll}
                            getOptionLabel={option => option.jobTypeName}
                            style={{ minWidth: 270 }}
                            value={selectedJobType ? selectedJobType : jobType}
                            clearOnEscape
                            disableClearable
                            autoComplete
                            onChange={(event, newValue) => {
                                handleJobTypeSelect(newValue); 
                            }}
                            renderInput={params => (
                            <TextField {...params} 
                                label={t('applyOrEmploy.labelJobType')}
                                // label="Job type" 
                                variant="outlined" 
                                required 
                                fullWidth 
                                onChange={(event) => {
                                    if (!event || event.target.value === "" || event.target.value === "") {
                                        handleJobTypeSelect(null);
                                    }
                                }}
                            />
                            )}
                        />
                    </div>
                </div>
            </div>
            }
            {
            (!jobTypesIsLoading && jobTypesIsError) &&
            <p>Error at loading job types.</p>
            }
            {
            jobTypesIsLoading &&
            <p>Job types are loading ... </p>
            }
        </React.Fragment>      
    )
}

const mapStateToProps = (state) => {
    return {
    jobTypesAll: jobTypesSelectors.getAll(state),
    jobTypesIsLoading: jobTypesSelectors.isFetching(state),
    jobTypesIsError: jobTypesSelectors.error(state)
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
    loadJobTypesAll: () => dispatch(jobTypesActions.fetchAll())
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(JobTypesContainer)