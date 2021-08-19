import request from '@/utils/request'

export function api_notice_register(data) {
  return request({
    url: '/board/notice',
    method: 'post',
    data
  })
}

export function api_notice_list(data) {
  return request({
    url: '/board/notice',
    method: 'get',
    params: data
  })
}
export function api_notice_count_up(id) {
  return request({
    url: `/board/notice/${id}`,
    method: 'patch'
  })
}
export function api_notice_info(id) {
  return request({
    url: `/board/notice/${id}`,
    method: 'get'
  })
}
export function api_notice_delete(id) {
  return request({
    url: `/board/notice/${id}`,
    method: 'delete'
  })
}
export function api_notice_edit(id, data) {
  return request({
    url: `/board/notice/${id}`,
    method: 'put',
    data
  })
}
export function api_notice_file_upload(data) {
  return request({
    url: '/board/notice/file',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}

export function api_notice_file_delete(id) {
  return request({
    url: `/board/notice/file/${id}`,
    method: 'delete'
  })
}
