import request from '@/utils/request'

export function api_certificate_request(data) {
  return request({
    url: `/certificate-request`,
    method: 'post',
    data
  })
}
export function api_certificates_request(data) {
  return request({
    url: `/certificate-request/all`,
    method: 'post',
    data
  })
}

export function api_certificate_list(data) {
  return request({
    url: `/certificate-request`,
    method: 'get',
    params: data
  })
}
export function api_certificate_userId(userId, data) {
  return request({
    url: `/certificate-request/${userId}`,
    method: 'get',
    params: data
  })
}

export function api_certificate_print(id) {
  return request({
    url: `/certificate-request/${id}/download`,
    method: 'get'
  })
}
export function api_certificate_print_success(id) {
  return request({
    url: `/certificate-request/${id}/download`,
    method: 'patch'
  })
}
export function api_certificate_state(id, data) {
  return request({
    url: `/certificate-request/${id}`,
    method: 'patch',
    data
  })
}
export function api_certificate_count() {
  return request({
    url: '/certificate-request/count',
    method: 'get'
  })
}
