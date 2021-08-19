import request from '@/utils/request'

export function api_vacation_list(data) {
  return request({
    url: '/vacation/vacation-list',
    method: 'GET',
    params: data
  })
}

export function api_vacation_detail(vacationId) {
  return request({
    url: `/vacation/vacation-detail/${vacationId}`,
    method: 'GET'
  })
}

export function api_vacation_insert(data) {
  return request({
    url: `/vacation`,
    method: 'post',
    data
  })
}
export function api_vacation_update(data) {
  return request({
    url: `/vacation/edit`,
    method: 'put',
    data
  })
}
export function api_vacation_delete(vacationId) {
  return request({
    url: `/vacation/vacation-delete/${vacationId}`,
    method: 'delete'
  })
}
export function api_years_list_search() {
  return request({
    url: `/vacation/years`,
    method: 'get'
  })
}
export function api_years_list_orgCode_search(orgCode) {
  return request({
    url: `/vacation/years/org/${orgCode}`,
    method: 'get'
  })
}
export function api_years_list_search_userId(userId) {
  return request({
    url: `/vacation/years/${userId}`,
    method: 'get'
  })
}
export function api_user_vacation_info_list(data) {
  return request({
    url: `/vacation`,
    method: 'GET',
    params: data
  })
}
export function api_user_vacation_list(userId, data, token) {
  return request({
    url: `/vacation/${userId}`,
    headers: {
      'Authorization': 'Bearer ' + token
    },
    method: 'GET',
    params: data
  })
}
export function api_vacation_file_upload(data) {
  return request({
    url: `/vacation/file`,
    method: 'POST',
    data
  })
}
export function api_vacation_file_delete(fileId) {
  return request({
    url: `/vacation/file/${fileId}`,
    method: 'DELETE'
  })
}
export function api_vacation_calendar_list(data) {
  return request({
    url: `/vacation/calendar`,
    method: 'GET',
    params: data
  })
}
export function api_vacation_date_check(userId, startDay, endDay, vacationId) {
  return request({
    url: `/vacation/date_check/${userId}/${startDay}/${endDay}/${vacationId}`,
    method: 'get'
  })
}
export function api_user_total_update(userId, data) {
  return request({
    url: `/vacation/total/${userId}`,
    method: 'put',
    data
  })
}
export function api_admin_delete_vacation(data) {
  return request({
    url: `/vacation/admin/delete`,
    method: 'delete',
    params: data
  })
}
export function api_vacation_cancel(data) {
  return request({
    url: `/vacation/cancel`,
    method: 'post',
    data
  })
}
export function api_vacation_deadline_sender(data) {
  return request({
    url: `/vacation/deadline/sender`,
    method: 'post',
    data
  })
}

export function api_vacation_change_list(userId, data) {
  return request({
    url: `/vacation/log/${userId}`,
    method: 'get',
    params: data
  })
}

