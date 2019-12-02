import { combineReducers } from 'redux'
import accounts from './accounts'
import persons from './persons'
import companies from './companies'


const entities = combineReducers({
  accounts,
  persons,
  companies
})

export default entities