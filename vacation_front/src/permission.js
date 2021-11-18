import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/', '/mng', '/approve-mail', '/annual-deadline', '/asset-info'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()
  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()
  if (hasToken) {
    if (to.path === '/mng') {
      next({ name: 'admin' })
      NProgress.done()
    } else if (to.path === '/') {
      // if is logged in, redirect to the home page
      next({ name: 'user-home' })
      NProgress.done()
    } else {
      const hasRoles = store.getters.role && store.getters.role.length > 0
      if (hasRoles) {
        if (store.getters.role === 'ROLE_USER') {
          await store.dispatch('approver/approvalCheck', store.getters.userId)
        }
        if (store.getters.role === 'ROLE_ADMIN') {
          await store.dispatch('alarm/alarmCheck')
        }
        next()
      } else {
        try {
          const { role } = await store.dispatch('admin/getInfo')
          if (role === 'ROLE_ADMIN') {
            await store.dispatch('app/setYearsList')
            await store.dispatch('app/setUserList')
            await store.dispatch('app/setOrgList', '00000000')
          } else {
            const { isAssetAdmin } = await store.dispatch('user/getInfo')
            if (isAssetAdmin) {
              await store.dispatch('app/setOrgList', store.getters.mgrOrgCode)
            } else {
              await store.dispatch('app/setOrgList', store.getters.orgCode)
            }

            await store.dispatch('app/setYearsListByOrgCode', store.getters.orgCode)
          }
          const { holiday } = await store.dispatch('app/setCommonCode', 'all')
          await store.dispatch('app/setHolidayList', holiday)
          // await store.dispatch('app/setApproverCode', 'all')
          if (to.meta.auth) {
            if (to.meta.auth.indexOf(role) === -1) {
              throw new Error('Not Permission')
            }
          }
          next({ ...to, replace: true })
        } catch (error) {
          console.log(error)
          // remove token and go to login page to re-login
          await store.dispatch('admin/logout')
          await store.dispatch('user/logout')
          // Message.error(error || 'Has Error')
          next(`/`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      if (to.path === 'mng' || to.path === '/') {
        await store.dispatch('app/setCommonCode', 'regex')
      }
      if (to.path === 'annual-deadline') {
        const { holiday } = await store.dispatch('app/setCommonCode', 'all')
        await store.dispatch('app/setHolidayList', holiday)
      }
      next()
      NProgress.done()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
