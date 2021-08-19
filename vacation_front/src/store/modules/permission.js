import path from 'path'
/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */

function onlyOneShowingChild(children = [], parent) {
  let onlyOneChild = null
  const showingChildren = children.filter(item => !item.hidden)
  if (showingChildren.length === 1) {
    onlyOneChild = showingChildren[0]
    onlyOneChild.path = path.resolve(parent.path, onlyOneChild.path)
    return onlyOneChild
  }
  if (showingChildren.length === 0) {
    onlyOneChild = { ...parent, path: '', noShowingChildren: true }
    return onlyOneChild
  }
  return false
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */

export function filterAsyncRoutes(routes, roles, basePath = '/') {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (tmp.hidden) {
      res.push(tmp)
    }
    let path = basePath + '/' + tmp.path
    if (basePath === '/') {
      path = onlyOneShowingChild(tmp.children, tmp).path
    }
    roles.some((role, i, arr) => {
      const role_path = role.path
      if (path !== undefined) {
        if (role_path === path) {
          res.push(tmp)
          arr.splice(i, 1)
          return (role_path === path)
        }
      } else {
        if (tmp.path === role_path) {
          tmp.children = filterAsyncRoutes(tmp.children, role.children, tmp.path)
          res.push(tmp)
        }
      }
    })
  })
  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {

}

const actions = {

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
