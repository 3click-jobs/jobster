import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'

import { actions as offersActions } from '../../../redux/actions/offers'
import { offersSelectors } from '../../../redux/selectors/offers'
import OfferCard from './OfferCard'


export const OffersContainer = ({
  role,
  offersAll,
  offersIsLoading,
  offersIsError,
  loadOffersAll,
  accept,
  decline,
  selectedCity,
  selectedJobType
}) => {

  const [acceptableOffers, setAcceptableOffers] = React.useState(null)

  React.useEffect(() => {
    if (offersAll.length === 0)
      loadOffersAll()
  }, [loadOffersAll, offersAll])

  React.useEffect(() => {
    if (selectedCity && selectedJobType) {
      const byCityAndJob = offersAll.filter(o => o.city === selectedCity.city && o.country === selectedCity.country && o.jobType.jobTypeName === selectedJobType.jobTypeName )
      setAcceptableOffers(byCityAndJob)
    } else {
      setAcceptableOffers(offersAll)
    }
  }, [offersAll, selectedCity, selectedJobType])


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