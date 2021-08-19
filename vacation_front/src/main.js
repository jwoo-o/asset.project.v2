import Vue from 'vue'
import App from './App.vue'
import Cookies from 'js-cookie'
import vueMoment from 'vue-moment'
import store from './store'
import router from './router'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import '@/styles/index.scss' // global css

import './icons' // icon
import './permission'
import './utils/error-log' // error log

import enLang from 'element-ui/lib/locale/lang/ko'

Vue.config.productionTip = false

Vue.use(vueMoment)

Vue.use(ElementUI, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  locale: enLang
})

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
