import { getAuthHeader } from './auth'

const buildUrl = (url, params = {}) => {
  for (let param in params) {
    url = url.replace(`{${param}}`, params[param])
  }
}

const execute = async (
  resourceAction, 
  params = {}, 
  fields = {}, 
  auth = false,
  // okCallback,
  // serverErrorCallback,
  // unauthorizedCallback,
  // notFoundCallback,
  // badRequestCallback
  ) => {

  try {
    const url = buildUrl(resourceAction.url, params)
    const options = buildFetchOptions(resourceAction.method, fields, auth)
    const resp = await fetch(url, options)

    if (resp.ok) {
      const json = await resp.json()
      return json
    }
  }
  catch (err) {

  }

}

const buildFetchOptions = (method, fields = {}, auth) => {
  const options = {}
  options.method = method
  if (method === 'POST' || method === 'PUT') {
    const body = JSON.stringify(fields)
    options.body = body
  }

  let headers = {}

  if (auth) {
    headers = { ...getAuthHeader() }
  }

  if (headers !== {}) {
    options.headers = headers
  }

  return options
}

export default execute