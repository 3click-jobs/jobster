import React from 'react'
import { connect } from 'react-redux'
import ResponsiveTable from 'material-ui-next-responsive-table'

import { actions as companiesActions } from '../../../redux/actions/companies'
import { companiesSelectors } from '../../../redux/selectors/companies'
import CompaniesTable from './CompaniesTable'
import { useTranslation } from 'react-i18next'

import PropTypes from 'prop-types'

/**
 * Container component for browsing companies. Using ResponsiveTable for table rendering.
 */
export const CompaniesContainer = ({
  companiesAll,
  companiesIsLoading,
  companiesIsError,
  loadCompaniesAll
}) => {

  const { t } = useTranslation();

  React.useEffect(() => {
    loadCompaniesAll()
  }, [loadCompaniesAll])


  const columns = [
  {
    key: 'companyName',
    label: `${t('profile.labelName')}`,
    // label: 'Name',
    primary: true,
  },
  {
    key: 'companyRegistrationNumber',
    label: `${t('profile.labelRegistrationNumber')}`,
    // label: 'Registration number',
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
    primary: true,
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

const data = companiesAll


  return (
    <React.Fragment>
      {
        (!companiesIsLoading && companiesAll) &&
        <React.Fragment>
          <p>
            {t('companiesContainer.numberOfCompanies')}
            {/* Number of companies in the database:  */}
            {companiesAll.length}
          </p>
          <ResponsiveTable columns={columns} data={data} />
          <CompaniesTable />
        </React.Fragment>

      }
      {
        (!companiesIsLoading && companiesIsError) &&
        <p>
          {t('companiesContainer.errorAtLoading')}
          {/* Error at loading companies. */}
        </p>
      }
      {
        companiesIsLoading &&
        <p>
          {t('companiesContainer.resourcesAreLoading')}
          {/* Companies are loading ...  */}
        </p>
      }
    </React.Fragment>

  )
}

const mapStateToProps = (state) => {
  return {
    companiesAll: companiesSelectors.getAll(state),
    companiesIsLoading: companiesSelectors.isFetching(state),
    companiesIsError: companiesSelectors.error(state)
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadCompaniesAll: () => dispatch(companiesActions.fetchAll())
  }
}

CompaniesContainer.propTypes = {
  /**
   * An array containing the list of all companies.
   */
  companiesAll: PropTypes.array,
  /**
   * Flag indicating if companies data is being loaded.
   */
  companiesIsLoading: PropTypes.bool,
  /**
   * Flag indicating if there was an error loading the data.
   */
  companiesIsError: PropTypes.bool,
  /**
   * Callback function to initiate the loading of companies data.
   */
  loadCompaniesAll: PropTypes.func
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CompaniesContainer)