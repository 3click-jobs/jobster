import React from 'react'
import { connect } from 'react-redux'
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

  return (
    <React.Fragment>
      {
        (!companiesIsLoading && companiesAll) &&
        <React.Fragment>
          <p>Number of companies in the database: {companiesAll.length}</p>
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