import request from '@/utils/request'

export function api_user_login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

/* export function api_user_logout(pcId) {
  return request({
    url: `/client/logout/${pcId}`,
    method: 'post'
  })
}

export function api_user_pwd_edit(data) {
  return request({
    url: '/client/user',
    method: 'patch',
    data
  })
}
export function api_user_register(data) {
  return request({
    url: '/client/user',
    method: 'post',
    data
  })
}
export function api_user_check(userId) {
  return request({
    url: `/client/user/${userId}/check`,
    method: 'get'
  })
}
export function api_user_organization() {
  return request({
    url: `/client/organization`,
    method: 'get'
  })
}*/

