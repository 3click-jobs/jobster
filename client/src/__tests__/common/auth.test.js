import { hasCredentials, setCredentials, removeCredentials, getAuthHeader } from '../../common/auth'
import { env } from '../../common/env'

describe('authorization helpers', () => {
  describe('base64 conversion for username:password working', () => {
    test('username:password equals dXNlcm5hbWU6cGFzc3dvcmQ=', () => {
      expect(btoa('username:password')).toBe('dXNlcm5hbWU6cGFzc3dvcmQ=')
    })
    test('dXNlcm5hbWU6cGFzc3dvcmQ= equals username:password', () => {
      expect(atob('dXNlcm5hbWU6cGFzc3dvcmQ=')).toBe('username:password')
    })
  })
  describe('working with local storage', () => {

    // SETUP clear key
    let currentKey = localStorage.getItem(env.localStorageKey)

    beforeEach(() => {
      localStorage.removeItem(env.localStorageKey)
    })

    afterEach(() => {
      localStorage.removeItem(env.localStorageKey)
    })

    it('set credentials sets token to expected value', () => {
      // Set credentials by converting to base64
      let username = 'username'
      let password = 'password'

      let expected = btoa(`${username}:${password}`)

      setCredentials({ username: username, password: password })

      let actual = localStorage.getItem(env.localStorageKey)

      expect(actual).toEqual(expected)
    })
    

    it('has credentials returns true if there is a token', () => {
      localStorage.setItem(env.localStorageKey, 'randomstring')

      // NOTE: IT DOES NOT CHECK IF TOKEN IS WELL FORMED AT ALL!
      let expected = true

      let actual = hasCredentials()

      expect(actual).toEqual(expected)
    })

    it('has credentials returns false if there is no token', () => {
      localStorage.removeItem(env.localStorageKey)

      let expected = false

      let actual = hasCredentials()

      expect(actual).toEqual(expected)
    })

    it('remove credentials removes the token, token is null', () => {
      localStorage.setItem(env.localStorageKey, 'randomstring')

      removeCredentials()

      expect(localStorage.getItem(env.localStorageKey)).toBeNull()      
    })

    it('get auth header returns authorization header with token from local storage', () => {
      localStorage.setItem(env.localStorageKey, 'randomstring')

      let expected = { Authorization: 'Basic randomstring'}

      let actual = getAuthHeader()

      expect(actual).toStrictEqual(expected)

      expect(actual).toHaveProperty('Authorization')
      expect(actual).toHaveProperty('Authorization', 'Basic randomstring')
    })

    // CLEARUP
    localStorage.setItem(env.localStorageKey, currentKey)
  })
  test.todo('has credentials does not check well-formedness of token!')
  test.todo('test env')
})