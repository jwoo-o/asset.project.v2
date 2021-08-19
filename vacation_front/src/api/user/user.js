import request from '@/utils/request'

export function api_user_list(data) {
  return request({
    url: '/user/user-list',
    method: 'get',
    params: data
  })
}
export function api_user_list_all() {
  return request({
    url: '/user',
    method: 'get'
  })
}

export function api_user_list_userId(data) {
  return request({
    url: '/user/user-list-userId',
    method: 'get',
    params: data
  })
}

export function api_org_user_list(orgCode) {
  return request({
    url: `/user/user-list/${orgCode}`,
    method: 'get'
  })
}
export function api_user_check(userId) {
  return request({
    url: `/user/${userId}/check`,
    method: 'get'
  })
}
export function api_user_register(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function api_user_edit(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}
export function api_user_delete(userId, leaveDate) {
  return request({
    url: `/user/${userId}`,
    method: 'delete',
    params: {
      leaveDate: leaveDate
    }
  })
}
export function api_user_info(userId) {
  return request({
    url: `/user/${userId}`,
    method: 'get'
  })
}
export function api_user_info_list(data) {
  return request({
    url: '/user',
    method: 'get',
    params: data
  })
}
export function api_user_pwd_edit(data) {
  return request({
    url: '/user',
    method: 'patch',
    data
  })
}
export function api_user_image_delete(userId) {
  return request({
    url: `/user/image/${userId}`,
    method: 'delete'
  })
}
export function api_user_image_upload(data) {
  return request({
    url: `/user/image`,
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}

export function api_user_seat_list() {
  return request({
    url: '/seat',
    method: 'get'
  })
}

