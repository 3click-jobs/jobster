import React from 'react'
import { connect } from 'react-redux'
import ResponsiveTable from 'material-ui-next-responsive-table'

import { actions as companiesActions } from '../../../redux/actions/companies'
import { companiesSelectors } from '../../../redux/selectors/companies'
import CompaniesTable from './CompaniesTable'


export const CompaniesContainer = ({
  companiesAll,
  companiesIsLoading,
  companiesIsError,
  loadCompaniesAll
}) => {

  React.useEffect(() => {
    loadCompaniesAll()
  }, [loadCompaniesAll])


  const columns = [
  {
    key: 'companyName',
    label: 'Name',
    primary: true,
  },
  {
    key: 'companyRegistrationNumber',
    label: 'Registration number',
  },
  {
    key: 'email',
    label: 'E-mail',
  },
  {
    key: 'mobilePhone',
    label: 'Contact phone',
  },
  {
    key: 'about',
    label: 'About',
  },
  {
    key: 'accessRole',
    label: 'Access role',
  },
  {
    key: 'city',
    label: 'City',
    primary: true,
  },
  {
    key: 'countryRegion',
    label: 'Country region',
  },
  {
    key: 'country',
    label: 'Country',
  },
  {
    key: 'iso2Code',
    label: 'Country code',
  },
  {
    key: 'username',
    label: 'Username',
  }
]

const data = companiesAll


  return (
    <React.Fragment>
      {
        (!companiesIsLoading && companiesAll) &&
        <React.Fragment>
          <p>Number of companies in the database: {companiesAll.length}</p>
          <ResponsiveTable columns={columns} data={data} />
          <CompaniesTable />
        </React.Fragment>

      }
      {
        (!companiesIsLoading && companiesIsError) &&
        <p>Error at loading companies.</p>
      }
      {
        companiesIsLoading &&
        <p>Companies are loading ... </p>
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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CompaniesContainer)