import React from 'react';
import { connect } from 'react-redux';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Icon from '@material-ui/core/Icon';
import { makeStyles } from '@material-ui/core/styles';
import { actions as jobTypesActions } from '../../redux/actions/jobTypes'
import { jobTypesSelectors } from '../../redux/selectors/jobTypes'
import CityAPIContainer from '../cities/CityAPIContainer';
import SeeksContainer from '../seeks/browse/SeeksContainer';
import { useTranslation } from 'react-i18next'


const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1),
    float: 'right',
  },
  buttonLeft: {
    margin: theme.spacing(1),
    float: 'left',
  },
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

function TabPanel(props) {
  const { children, value, index, ...other } = props;
  return (
    <Typography
      component="div"
      role="tabpanel"
      hidden={value !== index}
      id={`scrollable-auto-tabpanel-${index}`}
      aria-labelledby={`scrollable-auto-tab-${index}`}
      {...other}
    >
      {value === index && <Box p={3}>{children}</Box>}
    </Typography>
  );
}


export const EmployContainer = ({
  jobTypesAll,
  jobTypesIsLoading,
  jobTypesIsError,
  loadJobTypesAll
}) => {

  const [city, setCity] = React.useState(null)
  const [jobType, setJobType] = React.useState(null)
  const [activeTab, setActiveTab] = React.useState(0)
  const classes = useStyles();
  const { t } = useTranslation();

  React.useEffect(() => {
    if (jobTypesAll.length === 0)
      loadJobTypesAll()
  }, [loadJobTypesAll, jobTypesAll])


  const handleTabChange = (event, newActiveTab) => {
    setActiveTab(newActiveTab)
  }


  return (
    <React.Fragment>
      { 
        (!jobTypesIsLoading && jobTypesAll) &&
        <div>
          <div>
            <Tabs
              value={activeTab}
              onChange={handleTabChange}
              indicatorColor='primary'
              textColor='primary'
              variant='fullWidth'
            >
              <Tab 
                label={t('applyOrEmploy.labelWhere')}
                // label='Where' 
              />
              {city ? 
                <Tab 
                  label={t('applyOrEmploy.labelWhat')}
                  // label='What'
                /> : <Tab 
                  label={t('applyOrEmploy.labelWhat')}
                  // label='What' 
                  disabled
                />}
              {city && jobType ? 
                <Tab 
                  label={t('applyOrEmploy.labelConnect')}
                  // label='Connect'
                /> : <Tab 
                  label={t('applyOrEmploy.labelConnect')}
                  // label='Connect' 
                  disabled
                />}
            </Tabs>
              <TabPanel value={activeTab} index={0}>
                <div className={classes.tabContainer}>
                  <div className={classes.tabPaper}>
                    <CityAPIContainer city={city} setCity = {setCity} />
                    <div>
                      <Button
                        disabled={ !city }
                        variant="contained"
                        color="primary"
                        className={classes.button}
                        endIcon={<Icon>arrow_forward_ios</Icon>} 
                        onClick={() => { setActiveTab(1); }}
                      >
                        {t('applyOrEmploy.buttonNext')}
                        {/* Next */}
                      </Button>
                    </div>
                  </div>
                </div>
              </TabPanel>
              <TabPanel value={activeTab} index={1}>
                <div className={classes.tabContainer}>
                  <div className={classes.tabPaper}>
                    <Autocomplete
                      id="combo-box"
                      options={jobTypesAll}
                      getOptionLabel={option => option.jobTypeName}
                      style={{ minWidth: 270 }}
                      value={jobType}
                      clearOnEscape
                      disableClearable
                      autoComplete
                      onChange={(event, newValue) => {
                        setJobType(newValue);
                      }}
                      renderInput={params => (
                        <TextField {...params} 
                          label={t('applyOrEmploy.labelJobType')}
                          // label="Job type" 
                          variant="outlined" 
                          required 
                          fullWidth 
                          onChange={(event) => {
                            if (!event || event.target.value === "" || event.target.value === "")
                              setJobType(null);
                          }}
                        />
                      )}
                    />
                    <div>
                      <Button
                        variant="contained"
                        color="primary"
                        className={classes.buttonLeft}
                        startIcon={<Icon>arrow_back_ios</Icon>} 
                        onClick={() => { setActiveTab(0); }}
                      >
                        {t('applyOrEmploy.buttonBack')}
                        {/* Back */}
                      </Button>
                      <Button
                        disabled={ !jobType }
                        variant="contained"
                        color="primary"
                        className={classes.button}
                        endIcon={<Icon>arrow_forward_ios</Icon>} 
                        onClick={() => { setActiveTab(2); }}
                      >
                        {t('applyOrEmploy.buttonNext')}
                        {/* Next */}
                      </Button>
                    </div>
                  </div>
                </div>
              </TabPanel>
              <TabPanel value={activeTab} index={2}>
                <SeeksContainer selectedCity={city} selectedJobType={jobType} />
                <div className={classes.tabContainer}>
                  <div className={classes.tabPaper}>
                    <div>
                      <Button
                        variant="contained"
                        color="primary"
                        className={classes.buttonLeft}
                        startIcon={<Icon>arrow_back_ios</Icon>} 
                        onClick={() => { setActiveTab(1); }}
                      >
                        {t('applyOrEmploy.buttonBack')}
                        {/* Back */}
                      </Button>
                    </div>
                  </div>
                </div>
              </TabPanel>
            
          </div>
        </div>
      }
      {
        (!jobTypesIsLoading && jobTypesIsError) &&
        <p>Error at loading resources.</p>
      }
      {
        jobTypesIsLoading &&
        <p>Resources are loading ... </p>
      }
    </React.Fragment>
  )
}

const mapStateToProps = (state) => {
  return {
    jobTypesAll: jobTypesSelectors.getAll(state),
    jobTypesIsLoading: jobTypesSelectors.isFetching(state),
    jobTypesIsError: jobTypesSelectors.error(state),
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadJobTypesAll: () => dispatch(jobTypesActions.fetchAll()),
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EmployContainer)