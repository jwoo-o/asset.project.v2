import request from '@/utils/request'
export function api_adminId_check(adminId) {
  return request({
    url: '/admin/' + adminId + '/check',
    method: 'get'
  })
}
export function api_admin_pwd_edit(data) {
  return request({
    url: '/admin',
    method: 'patch',
    data
  })
}

export function api_user_login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function api_admin_login(data) {
  return request({
    url: '/login/admin',
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

export function api_admin_register(data) {
  return request({
    url: '/admin',
    method: 'post',
    data
  })
}

export function api_admin_edit(data) {
  return request({
    url: '/admin',
    method: 'put',
    data
  })
}
export function api_admin_delete(adminId) {
  return request({
    url: `/admin/${adminId}`,
    method: 'delete'
  })
}
export function api_admin_list(data) {
  return request({
    url: '/admin',
    method: 'get',
    params: data
  })
}

export function api_admin_info(adminId) {
  return request({
    url: '/admin/' + adminId,
    method: 'get'
  })
}

