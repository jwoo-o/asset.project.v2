import request from '@/utils/request'

export function api_approver_code(data) {
  return request({
    url: '/approver-code',
    method: 'get',
    params: { 'query': data }
  })
}
export function api_approver_code_list(data) {
  return request({
    url: '/approver',
    method: 'get',
    params: data
  })
}
export function api_approver_detail(approverGroupCode) {
  return request({
    url: `/approver/${approverGroupCode}`,
    method: 'get'
  })
}

export function api_approver_detail_orgcode(orgCode, jobCode) {
  return request({
    url: `/approver/user/${orgCode}/${jobCode}`,
    method: 'get'
  })
}
export function api_approver_check(orgCode, jobCode) {
  return request({
    url: `/approver/${orgCode}/${jobCode}/check`,
    method: 'get'
  })
}

export function api_approver_register(data) {
  return request({
    url: '/approver',
    method: 'post',
    data
  })
}
export function api_approver_edit(data) {
  return request({
    url: `/approver`,
    method: 'put',
    data
  })
}

export function api_approver_group_list(data) {
  return request({
    url: '/approverGroup',
    method: 'get',
    params: data
  })
}
export function api_approvel_group_delete(id) {
  return request({
    url: `/approverGroup/${id}`,
    method: 'delete'
  })
}
