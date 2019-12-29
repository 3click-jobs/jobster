import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'

import { actions as seeksActions } from '../../../redux/actions/seeks'
import { seeksSelectors } from '../../../redux/selectors/seeks'
import SeekCard from './SeekCard'
import ToTopButton from '../../toTopButton/ToTopButton';


export const SeeksContainer = ({
  role,
  seeksAll,
  seeksIsLoading,
  seeksIsError,
  loadSeeksAll,
  accept,
  decline,
  selectedCity,
  selectedJobType
}) => {

  const [acceptableSeeks, setAcceptableSeeks] = React.useState(null)


  React.useEffect(() => {
    if (seeksAll.length === 0)
      loadSeeksAll()
  }, [loadSeeksAll, seeksAll])

  React.useEffect(() => {
    if (selectedCity && selectedJobType) {
      const byCityAndJob = seeksAll.filter(o => o.city === selectedCity.city && o.country === selectedCity.country && o.jobType.jobTypeName === selectedJobType.jobTypeName )
      setAcceptableSeeks(byCityAndJob)
    } else {
      setAcceptableSeeks(seeksAll)
    }
  }, [seeksAll, selectedCity, selectedJobType])


  return (
    <React.Fragment>
      {
        (!seeksIsLoading && acceptableSeeks) &&
        <React.Fragment>
            <p>Number of job seeks in the database: {acceptableSeeks.length}</p>
            <div style={{ padding: '16px' }}>
                <Grid container spacing={2} direction="row" justify="space-evenly" alignItems="flex-start" >
                    {acceptableSeeks.map(seek => {
                        return (
                        <Grid key={seek.id} item xs={12} sm={8} md={7} lg={7} xl={7} >
                          <SeekCard seek={seek} role={role} handleDeclineSeek={(declinedSeek) => decline(declinedSeek)} handleAcceptSeek={(acceptedSeek) => accept(acceptedSeek)} />
                        </Grid> )
                    })}     
                </Grid>
            </div>
            <ToTopButton />
        </React.Fragment>

      }
      {
        (!seeksIsLoading && seeksIsError) &&
        <p>Error at loading job seeks.</p>
      }
      {
        seeksIsLoading &&
        <p>Job seeks are loading ... </p>
      }
    </React.Fragment>

  )
}

const mapStateToProps = (state) => {
  return {
    seeksAll: seeksSelectors.getAll(state),
    seeksIsLoading: seeksSelectors.isFetching(state),
    seeksIsError: seeksSelectors.error(state),
    role: state.user.role
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadSeeksAll: () => dispatch(seeksActions.fetchAll()),
    decline: (seek) => dispatch(seeksActions.decline(seek)),
    accept: (seek) => dispatch(seeksActions.accept(seek))
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SeeksContainer)