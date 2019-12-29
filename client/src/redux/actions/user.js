import { env } from '../../common/api/env'
import { hasCredentials, setCredentials, removeCredentials, getAuthHeader } from '../../common/auth'

const verifyUrl = env.baseUrl + '/verify'

const verifyUserFetch = async () => {
  try {
    const resp = await fetch(`${verifyUrl}`, {
      headers: {
        ...getAuthHeader()
      }
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

export const verifyUser = () => async (dispatch) => {
  try {
    dispatch({ type: `REQUEST_USER_VERIFY` })
    const resolved = await verifyUserFetch()
    return dispatch({
      type: `SUCCESS_USER_VERIFY`,
      payload: resolved
    })
  } catch (err) {
    unassignCredentials()
    return dispatch({ type: `FAILURE_USER_VERIFY`, payload: err })
  }
}

export const checkCredentials = () => async (dispatch) => {
  dispatch({ type: 'REQUEST_USER_CREDENTIALS' })
  const result = hasCredentials()
  if (result) {
    return dispatch({ type: 'SUCCESS_USER_CREDENTIALS' })
  } else {
    return dispatch({ type: 'FAILURE_USER_CREDENTIALS' })
  }
}

export const unassignCredentials = () => async (dispatch) => {
  dispatch({ type: 'REQUEST_USER_CREDENTIALS_UNASSIGN' })
  removeCredentials()
}

export const assignCredentials = (username, password) => async (dispatch) => {
  setCredentials({ username, password })
  return dispatch({ type: 'REQUEST_USER_CREDENTIALS_ASSIGN' })
}

export default verifyUser