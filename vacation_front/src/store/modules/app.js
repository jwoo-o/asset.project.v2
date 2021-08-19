import Cookies from 'js-cookie'
import { api_common_code } from '@/api/manager/common'
import { api_organization_hierarchy_search } from '@/api/organization/organization'
import { api_approver_code } from '@/api/approverGroup/approverGroup'
import { api_years_list_search, api_years_list_orgCode_search } from '@/api/vacation/vacation'
import { api_user_list_all } from '@/api/user/user'
import solarLunar from 'holiday-kr'
import Vue from 'vue'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  device: 'desktop',
  size: Cookies.get('size') || 'medium',
  commonCode: null,
  approverCode: null,
  isAlreadyFetchingAccessToken: false,
  orgList: [],
  width: window.innerWidth,
  height: window.innerHeight,
  mobile: window.innerWidth < 765,
  userList: [],
  orgUserList: [],
  yearsList: [],
  holidayList: [],
  yearsListByOrgCode: []
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  SET_WIDTH: (state, width) => {
    state.width = width
    state.mobile = width < 765
  },
  SET_HEIGHT: (state, height) => {
    state.height = height
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  SET_SIZE: (state, size) => {
    state.size = size
    Cookies.set('size', size)
  },
  TOGGLE_FETCHING: (state, toggle) => {
    state.isAlreadyFetchingAccessToken = toggle
  },
  SET_COMMON_CODE: (state, code) => {
    state.commonCode = code
  },
  SET_APPROVER_CODE: (state, code) => {
    state.approverCode = code
  },
  SET_ORG_LIST: (state, list) => {
    state.orgList = list
  },
  SET_USER_LIST: (state, list) => {
    state.userList = list
  },
  SET_ORG_USER_LIST: (state, list) => {
    state.orgUserList = list
  },
  SET_YEARS_LIST: (state, list) => {
    state.yearsList = list
  },
  SET_HOLIDAY_LIST: (state, list) => {
    state.holidayList = list
  },
  SET_YEARS_LIST_ORGCODE: (state, list) => {
    state.yearsListByOrgCode = list
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  toggleFetching({ commit }, toggle) {
    commit('TOGGLE_FETCHING', toggle)
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  setSize({ commit }, size) {
    commit('SET_SIZE', size)
  },
  setWidth({ commit }, width) {
    commit('SET_WIDTH', width)
  },
  setHeight({ commit }, height) {
    commit('SET_HEIGHT', height)
  },
  setCommonCode({ commit }, payload) {
    return new Promise((resolve, reject) => {
      api_common_code(payload).then(response => {
        const { data } = response
        commit('SET_COMMON_CODE', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  setApproverCode({ commit }, payload) {
    return new Promise((resolve, reject) => {
      api_approver_code(payload).then(response => {
        const { data } = response
        commit('SET_APPROVER_CODE', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  setOrgList({ commit }, mgrOrgCode) {
    return new Promise((resolve, reject) => {
      api_organization_hierarchy_search(mgrOrgCode, 'self').then(response => {
        const { data } = response
        commit('SET_ORG_LIST', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  setYearsList({ commit }) {
    return new Promise((resolve, reject) => {
      api_years_list_search().then(response => {
        const { data } = response
        commit('SET_YEARS_LIST', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  setYearsListByOrgCode({ commit }, orgCode) {
    return new Promise((resolve, reject) => {
      api_years_list_orgCode_search(orgCode).then(response => {
        const { data } = response
        commit('SET_YEARS_LIST_ORGCODE', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  setUserList({ commit }) {
    return new Promise((resolve, reject) => {
      api_user_list_all().then(response => {
        const { data } = response
        commit('SET_USER_LIST', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  setOrgUserList({ commit }, userList) {
    return commit('SET_ORG_USER_LIST', userList)
  },
  setHolidayList({ commit }, holidays) {
    const curYear = new Date()
    let minYear = curYear.getFullYear() - 50
    const maxYear = curYear.getFullYear() + 50
    const holidayList = []

    for (minYear; minYear <= maxYear; minYear++) {
      for (const holiday of holidays) {
        const isLunar = holiday.detailCode.split('-')[1] === 'l'
        const day = holiday.detailName.split('-')
        const curDate = new Date()
        curDate.setHours(0, 0, 0)
        let sorNext = false
        let thanksDay = false
        let newDay = false
        if (isLunar) {
          let solar
          if (holiday.detailName === '12-30') {
            solar = solarLunar.getSolar(minYear, 1, 1)
            curDate.setFullYear(solar.year, Number(solar.month - 1), solar.day)
            curDate.setDate(curDate.getDate() - 1)
          } else {
            solar = solarLunar.getSolar(minYear, Number(day[0]), Number(day[1]))
            curDate.setFullYear(solar.year, Number(solar.month - 1), solar.day)
          }
          /** 설날 대체 공휴일 */
          if (holiday.detailName === '12-30' && curDate.getDay() === 0) { newDay = true }
          if (holiday.detailName === '01-01' && curDate.getDay() === 0) { newDay = true }
          if (holiday.detailName === '01-02' && curDate.getDay() === 0) { newDay = true }

          /** 추석 대체 공휴일 */
          if (holiday.detailName === '08-14' && curDate.getDay() === 0) { thanksDay = true }
          if (holiday.detailName === '08-15' && curDate.getDay() === 0) { thanksDay = true }
          if (holiday.detailName === '08-16' && curDate.getDay() === 0) { thanksDay = true }
        } else {
          curDate.setFullYear(minYear, Number(day[0] - 1), Number(day[1]))
          /** 5월 5일 대체 공휴일 */
          if (holiday.detailName === '05-05' && curDate.getDay() === 0) { sorNext = true }
        }
        holidayList.push(Vue.moment(curDate).format('YYYY-MM-DD'))
        if (sorNext) {
          curDate.setFullYear(minYear, Number(day[0] - 1), Number(day[1] + 1))
          holidayList.push(Vue.moment(curDate).format('YYYY-MM-DD'))
        } else if (newDay) {
          const solar = solarLunar.getSolar(minYear, 1, 3)
          curDate.setFullYear(solar.year, Number(solar.month - 1), solar.day)
          holidayList.push(Vue.moment(curDate).format('YYYY-MM-DD'))
        } else if (thanksDay) {
          const solar = solarLunar.getSolar(minYear, 8, 17)
          curDate.setFullYear(solar.year, Number(solar.month - 1), solar.day)
          holidayList.push(Vue.moment(curDate).format('YYYY-MM-DD'))
        }
      }
    }
    commit('SET_HOLIDAY_LIST', holidayList)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
