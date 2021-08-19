import axios from 'axios'
import { Message } from 'element-ui'
import { getUserToken } from '@/utils/auth'
// import store from '@/store'

const service1 = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 30000 // request timeout
})

service1.interceptors.request.use(
  config => {
    if (getUserToken()) {
      config.headers['agent-token'] = getUserToken()
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

service1.interceptors.response.use(
  response => {
    const res = response.data

    return res
  },
  error => {
    const config = error
    let message = error.response.data.message

    if (config.message.includes('404')) {
      message = '요청하신 URL를 찾을수 없습니다'
    }
    if (config.message.includes('500')) {
      message = '요청하신 URL를 찾을수 없습니다'
    }
    /* if (config.message.includes('401')) {
      store.dispatch('user/logout')
      location.reload()
    }*/
    if (message !== '') {
      Message({
        message: message,
        type: 'error'
      })
    }

    return Promise.reject(error)
  }
)

export default service1
