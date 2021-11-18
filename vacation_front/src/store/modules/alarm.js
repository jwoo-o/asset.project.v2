import { api_certificate_count } from '@/api/certificate'

const state = {
  alarmCount: 0
}

const mutations = {
  SET_ALARM_COUNT: (state, alarmCount) => {
    state.alarmCount = alarmCount
  }
}

const actions = {
  alarmCheck({ commit }, userId) {
    return new Promise((resolve, reject) => {
      api_certificate_count().then(response => {
        const { data } = response
        commit('SET_ALARM_COUNT', data)
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
