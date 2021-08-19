export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * @param {string} ip
 * @returns {Boolean}
 */
export function validIp(ip) {
  const reg = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
  return reg.test(ip)
}

/**
 * @param {string} tel
 * @returns {Boolean}
 */
export function validTel(tel) {
  const reg = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/
  return reg.test(tel)
}

/**
 * @param {string} id
 * @returns {Boolean}
 */
export function validId(id) {
  const reg = /^[A-Za-z0-9_-]{2,20}$/
  return reg.test(id)
}

/**
 * @param {string} name
 * @returns {Boolean}
 */
export function validName(name) {
  const reg = /^[가-힣a-zA-Z0-9_-]+$/
  return reg.test(name)
}

/**
 * @param {string} name
 * @returns {Boolean}
 */
export function validNumber(name) {
  const reg = /^[0-9]*$/
  return reg.test(name)
}

/**
 * @param {string} domain
 * @returns {Boolean}
 */
export function validDomain(domain) {
  const reg = /(http(s)?:\/\/|www.)([a-z0-9\w]+\.*)+[a-z0-9]{2,4}/gi
  return reg.test(domain)
}

/**
 * @param {string} protocol
 * @returns {Boolean}
 */
export function validProtocol(protocol) {
  const reg = /(http(s)?:\/\/|www.)([a-z0-9\w]+\.*)+[a-z0-9]{2,4}/gi
  return reg.test(protocol)
}

/**
 * @param {string} protocol
 * @returns {Boolean}
 */
export function validPortNumber(portNumber) {
  const reg = /^()([1-9]|[1-5]?[0-9]{2,4}|6[1-4][0-9]{3}|65[1-4][0-9]{2}|655[1-2][0-9]|6553[1-5])$/gm
  return reg.test(portNumber)
}

/**
 * @param {string} exe
 * @param {string} type
 * @returns {Boolean}
 */
export function validProcess(exe, type) {
  let reg = /^/
  if (type === 'exe') {
    reg = /(.*?)\.(exe|npe|com)$/
  }
  return reg.test(exe)
}

/**
 * @param {string} exe
 * @param {string} type
 * @returns {Boolean}
 */
export function validImage(value) {
  const reg = /(.*?)\.(jpg|png|JPG|PNG|JPEG|jpeg)$/
  return reg.test(value)
}

/**
 * @param {string} value
 * @param {string} type
 * @returns {Boolean}
 */
export function validNetwork(value, type) {
  let result = false
  if (type === 'ip') {
    result = validIp(value)
  } else if (type === 'domain') {
    result = validDomain(value)
  } else if (type === 'protocol') {
    /* result = validProtocol(value)*/
    result = true
  } else if (type === 'port') {
    result = validPortNumber(value)
  }
  return result
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}
