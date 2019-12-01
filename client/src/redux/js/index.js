import store from '../store/index'

import { accountsSelectors } from '../selectors/accounts'
import { personsSelectors } from '../selectors/persons'
import { companiesSelectors } from '../selectors/companies'

import { actions as accountsActions } from '../actions/accounts'
import { actions as personsActions } from '../actions/persons'
import { actions as companiesActions } from '../actions/companies'

const sandbox = true

// HELP:
// actions currently: fetchOne, fetchAll, fetchAny
// selectors currently: getAll, byId, byQuery, error, isFetching
// added actions create, update, remove (delete is a keyword) --> some not defined in rest, some lack support at backend


if (sandbox) {
  window.store = store
  // window.getTeacher = getTeacher
  window.accountsSelectors = accountsSelectors
  window.personsSelectors = personsSelectors
  window.companiesSelectors = companiesSelectors

  window.accountsActions = accountsActions
  window.personsActions = personsActions
  window.companiesActions = companiesActions
}
