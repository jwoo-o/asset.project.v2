import request from '@/utils/request'

export function api_refresh_token(data) {
  return request({
    url: '/oauth/admin',
    method: 'post',
    data
  })
}
