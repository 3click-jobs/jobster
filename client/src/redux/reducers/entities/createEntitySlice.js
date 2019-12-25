import { combineReducers } from 'redux'

const createEntitySlice = (entity) => {
  const entityUpper = entity.toUpperCase()

  const isFetching = (state = false, action) => {
    switch (action.type) {
      case `REQUEST_${entityUpper}_GETALL`:
      case `REQUEST_${entityUpper}_GETONE`:
      case `REQUEST_${entityUpper}_GETANY`:
      case `REQUEST_${entityUpper}_CREATE`:
      case `REQUEST_${entityUpper}_UPDATE`:
      case `REQUEST_${entityUpper}_DECLINE`:
        return true
      case `REQUEST_${entityUpper}_ACCEPT`:    
        return true
      case `REQUEST_${entityUpper}_DELETE`:
        return true
      case `SUCCESS_${entityUpper}_GETALL`:
      case `SUCCESS_${entityUpper}_GETONE`:
      case `SUCCESS_${entityUpper}_GETANY`:
      case `SUCCESS_${entityUpper}_CREATE`:
      case `SUCCESS_${entityUpper}_UPDATE`:
      case `SUCCESS_${entityUpper}_DECLINE`:
      case `SUCCESS_${entityUpper}_ACCEPT`:
      case `SUCCESS_${entityUpper}_DELETE`:
      case `FAILURE_${entityUpper}_GETALL`:
      case `FAILURE_${entityUpper}_GETONE`:
      case `FAILURE_${entityUpper}_GETANY`:
      case `FAILURE_${entityUpper}_CREATE`:
      case `FAILURE_${entityUpper}_UPDATE`:
      case `FAILURE_${entityUpper}_DECLINE`:
        return false
      case `FAILURE_${entityUpper}_ACCEPT`:  
        return false  
      case `FAILURE_${entityUpper}_DELETE`:
        return false
      default:
        return state
    }
  }

  const errorMessage = (state = null, action) => {
    switch (action.type) {
      case `REQUEST_${entityUpper}_GETALL`:
      case `REQUEST_${entityUpper}_GETONE`:
      case `REQUEST_${entityUpper}_GETANY`:
      case `REQUEST_${entityUpper}_CREATE`:
      case `REQUEST_${entityUpper}_UPDATE`:
      case `REQUEST_${entityUpper}_DELETE`:
      case `REQUEST_${entityUpper}_DECLINE`:
      case `REQUEST_${entityUpper}_ACCEPT`:
      case `SUCCESS_${entityUpper}_GETALL`:
      case `SUCCESS_${entityUpper}_GETONE`:
      case `SUCCESS_${entityUpper}_GETANY`:
      case `SUCCESS_${entityUpper}_CREATE`:
      case `SUCCESS_${entityUpper}_UPDATE`:
      case `SUCCESS_${entityUpper}_DECLINE`:
        return null
      case `SUCCESS_${entityUpper}_ACCEPT`:
        return null
      case `SUCCESS_${entityUpper}_DELETE`:
        return null
      case `FAILURE_${entityUpper}_GETALL`:
      case `FAILURE_${entityUpper}_GETONE`:
      case `FAILURE_${entityUpper}_GETANY`:
      case `FAILURE_${entityUpper}_CREATE`:
      case `FAILURE_${entityUpper}_UPDATE`:
      case `FAILURE_${entityUpper}_DECLINE`:
        return action.payload.message || 'Something went wrong'
      case `FAILURE_${entityUpper}_ACCEPT`:
        return action.payload.message || 'Something went wrong'
      case `FAILURE_${entityUpper}_DELETE`:
        return action.payload.message || 'Something went wrong'
      default:
        return state
    }
  }

  const byId = (state = {}, action) => {
    switch (action.type) {
      case `SUCCESS_${entityUpper}_GETONE`:
      case `SUCCESS_${entityUpper}_GETALL`:
      case `SUCCESS_${entityUpper}_GETANY`:
      case `SUCCESS_${entityUpper}_CREATE`:
      case `SUCCESS_${entityUpper}_UPDATE`: {
        // if (entityUpper === 'TEACHERS') {
        //   console.log('INSIDE REDUCER FOR TEACHER BY ID', action.payload.entities[entity])
        //   console.log('STATE: ', state)
        //   console.log('AFTER REDUCING: ', {
        //     ...state,
        //     ...action.payload.entities[entity]
        //   })
        // }
        return {
          ...state,
          ...action.payload.entities[entity]
        }
      }
      case `SUCCESS_${entityUpper}_DELETE`: {
        const newState = {...state}
        delete newState[action.payload.result]
        return newState
      }
      case `SUCCESS_${entityUpper}_DECLINE`: {
        const newState = {...state}
        // const newState = oldState.filter( val => val !== action.payload.id );
        delete newState[action.payload.result]
        return newState
      }
      case `SUCCESS_${entityUpper}_ACCEPT`: {
        const newState = {...state}
        // const newState = oldState.filter( val => val !== action.payload.id );
        delete newState[action.payload.result]
        return newState
      }
      default:
        return state
    }
  }

  const allIds = (state = [], action) => {
    switch (action.type) {
      case `SUCCESS_${entityUpper}_GETONE`:
      case `SUCCESS_${entityUpper}_CREATE`:
      case `SUCCESS_${entityUpper}_UPDATE`:
        return [...new Set([...state, action.payload.result])]
      case `SUCCESS_${entityUpper}_GETALL`:
      case `SUCCESS_${entityUpper}_GETANY`:
        {
          if (entityUpper === 'TEACHERS') {
            console.log('TEACHERS IN REDUCER: STATE: ', state, 'PAYLOAD: ', action.payload)
          }
          return [...new Set([...state, ...action.payload.result])]
        }
      case `SUCCESS_${entityUpper}_DELETE`:
        return state.filter(id => id !== action.payload.result)
      case `SUCCESS_${entityUpper}_DECLINE`:
        return state.filter(id => id !== action.payload.result)
      case `SUCCESS_${entityUpper}_ACCEPT`:
        return state.filter(id => id !== action.payload.result)    
      default:
        return state
    }
  }

  // THESE WILL BE HARD TO MANAGE, ESPECIALLY WITH RELATIONS
  // WITH CREATE, UPDATE & DELETE
  const queries = (state = {}, action) => {
    switch (action.type) {
      case `SUCCESS_${entityUpper}_GETANY`:
        return {
          ...state,
          [action.meta.query]: [...action.payload.result]
        }
      default:
        return state
    }
  }

  const entityReducer = combineReducers({
    byId,
    allIds,
    isFetching,
    errorMessage,
    queries
  })

  return entityReducer
}

export default createEntitySlice