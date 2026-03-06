import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register']

// 前台页面白名单（允许未登录访问）
const frontWhiteList = ['/front']

// 判断是否为前台页面
function isFrontPage(path) {
  return path.startsWith('/front')
}

// 判断是否为管理员或商户（非观众角色，跳转管理端）
function isAdminOrMerchants(roles) {
  if (!roles || roles.length === 0) return false
  return roles.some(role => {
    const r = (role || '').toLowerCase()
    return r === 'admin' || r === 'merchants' ||
      r.includes('admin') || r.includes('merchants')
  })
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      if (store.getters.roles.length === 0) {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(res => {
          isRelogin.show = false
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表

            // 如果是首次登录跳转到根路径，根据角色决定去向
            if (to.path === '/' || to.path === '/index') {
              const roles = (res && res.roles) || store.getters.roles || []
              if (isAdminOrMerchants(roles)) {
                // admin、merchants 跳转管理端首页
                next({ ...to, replace: true })
              } else {
                // 观众(common)跳转用户前端页面
                next({ path: '/front', replace: true })
              }
            } else {
              next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
            }
          })
        }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else if (isFrontPage(to.path)) {
      // 前台页面允许未登录访问
      next()
    } else {
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
