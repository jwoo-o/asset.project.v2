import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const constantRoutes = [

  {
    path: '/',
    component: () => import('@/views/login/user-login'),
    name: 'user-login',
    meta: { title: 'Login', affix: true, auth: 'ROLE_USER' }

  },
  {
    path: '/mng',
    component: () => import('@/views/login/admin-login'),
    name: 'admin-login',
    meta: { title: 'Login', affix: true, auth: 'ROLE_ADMIN' }

  },
  {
    path: '/approve-mail',
    component: () => import('@/views/user/approver/approve-mail'),
    name: 'approve-mail',
    meta: { title: '휴가승인' }
  },
  {
    path: '/annual-deadline',
    component: () => import('@/views/deadline/deadline-check'),
    name: 'annual-deadline',
    meta: { title: '휴가일정' }
  },
  {
    path: '/popup-board-notice',
    component: () => import('@/components/Popup/popup'),
    name: 'popup-board-notice',
    meta: { title: '공지사항popup', auth: 'ROLE_USER' }

  },
  {
    path: '/asset-info',
    component: () => import('@/views/asset/asset-view'),
    name: 'asset-info',
    meta: { title: '자산현황' }

  },
  {
    path: '/user',
    component: () => import('@/views/user'),
    name: 'user',
    redirect: { name: 'user-home' },
    children: [
      {
        path: '/user-home',
        component: () => import('@/views/user/user-home'),
        name: 'user-home',
        meta: { title: '휴가현황', parent: 'user-home', auth: 'ROLE_USER', hidden: true }

      },
      {
        path: '/vac-approval',
        component: () => import('@/views/user/vacation/vac-approval'),
        name: 'vac-approval',
        meta: { title: '휴가상신', parent: 'user-home', auth: 'ROLE_USER', hidden: true }

      },
      {
        path: '/vac-check',
        component: () => import('@/views/user/vacation/vac-check'),
        name: 'vac-check',
        meta: { title: '휴가상신', parent: 'user-home', auth: 'ROLE_USER', hidden: true }

      },
      {
        path: '/vac-edit',
        component: () => import('@/views/user/vacation/vac-edit'),
        name: 'vac-edit',
        meta: { title: '휴가수정', parent: 'user-home', auth: 'ROLE_USER', hidden: true }

      },
      {
        path: '/approve-list',
        component: () => import('@/views/user/approver/approve-list'),
        name: 'approve-list',
        meta: { title: '휴가승인현황', auth: 'ROLE_USER' }

      },
      {
        path: '/approve-check',
        component: () => import('@/views/user/approver/approve-check'),
        name: 'approve-check',
        meta: { title: '휴가결재', parent: 'approve-list', auth: 'ROLE_USER', hidden: true }

      },
      {
        path: '/user-vac-list',
        component: () => import('@/views/user/vacationStatus/user-vac-list'),
        name: 'user-vac-list',
        meta: { title: '휴가현황', auth: 'ROLE_USER' }
      },
      {
        path: '/user-vac-detail',
        component: () => import('@/views/user/vacationStatus/user-vac-detail'),
        name: 'user-vac-detail',
        meta: { title: '휴가현황리스트', parent: 'user-vac-list', auth: 'ROLE_USER', hidden: true }
      },
      {
        path: '/user-vac-check',
        component: () => import('@/views/user/vacationStatus/user-vac-check'),
        name: 'user-vac-check',
        meta: { title: '휴가현황디테일', parent: 'user-vac-list', auth: 'ROLE_USER', hidden: true }
      },
      {
        path: '/user-board-notice',
        component: () => import('@/views/board/user-board-notice'),
        name: 'user-board-notice',
        meta: { title: '공지사항', auth: 'ROLE_USER' }

      },
      {
        path: '/user-seat-index',
        component: () => import('@/views/seat/seat-index'),
        name: 'user-seat-index',
        meta: { title: '배치도', auth: 'ROLE_USER' }
      },
      {
        path: '/user-info',
        component: () => import('@/views/user/info/index'),
        name: 'user-info',
        meta: { title: '사용자 정보', auth: 'ROLE_USER', hidden: true }
      },
      {
        path: '/user-asset-dashboard',
        component: () => import('@/views/asset/asset-dashboard'),
        name: 'user-asset-dashboard',
        meta: { title: '자산현황', auth: 'ROLE_USER' }
      },
      {
        path: '/user-asset-list',
        component: () => import('@/views/asset/asset-list'),
        name: 'user-asset-list',
        meta: { title: '자산정보', auth: 'ROLE_USER' }
      },
      {
        path: '/user-asset-edit',
        component: () => import('@/views/asset/asset-edit'),
        name: 'user-asset-edit',
        meta: { title: '자산등록', parent: 'asset-list', auth: 'ROLE_USER', hidden: true }
      }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/views/admin'),
    name: 'admin',
    redirect: { name: 'admin-vacation-list' },
    children: [
      {
        path: '/admin-approve-line',
        component: () => import('@/views/admin/line/admin-approve-line'),
        name: 'admin-approve-line',
        meta: { title: '결재라인', auth: 'ROLE_ADMIN' }
      },
      {
        path: '/admin-line-create',
        component: () => import('@/views/admin/line/admin-line-create'),
        name: 'admin-line-create',
        meta: { title: '결재라인지정', parent: 'admin-approve-line', auth: 'ROLE_ADMIN', hidden: true }
      },
      {
        path: '/common',
        component: () => import('@/views/admin/common/common'),
        name: 'common',
        meta: { title: '공통코드', auth: 'ROLE_ADMIN' }

      },
      {
        path: '/common-edit',
        component: () => import('@/views/admin/common/common-edit'),
        name: 'common-edit',
        meta: { title: '공통코드관리', parent: 'common', auth: 'ROLE_ADMIN', hidden: true }

      },
      {
        path: '/admin-vacation-list',
        component: () => import('@/views/admin/vacation/admin-vacation-list'),
        name: 'admin-vacation-list',
        meta: { title: '휴가현황', auth: 'ROLE_ADMIN' }
      },
      {
        path: '/admin-vacation-detail',
        component: () => import('@/views/admin/vacation/admin-vacation-detail'),
        name: 'admin-vacation-detail',
        meta: { title: '휴가현황조회', parent: 'admin-vacation-list', auth: 'ROLE_ADMIN', hidden: true }
      },
      {
        path: '/admin-vacation-check',
        component: () => import('@/views/admin/vacation/admin-vacation-check'),
        name: 'admin-vacation-check',
        meta: { title: '휴가상세조회', parent: 'admin-vacation-list', auth: 'ROLE_ADMIN', hidden: true }
      },
      {
        path: '/admin-annual-deadline',
        component: () => import('@/views/deadline/deadline-check'),
        name: 'admin-annual-deadline',
        meta: { title: '휴가일정', parent: 'admin-vacation-list', auth: 'ROLE_ADMIN', hidden: true }
      },
      {
        path: '/user-edit',
        component: () => import('@/views/admin/user/user-edit'),
        name: 'user-edit',
        meta: { title: '사원관리', parent: 'user-list', auth: 'ROLE_ADMIN', hidden: true }
      },
      {
        path: '/user-list',
        component: () => import('@/views/admin/user/user-list'),
        name: 'user-list',
        meta: { title: '사원관리', auth: 'ROLE_ADMIN' }
      },
      {
        path: '/user-cert',
        component: () => import('@/views/admin/user/cert-list'),
        name: 'user-cert',
        meta: { title: '증명서관리', auth: 'ROLE_ADMIN' }
      },
      {
        path: '/organization-index',
        component: () => import('@/views/admin/organization/index'),
        name: 'organization-index',
        meta: {
          title: '부서관리',
          auth: 'ROLE_ADMIN'
        }
      },
      {
        path: '/board-notice',
        component: () => import('@/views/board/notice'),
        name: 'board-notice',
        meta: { title: '공지사항', auth: 'ROLE_ADMIN' }
      },
      {
        path: '/board-edit',
        component: () => import('@/views/board/edit'),
        name: 'board-edit',
        meta: { title: '공지사항등록', parent: 'board-notice', auth: 'ROLE_ADMIN', hidden: true }
      },
      {
        path: '/admin-seat-index',
        component: () => import('@/views/seat/seat-index'),
        name: 'admin-seat-index',
        meta: { title: '배치도', auth: 'ROLE_ADMIN' }
      },
      {
        path: '/admin-asset-dashboard',
        component: () => import('@/views/asset/asset-dashboard'),
        name: 'admin-asset-dashboard',
        meta: { title: '자산현황', auth: 'ROLE_ADMIN' }
      },
      {
        path: '/admin-asset-list',
        component: () => import('@/views/asset/asset-list'),
        name: 'admin-asset-list',
        meta: { title: '자산정보', auth: 'ROLE_ADMIN' }
      },
      {
        path: '/admin-asset-edit',
        component: () => import('@/views/asset/asset-edit'),
        name: 'admin-asset-edit',
        meta: { title: '자산등록', parent: 'asset-list', auth: 'ROLE_ADMIN', hidden: true }
      }
    ]
  },

  /** when your routing map is too long, you can split it into small modules **/
  /* componentsRouter,
  chartsRouter,
  nestedRouter,*/
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes,
  mode: 'history'
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
