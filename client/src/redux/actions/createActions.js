import rest from '../../common/api/rest'
import { getOne, getAll, getAny, createResource, updateResource, deleteResource } from '../../common/utils/fetchApi'

import { normalize, schema } from 'normalizr'

const createActions = (entity, idKey = 'id') => {

  const one = new schema.Entity(entity, {}, { idAttribute: idKey })
  const many  = [one]

  const fetchOne = (id) => async (dispatch) => {
    const entityUpper = entity.toUpperCase()
    try {
      dispatch({ type: `REQUEST_${entityUpper}_GETONE`})
      const resolved = await getOne(rest[entity].actions.get.url, id)
      return dispatch({
        type: `SUCCESS_${entityUpper}_GETONE`,
        payload: normalize(resolved, one)
      })
    } catch (err) {
      return dispatch({ type: `FAILURE_${entityUpper}_GETONE`, payload: err })
    }
  }
  
  const fetchAll = () => async (dispatch) => {
    const entityUpper = entity.toUpperCase()

    // console.log('rest entity: ', rest[entity] );

    try {

      // console.log('rest entity: ', rest[entity].actions.get.url );

      dispatch({ type: `REQUEST_${entityUpper}_GETALL`})

      

      const resolved = await getAll(rest[entity].actions.get.url)
      console.log('CREATEACTIONS fetchAll RESOLVED: ', resolved)
  
      console.log('CREATEACTIONS fetchAll SCHEMA: ', many)
      console.log('CREATEACTIONS fetchAll NORMALIZED: ', normalize(resolved, many))
      return dispatch({
        type: `SUCCESS_${entityUpper}_GETALL`,
        payload: normalize(resolved, many)
      })
    } catch (err) {
      return dispatch({ type: `FAILURE_${entityUpper}_GETALL`, payload: err })
    }
  }
  
  const fetchAny = (query) => async (dispatch) => {
    const entityUpper = entity.toUpperCase()

    //  console.log('IDKEY: ', idKey)

    try {
      dispatch({ type: `REQUEST_${entityUpper}_GETANY`})
      const resolved = await getAny(rest[entity].actions.get.url, query)
      return dispatch({
        type: `SUCCESS_${entityUpper}_GETANY`,
        payload: normalize(resolved, many),
        meta: { query }
      })
    } catch (err) {
      return dispatch({ type: `FAILURE_${entityUpper}_GETANY`, payload: err })
    }
  }

  const create = (payload) => async (dispatch) => {
    const entityUpper = entity.toUpperCase()
    try {
      dispatch({ type: `REQUEST_${entityUpper}_CREATE`})
      const resolved = await createResource(rest[entity].actions.create.url, payload)

      console.log('createActions IDKEY received: ', idKey)
      console.log('createActions CREATE resolved value from response: ', resolved)
      console.log('createActions CREATE normalized value from response: ', normalize(resolved, one))

      return dispatch({
        type: `SUCCESS_${entityUpper}_CREATE`,
        payload: normalize(resolved, one)
      })
    } catch (err) {
      return dispatch({ type: `FAILURE_${entityUpper}_CREATE`, payload: err })
    }
  }

  const update = (id, payload) => async (dispatch) => {
    const entityUpper = entity.toUpperCase()
    
    if (payload[idKey] === 0 || !payload[idKey]) {
      payload[idKey] = id
    }
    try {
      dispatch({ type: `REQUEST_${entityUpper}_UPDATE`})
      const resolved = await updateResource(rest[entity].actions.update.url, id, payload)
      return dispatch({
        type: `SUCCESS_${entityUpper}_UPDATE`,
        payload: normalize(resolved, one)
      })
    } catch (err) {
      return dispatch({ type: `FAILURE_${entityUpper}_UPDATE`, payload: err })
    }
  }

  const remove = (id) => async (dispatch) => {
    const entityUpper = entity.toUpperCase()
    try {
      dispatch({ type: `REQUEST_${entityUpper}_DELETE`})
      const resolved = await deleteResource(rest[entity].actions.delete.url, id)
      return dispatch({
        type: `SUCCESS_${entityUpper}_DELETE`,
        payload: normalize(resolved, one)
      })
    } catch (err) {
      return dispatch({ type: `FAILURE_${entityUpper}_DELETE`, payload: err })
    }
  }

  const decline = (payload) => async (dispatch) => {
    const entityUpper = entity.toUpperCase()
    try {
      dispatch({ type: `REQUEST_${entityUpper}_DECLINE`})
      // const resolved = await decline(rest[entity].actions.decline.url, payload.id)
      return dispatch({
        type: `SUCCESS_${entityUpper}_DECLINE`,
        payload: normalize(payload, one)
      })
    } catch (err) {
      return dispatch({ type: `FAILURE_${entityUpper}_DECLINE`, payload: err })
    }
  }

  const accept = (payload) => async (dispatch) => {
    const entityUpper = entity.toUpperCase()
    try {
      dispatch({ type: `REQUEST_${entityUpper}_ACCEPT`})
      // const resolved = await accept(rest[entity].actions.accept.url, payload.id)
      return dispatch({
        type: `SUCCESS_${entityUpper}_ACCEPT`,
        payload: normalize(payload, one)
      })
    } catch (err) {
      return dispatch({ type: `FAILURE_${entityUpper}_ACCEPT`, payload: err })
    }
  }

  return { fetchAll, fetchAny, fetchOne, create, update, remove, decline, accept }
}

export default createActions