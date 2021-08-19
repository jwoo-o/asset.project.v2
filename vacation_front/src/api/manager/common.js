import request from '@/utils/request'

export function api_common_code(data) {
  return request({
    url: '/common-code',
    method: 'get',
    params: { 'query': data }
  })
}
export function api_common_code_list(data) {
  return request({
    url: '/common',
    method: 'get',
    params: data
  })
}
export function api_common_code_detail(groupCode) {
  return request({
    url: `/common/${groupCode}`,
    method: 'get'
  })
}
export function api_common_code_check(groupCode) {
  return request({
    url: `/common/${groupCode}/check`,
    method: 'get'
  })
}

export function api_common_code_register(data) {
  return request({
    url: '/common',
    method: 'post',
    data
  })
}
export function api_common_code_edit(data) {
  return request({
    url: `/common`,
    method: 'put',
    data
  })
}

