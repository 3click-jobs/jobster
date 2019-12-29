import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles';
import NavigationRoundedIcon from '@material-ui/icons/NavigationRounded';
import Fab from '@material-ui/core/Fab';
// import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';

import { actions as offersActions } from '../../../redux/actions/offers'
import { offersSelectors } from '../../../redux/selectors/offers'
import OfferCard from './OfferCard'


const useStyles = makeStyles(theme => ({
  absolute: {
    position: 'absolute',
    bottom: theme.spacing(2),
    right: theme.spacing(3),
  },
  floatingBottomRight : {
    margin: 0,
    top: 'auto',
    right: 20,
    bottom: 20,
    left: 'auto',
    position: 'fixed',
    overflowX: 'hidden',
  },
}));


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
  const [windowPosition, setWindowPosition] = React.useState(0)

  const classes = useStyles();

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

  React.useEffect(() => {
    function handleScroll() {
      setWindowPosition(window.pageYOffset);
    }

    window.addEventListener('scroll', handleScroll);
    return function cleanup() {
      window.removeEventListener('scroll', handleScroll);
    };
  });


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
            <Tooltip title="Top" aria-label="top" placement="right-end" onClick={ () => { window.scroll(0, 0); }}>
              {/* <IconButton aria-label="delete"> */}
              <Fab color="primary" className={classes.floatingBottomRight} style={{ display: windowPosition === 0 ? 'none' : null }} >
                <NavigationRoundedIcon />
              </Fab>
              {/* </IconButton> */}
            </Tooltip>
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