import React from 'react'
import { connect } from 'react-redux'
import { actions as personsActions } from '../../../redux/actions/persons'
import { personsSelectors } from '../../../redux/selectors/persons'
import PersonsTable from './PersonsTable'

export const PersonsContainer = ({
  personsAll,
  personsIsLoading,
  personsIsError,
  loadPersonsAll
}) => {

  React.useEffect(() => {
    loadPersonsAll()
  }, [loadPersonsAll])

  return (
    <React.Fragment>
      {
        (!personsIsLoading && personsAll) &&
        <React.Fragment>
          <p>Number of persons in the database: {personsAll.length}</p>
          <PersonsTable />
        </React.Fragment>

      }
      {
        (!personsIsLoading && personsIsError) &&
        <p>Error at loading persons.</p>
      }
      {
        personsIsLoading &&
        <p>Persons is loading ... </p>
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