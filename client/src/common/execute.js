import { getAuthHeader } from './auth'

const buildUrl = (url, params = {}) => {
  for (let param in params) {
    url = url.replace(`{${param}}`, params[param])
  }

  return url
}

// maybe a parent param, like in StyledInput ?

const execute = async (
  resourceAction,
  fields = {},
  params = {},
  auth = false,
  // okCallback,
  // serverErrorCallback,
  // unauthorizedCallback,
  // notFoundCallback,
  // badRequestCallback
) => {

  try {

    console.log(resourceAction)
    const url = buildUrl(resourceAction.url, params)
    const options = buildFetchOptions(resourceAction.method, fields, auth)

    console.log('options:', options)

    const resp = await fetch(url, options)

    if (resp.ok) {
      const json = await resp.json()

      console.log('json: ', json)
      return json
    } else {
      console.log('resp not ok: ', resp)

      const json = await resp.json()

      console.log('json: ', json)
    }
  }
  catch (err) {
    console.log('error: ', err)
  }

}

const buildFetchOptions = (method, fields = {}, auth) => {
  const options = {}
  options.method = method
  // options.mode = 'no-cors'

  if (method === 'POST' || method === 'PUT') {
    const body = JSON.stringify(fields)
    options.body = body
    console.log(body)
  }

  let headers = {}

  if (auth) {
    headers = { ...getAuthHeader() }
  }

  headers['Content-Type'] = 'application/json'
  // headers['Accept'] = '*/*'
  // headers['Host'] = 'localhost:8080'
  // headers['Access-Control-Allow-Origin'] = '*'
  // headers['Origin'] = 'localhost:8080'
  // const base64 = btoa('username:password')
  // headers['Authorization'] = 'Basic ' + base64

  if (headers !== {}) {
    options.headers = headers
  }

  return options
}

export default execute