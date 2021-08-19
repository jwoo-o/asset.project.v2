import request from '@/utils/request'

export function api_admin_login(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export function admin_logout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

export function api_user_login2(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

