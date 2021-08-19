import { api_user_login } from '@/api/login/user'
import {
  getToken,
  setToken,
  removeToken,
  getRefreshToken,
  setRefreshToken,
  setUserToken, getUserToken, removeUserToken
} from '@/utils/auth'
import jwt_decode from 'jwt-decode'
import { resetRouter } from '@/router'

const state = {
  token: getToken(),
  refreshToken: getRefreshToken(),
  userName: '',
  orgCode: '',
  orgName: '',
  userRole: '',
  userId: '',
  rankNm: '',
  jobNm: '',
  jobCd: '',
  approvalCount: 0,
  tel: '',
  isApprover: false,
  isAdmin: false,
  isAssetAdmin: false

}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_REFRESH_TOKEN: (state, refreshToken) => {
    state.refreshToken = refreshToken
  },
  SET_USER_NAME: (state, userName) => {
    state.userName = userName
  },
  SET_ROLE: (state, userRole) => {
    state.userRole = userRole
  },
  SET_USERID: (state, userId) => {
    state.userId = userId
  },
  SET_ORGCODE: (state, orgCode) => {
    state.orgCode = orgCode
  },
  SET_ORGNAME: (state, orgName) => {
    state.orgName = orgName
  },
  SET_RANKNM: (state, rankNm) => {
    state.rankNm = rankNm
  },
  SET_JOBNM: (state, jobNm) => {
    state.jobNm = jobNm
  },
  SET_JOBCD: (state, jobCd) => {
    state.jobCd = jobCd
  },
  SET_TEL: (state, tel) => {
    state.tel = tel
  },
  SET_ISADMIN: (state, isAdmin) => {
    state.isAdmin = isAdmin
  },
  SET_ISAPPROVER: (state, isApprover) => {
    state.isApprover = isApprover
  },
  SET_APPROVAL_COUNT: (state, approvalCount) => {
    state.approvalCount = approvalCount
  },
  SET_ISASSET_ADMIN: (state, isAssetAdmin) => {
    state.isAssetAdmin = isAssetAdmin
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { userId, password } = userInfo
    return new Promise((resolve, reject) => {
      api_user_login({
        userId: userId.trim(),
        password: password
      }).then(response => {
        const { data } = response
        if (data.pwdChange) {
          commit('SET_TOKEN', data.token)
          setToken(data.token)
          setUserToken(data.token)
          reject(data)
        } else {
          commit('SET_TOKEN', data.token)
          commit('SET_REFRESH_TOKEN', data.refreshToken)
          setToken(data.token)
          setUserToken(data.token)
          setRefreshToken(data.refreshToken)
          resolve()
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit }) {
    return new Promise((resolve) => {
      const token = jwt_decode(getUserToken())
      const { role, userName, id, orgCode, orgName, rankNm, jobNm, jobCd, isApprover, isAdmin, tel, isAssetAdmin } = token
      commit('SET_ROLE', role)
      commit('SET_USER_NAME', userName)
      commit('SET_USERID', id)
      commit('SET_ORGCODE', orgCode)
      commit('SET_ORGNAME', orgName)
      commit('SET_RANKNM', rankNm)
      commit('SET_JOBNM', jobNm)
      commit('SET_JOBCD', jobCd)
      commit('SET_TEL', tel)
      commit('SET_ISAPPROVER', isApprover)
      commit('SET_ISADMIN', isAdmin)
      commit('SET_ISASSET_ADMIN', isAssetAdmin)
      resolve(token)
    })
  },
  // user logout
  logout({ commit, dispatch }) {
    return new Promise((resolve) => {
      // adminLogout(state.token).then(() => {
      commit('SET_TOKEN', '')
      commit('SET_ROLE', '')
      commit('SET_REFRESH_TOKEN', '')
      removeToken()
      removeUserToken()
      resetRouter()
      // reset visited views and cached views
      // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
      dispatch('tagsView/delAllViews', null, { root: true })
      resolve()
    })
  }
  // remove token
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
