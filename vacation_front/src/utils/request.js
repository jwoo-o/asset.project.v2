import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getRefreshToken, getToken } from '@/utils/auth'
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
    if (errorCode && refreshToken !== null) {
      Message({
        message: '로그인이 만료되었습니다',
        type: 'error'
      })
      if (store.getters.role === 'ROLE_USER') {
        store.dispatch('user/logout')
        router.push('/')
      } else if (store.getters.role === 'ROLE_ADMIN') {
        store.dispatch('admin/logout')
        router.push('/mng')
      }
    } else {
      if (message !== '') {
        Message({
          message: message,
          type: 'error'
        })
        return Promise.reject(error)
      }
    }
  }
)

export default service
