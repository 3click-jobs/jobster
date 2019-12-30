import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'

import { actions as seeksActions } from '../../../redux/actions/seeks'
import { seeksSelectors } from '../../../redux/selectors/seeks'
import SeekCard from './SeekCard'
import ToTopButton from '../../toTopButton/ToTopButton';


const toRadians = (angle) => {
  return angle * (Math.PI / 180);
}

const toDegrees = (radians) => {
  return radians * (180 / Math.PI);
}

const countDistance = (lat1, lon1, lat2, lon2, unit) => {
  if ((lat1 === lat2) && (lon1 === lon2)) {
    return 0;
  } else {
    let theta = lon1 - lon2;
    let dist = Math.sin(toRadians(lat1)) * Math.sin(toRadians(lat2)) + Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) * Math.cos(toRadians(theta));
    dist = Math.acos(dist);
    dist = toDegrees(dist);
    dist = dist * 60 * 1.1515; //Miles
    if (unit === "KM") { 
      dist = dist * 1.609344; //Kilometers
    } else if (unit === "NM") {
      dist = dist * 0.8684; //Nautical Miles
    }
    return (dist);
  }
}


export const SeeksContainer = ({
  role,
  seeksAll,
  seeksIsLoading,
  seeksIsError,
  loadSeeksAll,
  accept,
  decline,
  selectedCity,
  selectedJobType,
}) => {

  const [acceptableSeeks, setAcceptableSeeks] = React.useState(null)


  React.useEffect(() => {
    if (seeksAll.length === 0)
      loadSeeksAll()
  }, [loadSeeksAll, seeksAll])

  React.useEffect(() => {
    if (selectedCity && selectedJobType) {
      seeksAll.forEach(element => {
        // console.log(element.city)
        // console.log(element.latitude + " " + element.longitude)
        // console.log(element.jobType.jobTypeName)
        // console.log(countDistance(element.latitude, element.longitude, selectedCity.latitude, selectedCity.longitude, "KM"))
      });
      const byCityAndJob = seeksAll.filter(o => o.jobType.jobTypeName === selectedJobType.jobTypeName 
                                                && countDistance(o.latitude, o.longitude, selectedCity.latitude, selectedCity.longitude, "KM") <= o.distanceToJob)
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