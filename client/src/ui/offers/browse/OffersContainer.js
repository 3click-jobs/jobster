import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'

import { actions as offersActions } from '../../../redux/actions/offers'
import { offersSelectors } from '../../../redux/selectors/offers'
import OfferCard from './OfferCard'


export const OffersContainer = ({
  offersAll,
  offersIsLoading,
  offersIsError,
  loadOffersAll
}) => {

  React.useEffect(() => {
    loadOffersAll()
  }, [loadOffersAll])



  return (
    <React.Fragment>
      {
        (!offersIsLoading && offersAll) &&
        <React.Fragment>
            <p>Number of job offers in the database: {offersAll.length}</p>
            <div style={{ padding: '16px' }}>
                <Grid container spacing={2} direction="row" justify="space-evenly" alignItems="center" >
                    {offersAll.map(offer => {
                        return (
                        <Grid key={offer.id} item xs={12} sm={6}>
                            <OfferCard offer={offer} />
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
    offersIsError: offersSelectors.error(state)
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadOffersAll: () => dispatch(offersActions.fetchAll())
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OffersContainer)