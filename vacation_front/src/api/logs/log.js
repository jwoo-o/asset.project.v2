import request from '@/utils/request'

export function api_access_log_list(data) {
  return request({
    url: '/logs/access-log',
    method: 'get',
    params: data
  })
}
export function api_admin_log_list(data) {
  return request({
    url: '/logs/admin-log',
    method: 'get',
    params: data
  })
}
export function api_excel_access_log_list(data) {
  return request({
    url: '/logs/excel-access-log',
    method: 'get',
    params: data
  })
}
export function api_excel_admin_access_log_list(data) {
  return request({
    url: '/logs/excel-admin-access-log',
    method: 'get',
    params: data
  })
}
export function api_excel_admin_log_list(data) {
  return request({
    url: '/logs/excel-admin-log',
    method: 'get',
    params: data
  })
}

