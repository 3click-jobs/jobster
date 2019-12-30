import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'

import { actions as offersActions } from '../../../redux/actions/offers'
import { offersSelectors } from '../../../redux/selectors/offers'
import OfferCard from './OfferCard'
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


export const OffersContainer = ({
  role,
  offersAll,
  offersIsLoading,
  offersIsError,
  loadOffersAll,
  accept,
  decline,
  selectedCity,
  selectedJobType,
  distance
}) => {

  const [acceptableOffers, setAcceptableOffers] = React.useState(null)

  React.useEffect(() => {
    if (offersAll.length === 0)
      loadOffersAll()
  }, [loadOffersAll, offersAll])

  React.useEffect(() => {
    if (selectedCity && selectedJobType && distance) {
      const byCityAndJob = offersAll.filter(o => o.jobType.jobTypeName === selectedJobType.jobTypeName
                                                && countDistance(o.latitude, o.longitude, selectedCity.latitude, selectedCity.longitude, "KM") <= distance )
      setAcceptableOffers(byCityAndJob)
    } else {
      setAcceptableOffers(offersAll)
    }
  }, [offersAll, selectedCity, selectedJobType, distance])

  // let windowScrolled = 0


  return (
    <React.Fragment>
      {
        (!offersIsLoading && acceptableOffers) &&
        <React.Fragment>
            <p>Total number of job offers in the database: {acceptableOffers.length}</p>
            <div style={{ padding: '16px' }}>
              <Grid container spacing={2} direction="row" justify="space-evenly" alignItems="flex-start" >
                    {acceptableOffers.map(offer => {
                        return (
                        <Grid key={offer.id} item xs={12} sm={8} md={7} lg={7} xl={7} >
                          <OfferCard offer={offer} role={role} handleDeclineOffer={(declinedOffer) => decline(declinedOffer)} handleAcceptOffer={(acceptedOffer) => accept(acceptedOffer)} />
                        </Grid> )
                    })}     
                </Grid>
            </div>
            <ToTopButton />
        </React.Fragment>

      }
      {
        (!offersIsLoading && offersIsError) &&
        <p>Error at loading job offers.</p>
      }
      {
        offersIsLoading &&
        <p>Job offers are loading ... </p>
      }
    </React.Fragment>

  )
}

const mapStateToProps = (state) => {
  return {
    offersAll: offersSelectors.getAll(state),
    offersIsLoading: offersSelectors.isFetching(state),
    offersIsError: offersSelectors.error(state),
    role: state.user.role
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadOffersAll: () => dispatch(offersActions.fetchAll()),
    decline: (offer) => dispatch(offersActions.decline(offer)),
    accept: (offer) => dispatch(offersActions.accept(offer))
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OffersContainer)