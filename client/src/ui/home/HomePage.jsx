import React from 'react'
import RegisterAccount from '../accounts/RegisterAccount';
import NavbarGeneric from '../navbar/NavbarGeneric'
import LookingForAJobCard from './cards/LookingForAJobCard';
import OfferingAJobCard from './cards/OfferingAJobCard';

export const HomePage = () => {
  return (
    <div>
      <p>Looking for a job (card)</p>
      <p>Offering a job (card)</p>
      <LookingForAJobCard />
      <OfferingAJobCard />
    </div>
  )
}

export default HomePage