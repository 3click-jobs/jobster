import React from 'react'
import { connect } from 'react-redux'
import ResponsiveTable from 'material-ui-next-responsive-table'

import { actions as personsActions } from '../../../redux/actions/persons'
import { personsSelectors } from '../../../redux/selectors/persons'
import PersonsTable from './PersonsTable'
import { useTranslation } from 'react-i18next'


export const PersonsContainer = ({
  personsAll,
  personsIsLoading,
  personsIsError,
  loadPersonsAll
}) => {

  const { t } = useTranslation();

  React.useEffect(() => {
    loadPersonsAll()
  }, [loadPersonsAll])

  // comment can be deleted

  const columns = [
    {
      key: 'firstName',
      label: `${t('profile.labelFirstName')}`,
      // label: 'Name',
      primary: true,
    },
    {
      key: 'lastName',
      label: `${t('profile.labelLastName')}`,
      // label: 'Surname',
      primary: true,
    },
    {
      key: 'gender',
      label: `${t('profile.labelGender')}`,
      // label: 'Gender',
    },
    {
      key: 'birthDate',
      label: `${t('profile.labelDateOfBirth')}`,
      // label: 'Birth date',
    },
    {
      key: 'email',
      label: `${t('profile.labelEMail')}`,
      // label: 'E-mail',
    },
    {
      key: 'mobilePhone',
      label: `${t('profile.labelContactPhone')}`,
      // label: 'Contact phone',
    },
    {
      key: 'about',
      label: `${t('profile.labelAbout')}`,
      // label: 'About',
    },
    {
      key: 'accessRole',
      label: `${t('profile.labelAccessRole')}`,
      // label: 'Access role',
    },
    {
      key: 'city',
      label: `${t('profile.labelCity')}`,
      // label: 'City',
    },
    {
      key: 'countryRegion',
      label: `${t('profile.labelCountryRegion')}`,
      // label: 'Country region',
    },
    {
      key: 'country',
      label: `${t('profile.labelCountry')}`,
      // label: 'Country',
    },
    {
      key: 'iso2Code',
      label: `${t('profile.labelCountryCode')}`,
      // label: 'Country code',
    },
    {
      key: 'username',
      label: `${t('profile.labelUsername')}`,
      // label: 'Username',
    }
  ]
  
  const data = personsAll

  
  return (
    <React.Fragment>
      {
        (!personsIsLoading && personsAll) &&
        <React.Fragment>
          <p>
            {t('personsContainer.numberOfCompanies')}
            {/* Number of persons in the database:  */}
            {personsAll.length}
          </p>
          <ResponsiveTable columns={columns} data={data} />
          <PersonsTable />
        </React.Fragment>

      }
      {
        (!personsIsLoading && personsIsError) &&
        <p>
          {t('personsContainer.errorAtLoading')}
          {/* Error at loading persons. */}
        </p>
      }
      {
        personsIsLoading &&
        <p>
          {t('personsContainer.resourcesAreLoading')}
          {/* Persons is loading ... */}
        </p>
      }
    </React.Fragment>

  )
}

const mapStateToProps = (state) => {
  return {
    personsAll: personsSelectors.getAll(state),
    personsIsLoading: personsSelectors.isFetching(state),
    personsIsError: personsSelectors.error(state)
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadPersonsAll: () => dispatch(personsActions.fetchAll())
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PersonsContainer)