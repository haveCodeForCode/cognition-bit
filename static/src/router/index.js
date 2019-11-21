import Vue from 'vue'
import Router from 'vue-router'
import Login from '../view/login'
import Home from '../view/home'

Vue.use(Router);

export default new Router({
  mode:'history',
  routes: [
    {
      path: '/home',
      component: Home,
      // name: '知域网',
      meta:{
        title: '知域网',
        requireAuth:true
      }
    },
    {
      path: '/login',
      component: Login,
      name: '登录',
    }
  ]
})

