import React from 'react'
// import RegisterAccount from '../accounts/RegisterAccount';
// import NavbarGeneric from '../navbar/NavbarGeneric'
import LookingForAJobCard from './cards/LookingForAJobCard';
import OfferingAJobCard from './cards/OfferingAJobCard';
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles';
// import Container from '@material-ui/core/Container'
import LookingForAEmployeeCard from './cards/LookingForAEmployeeCard';

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
    height: "100%",
    padding: '16px'
  },
}));


export const HomePage = () => {

  const classes = useStyles();

  return (
    <div className={classes.root} >
      <Grid container justify= "center" alignItems="stretch" spacing={2}>
        <Grid item xs={12} sm={6} md={6} lg={4} xl={3}>
          <LookingForAJobCard />
        </Grid>
        <Grid item xs={12} sm={6} md={6} lg={4} xl={3}>
          <OfferingAJobCard />
        </Grid>
        <Grid item xs={12} sm={6} md={6} lg={4} xl={3}>
          <LookingForAEmployeeCard />
        </Grid>
      </Grid>
    </div>

  )
}

export default HomePage