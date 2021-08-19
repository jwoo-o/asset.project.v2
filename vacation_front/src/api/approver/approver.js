import request from '@/utils/request'

export function api_approver_vacation_list(data) {
  return request({
    url: '/approverVacationList',
    method: 'GET',
    params: data
  })
}

export function api_vacation_approval(data) {
  return request({
    url: '/vacationApproval',
    method: 'put',
    data
  })
}

export function api_vacation_approval_mail(token, data) {
  return request({
    url: '/vacationApproval',
    headers: {
      'Authorization': 'Bearer ' + token
    },
    method: 'put',
    data
  })
}

export function api_vacation_approver_check(userId) {
  return request({
    url: `/approver/approver-check/${userId}`,
    method: 'get'
  })
}

export function api_approver_order_check(userId, vacationId, orderPosition) {
  return request({
    url: `/approver/order-check/${userId}/${orderPosition}/${vacationId}`,
    method: 'get'
  })
}
export function api_approval_count(userId) {
  return request({
    url: `/approver/approvalCount/${userId}`,
    method: 'get'
  })
}
export function api_approver_list() {
  return request({
    url: `/approver/list`,
    method: 'get'
  })
}

