import { api_approval_count } from '../../api/approver/approver'

const state = {
  approvalCount: 0
}

const mutations = {
  SET_APPROVAL_COUNT: (state, approvalCount) => {
    state.approvalCount = approvalCount
  }
}

const actions = {
  approvalCheck({ commit }, userId) {
    return new Promise((resolve, reject) => {
      api_approval_count(userId).then(response => {
        const { data } = response
        commit('SET_APPROVAL_COUNT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
