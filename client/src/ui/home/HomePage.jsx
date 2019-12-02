import React from 'react'
import RegisterAccount from '../accounts/RegisterAccount';
import NavbarGeneric from '../navbar/NavbarGeneric'
import LookingForAJobCard from './cards/LookingForAJobCard';
import OfferingAJobCard from './cards/OfferingAJobCard';
import Grid from '@material-ui/core/Grid'
import Container from '@material-ui/core/Container'

export const HomePage = () => {
  return (
    <div style={{ padding: '16px' }}>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
          <LookingForAJobCard />
        </Grid>
        <Grid item xs={12} sm={6}>
          <OfferingAJobCard />
        </Grid>
      </Grid>
    </div>

  )
}

export default HomePage