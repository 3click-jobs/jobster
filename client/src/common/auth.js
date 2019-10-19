import { env } from './env'

const hasCredentials = () => {
  return !!localStorage.getItem(env.localStorageKey)
}

const getAuthHeader = () => {
  const token = localStorage.getItem(env.localStorageKey)
  return { Authorization: `Basic ${token}` }
}

const setCredentials = ({ username, password }) => {
  const base64 = btoa(`${username}:${password}`)
  localStorage.setItem(env.localStorageKey, base64)
}

const removeCredentials = () => {
  localStorage.removeItem(env.localStorageKey)
}

export { hasCredentials, setCredentials, removeCredentials, getAuthHeader }