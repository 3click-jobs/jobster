import { combineReducers } from 'redux'
import entities from './entities'
import user from './user/user'

const appReducer = combineReducers({
  user,
  entities
})

const rootReducer = (state, action) => {
  if (action.type === 'USER_LOGOUT') {
    state = undefined
  }
  
  return appReducer(state, action)
}

export default rootReducer