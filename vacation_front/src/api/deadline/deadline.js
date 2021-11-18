import request from '@/utils/request'

export function api_user_deadline_list(data, token) {
  return request({
    url: '/vacation/deadline',
    headers: {
      'Authorization': 'Bearer ' + token
    },
    method: 'get',
    params: data
  })
}
export function api_user_deadline_register(data, token) {
  return request({
    url: '/vacation/deadline',
    headers: {
      'Authorization': 'Bearer ' + token
    },
    method: 'post',
    data
  })
}
export function api_user_deadline_confirm(data, token) {
  return request({
    url: '/vacation/deadline',
    headers: {
      'Authorization': 'Bearer ' + token
    },
    method: 'patch',
    data
  })
}

