import { createStore, applyMiddleware } from 'redux'
import { composeWithDevTools } from 'redux-devtools-extension'
import rootReducer from '../reducers/index'
// import { forbiddenWordsMiddleware } from '../middleware'
// import createSagaMiddleware from 'redux-saga'
// import apiSaga from '../sagas/api-saga'
import logger from 'redux-logger'
import thunk from 'redux-thunk'

// const initialiseSagaMiddleware = createSagaMiddleware()

const configureStore = () => {
  const middlewares = [thunk]

  if (process.env.NODE_ENV !== 'production') {
    middlewares.push(logger)
  }

  return createStore(
    rootReducer,
    composeWithDevTools(
      applyMiddleware(...middlewares)
    )  
  )
}

// const storeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose

// const store = createStore(
//   rootReducer,
//   storeEnhancers(applyMiddleware(thunk))  
// )

// initialiseSagaMiddleware.run(apiSaga)

// export default configureStore

const store = configureStore()

export default store