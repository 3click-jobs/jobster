import React from 'react'
// import { connect } from 'react-redux'
// import RegisterAccount from '../accounts/RegisterAccount';
// import NavbarGeneric from '../navbar/NavbarGeneric'
import LookingForAJobCard from './cards/LookingForAJobCard';
import OfferingAJobCard from './cards/OfferingAJobCard';
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles';
// import Container from '@material-ui/core/Container'
import LookingForAEmployeeCard from './cards/LookingForAEmployeeCard';
import SeekingForAJobCard from './cards/SeekingForAJobCard';


const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
    height: "100%",
    padding: '16px'
  },
}));


export const HomePage = ({
  role
}) => {

  const classes = useStyles();


  return (
    <div className={classes.root} >
      <Grid container justify= "center" alignItems="stretch" spacing={2}>
        <Grid item xs={12} sm={6} md={6} lg={5} xl={3}>
          <LookingForAJobCard />
        </Grid>
        <Grid item xs={12} sm={6} md={6} lg={5} xl={3}>
          <LookingForAEmployeeCard />
        </Grid>
        { (role==="ROLE_USER" || role==="ROLE_ADMIN") && 
        <React.Fragment>
          <Grid item xs={12} sm={6} md={6} lg={5} xl={3}>
            <OfferingAJobCard />
          </Grid>
          <Grid item xs={12} sm={6} md={6} lg={5} xl={3}>
            <SeekingForAJobCard />
          </Grid>
        </React.Fragment>
        }
      </Grid>
    </div>
  )
}

export default HomePage