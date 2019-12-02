import { combineReducers } from 'redux'

const role = (state = 'ROLE_GUEST', action) => {
  switch (action.type) {
    case 'SUCCESS_USER_VERIFY':
    case 'SUCCESS_USER_LOGIN':
    case 'SUCCESS_USER_SIGNUP':
      return action.payload.accessRole
    case 'FAILURE_USER_LOGIN':
    case 'FAILURE_USER_VERIFY':
    case 'FAILURE_USER_SIGNUP':
    case 'REQUEST_USER_CREDENTIALS_UNASSIGN':
      return 'ROLE_GUEST'
    default:
      return state
  }
}

const username = (state = '', action) => {
  switch (action.type) {
    case 'SUCCESS_USER_VERIFY':
    case 'SUCCESS_USER_LOGIN':
    case 'SUCCESS_USER_SIGNUP':
      return action.payload.username
    case 'FAILURE_USER_VERIFY':
    case 'FAILURE_USER_LOGIN':
    case 'FAILURE_USER_SIGNUP':
    case 'REQUEST_USER_CREDENTIALS_UNASSIGN':
      return ''
    default:
      return state
  }
}

const loggedIn = (state = false, action) => {
  switch (action.type) {
    case 'SUCCESS_USER_VERIFY':
    case 'SUCCESS_USER_LOGIN':
    case 'SUCCESS_USER_SIGNUP':
      return true
    case 'FAILURE_USER_VERIFY':
    case 'FAILURE_USER_LOGIN':
    case 'FAILURE_USER_SIGNUP':
    case 'REQUEST_USER_CREDENTIALS_UNASSIGN':
      return false
    default:
      return state
  }
}

const errorMessage = (state = '', action) => {
  return ''
}

const isFetching = (state = false, action) => {
  return false
}

const userReducer = combineReducers({
  loggedIn,
  username,
  role,
  errorMessage,
  isFetching
})

export default userReducer