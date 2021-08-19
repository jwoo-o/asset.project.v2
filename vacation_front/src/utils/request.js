import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getRefreshToken, getToken } from '@/utils/auth'
import { api_refresh_token } from '@/api/login/auth'
import router from '@/router'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 30000 // request timeout
})

service.interceptors.request.use(
  config => {
    if (store.getters.token || getToken()) {
      config.headers['Authorization'] = `Bearer ` + getToken()
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data

    return res
  },
  error => {
    const config = error
    let message = error.response.data.message
    // let isAlreadyFetchingAccessToken = store.getters.toggleFetch
    // let isAlreadyFetchingAccessToken = false

    const adminId = store.getters.adminId
    const refreshToken = getRefreshToken()

    const errorCode = config.message.includes('401')

    if (config.message.includes('404')) {
      message = '요청하신 URL를 찾을수 없습니다'
    }
    if (config.message.includes('500')) {
      message = '요청하신 URL를 찾을수 없습니다'
    }
    if (config.message.includes('502')) {
      message = '잠시 후 다시 접속 바랍니다'
    }
    if (errorCode && refreshToken !== undefined && refreshToken !== null) {
      if (!store.getters.toggleFetch) {
        store.dispatch('app/toggleFetching', true)

        const data = {}
        data.adminId = adminId
        data.refreshToken = refreshToken

        api_refresh_token(data).then(response => {
          store.dispatch('app/toggleFetching', false)
          store.dispatch('admin/changeToken', response.data)
        }).catch(() => {
          store.dispatch('app/toggleFetching', false)
          store.dispatch('admin/logout')
          Message({
            message: '로그인이 만료되었습니다',
            type: 'error'
          })
          store.dispatch('admin/logout')
          router.push('/login/admin')
        })
      }
    } else {
      if (message !== '') {
        Message({
          message: message,
          type: 'error'
        })
      }
      return Promise.reject(error)
    }
  }
)

export default service
