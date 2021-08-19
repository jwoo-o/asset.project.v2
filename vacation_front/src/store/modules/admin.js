import { api_admin_login } from '@/api/login/admin'
import { getToken, setToken, removeToken, getRefreshToken, setRefreshToken, removeRefreshToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import jwt_decode from 'jwt-decode'

const state = {
  token: getToken(),
  refreshToken: getRefreshToken(),
  name: '',
  mgrOrgCode: '',
  mgrOrgName: '',
  role: '',
  adminId: ''
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_REFRESH_TOKEN: (state, refreshToken) => {
    state.refreshToken = refreshToken
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_ROLE: (state, role) => {
    state.role = role
  },
  SET_LEVEL: (state, level) => {
    state.level = level
  },
  SET_ADMINID: (state, adminId) => {
    state.adminId = adminId
  },
  SET_MGRORGCODE: (state, mgrOrgCode) => {
    state.mgrOrgCode = mgrOrgCode
  },
  SET_MGRORGNAME: (state, mgrOrgName) => {
    state.mgrOrgName = mgrOrgName
  },
  SET_ORDER: (state, order) => {
    state.order = order
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { adminId, password } = userInfo
    return new Promise((resolve, reject) => {
      api_admin_login({ adminId: adminId.trim(), password: password }).then(response => {
        const { data } = response
        if (data.pwdChange) {
          commit('SET_TOKEN', data.token)
          setToken(data.token)
          reject(data)
        } else {
          commit('SET_TOKEN', data.token)
          commit('SET_REFRESH_TOKEN', data.refreshToken)
          setToken(data.token)
          setRefreshToken(data.refreshToken)
          resolve()
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      const token = jwt_decode(getToken())
      const { role, name, mgrOrgCode, id, order, mgrOrgName } = token
      commit('SET_ROLE', role)
      commit('SET_NAME', name)
      commit('SET_MGRORGCODE', mgrOrgCode)
      commit('SET_MGRORGNAME', mgrOrgName)
      commit('SET_ORDER', order)
      commit('SET_ADMINID', id)
      resolve(token)
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      // adminLogout(state.token).then(() => {
      commit('SET_TOKEN', '')
      commit('SET_ROLE', '')
      commit('SET_REFRESH_TOKEN', '')
      removeToken()
      removeRefreshToken()
      resetRouter()

      // reset visited views and cached views
      // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
      dispatch('tagsView/delAllViews', null, { root: true })
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLE', '')
      removeToken()
      resolve()
    })
  },

  changeToken({ commit }, data) {
    return new Promise(resolve => {
      commit('SET_TOKEN', data)
      setToken(data)
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
