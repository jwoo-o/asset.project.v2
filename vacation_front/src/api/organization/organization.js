import request from '@/utils/request'

export function api_organization_hierarchy_search(orgCode, type) {
  return request({
    url: `/organization_charts/${orgCode}`,
    method: 'get',
    params: {
      'type': type
    }
  })
}

export function api_organization_org_code() {
  return request({
    url: `/organization_charts/code`,
    method: 'get'
  })
}

export function api_organization_org_code_check(orgCode) {
  return request({
    url: `/organization_charts/${orgCode}/check`,
    method: 'get'
  })
}
export function api_organization_register(data) {
  return request({
    url: `/organization_charts`,
    method: 'post',
    data
  })
}

export function api_organization_delete(orgCode) {
  return request({
    url: `/organization_charts/${orgCode}`,
    method: 'delete'
  })
}

export function api_organization_update(data) {
  return request({
    url: `/organization_charts`,
    method: 'put',
    data
  })
}

export function api_organization_order(order) {
  return request({
    url: `/organization_charts/order/${order}`,
    method: 'get'
  })
}
