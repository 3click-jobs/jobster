import { combineReducers } from 'redux'
import accounts from './accounts'
import persons from './persons'
import companies from './companies'
import seeks from './seeks'
import jobTypes from './jobTypes'
import offers from './offers'


const entities = combineReducers({
  accounts,
  persons,
  companies,
  seeks,
  jobTypes,
  offers
})

export default entities