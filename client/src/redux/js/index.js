import store from '../store/index'

import { accountsSelectors } from '../selectors/accounts'
import { personsSelectors } from '../selectors/persons'
import { companiesSelectors } from '../selectors/companies'
import { seeksSelectors } from '../selectors/seeks'
import { jobTypesSelectors } from '../selectors/jobTypes'
import { offersSelectors } from '../selectors/offers'

import { actions as accountsActions } from '../actions/accounts'
import { actions as personsActions } from '../actions/persons'
import { actions as companiesActions } from '../actions/companies'
import { actions as seeksActions } from '../actions/seeks'
import { actions as jobTypesActions } from '../actions/jobTypes'
import { actions as offersActions } from '../actions/offers'


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
  window.seeksSelectors = seeksSelectors
  window.jobTypesSelectors = jobTypesSelectors
  window.offersSelectors = offersSelectors

  window.accountsActions = accountsActions
  window.personsActions = personsActions
  window.companiesActions = companiesActions
  window.seeksActions = seeksActions
  window.jobTypesActions = jobTypesActions
  window.offersActions = offersActions
}
