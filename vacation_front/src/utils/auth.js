
const TokenKey = 'Admin-Token'
const refreshKey = 'Admin-Refresh'
const userTokenKey = 'User-Token'

export function getToken() {
  return sessionStorage.getItem(TokenKey)
}

export function setToken(token) {
  return sessionStorage.setItem(TokenKey, token)
}

export function removeToken() {
  return sessionStorage.removeItem(TokenKey)
}

export function getRefreshToken() {
  return sessionStorage.getItem(refreshKey)
}

export function setRefreshToken(token) {
  return sessionStorage.setItem(refreshKey, token)
}

export function removeRefreshToken() {
  return sessionStorage.removeItem(refreshKey)
}

export function getUserToken() {
  return sessionStorage.getItem(userTokenKey)
}

export function setUserToken(token) {
  return sessionStorage.setItem(userTokenKey, token)
}

export function removeUserToken() {
  return sessionStorage.removeItem(userTokenKey)
}
