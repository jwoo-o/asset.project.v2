import request from '@/utils/request'

export function api_asset_id_next(category) {
  return request({
    url: '/asset/next-id',
    method: 'get',
    params: { 'category': category }
  })
}

export function api_asset_register(data) {
  return request({
    url: '/asset',
    method: 'post',
    data
  })
}
export function api_asset_update(data) {
  return request({
    url: '/asset',
    method: 'put',
    data
  })
}

export function api_asset_list(data) {
  return request({
    url: '/asset',
    method: 'get',
    params: data
  })
}
export function api_asset_image_upload(data) {
  return request({
    url: '/asset/image',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}
export function api_asset_image_delete(assetId) {
  return request({
    url: `/asset/image/${assetId}`,
    method: 'delete'
  })
}
export function api_asset_detail(assetId) {
  return request({
    url: `/asset/${assetId}`,
    method: 'get'
  })
}
export function api_asset_detail_no_token(assetId) {
  return request({
    url: `/asset/token/${assetId}`,
    method: 'get'
  })
}

export function api_excel_asset_list(data) {
  return request({
    url: '/asset/excel',
    method: 'get',
    params: data
  })
}

export function api_asset_total(orgCode) {
  return request({
    url: `/asset/total`,
    method: 'get',
    params: { 'orgCode': orgCode }
  })
}
