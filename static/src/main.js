// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.min.css'

Vue.prototype.$axios = axios;
Vue.prototype.api = 'http://127.0.0.1:8090';

Vue.config.productionTip = false;
Vue.use(ElementUI);


//异步请求前判断请求的连接是否需要token
router.beforeEach((to, from, next) => {
  console.log(localStorage);
  let token = localStorage.getItem('Authorization');
  if (to.path === '/') {
    console.log("我是浏览器本地缓存的token: "+token);
    if (!token || token === 'null' || token === '') {
      next('/login');
    } else {
      next('/home');
    }
  } else {
    if (!token || token === 'null' || token === '') {
      next('/login');
    }else {
      next();
    }
  }
});

//异步请求前在header里加入token
axios.interceptors.request.use(
  config => {
    console.log("异步请求存token");
    if (config.url === '/login' || config.url === '/') {  //如果是登录和注册操作，则不需要携带header里面的token
    } else {
      if (localStorage.getItem('Authorization')) {
        config.headers.Authorizatior = localStorage.getItem('Authorization');
      }
    }
    return config;
  },
  error => {
    console.log("异步存token错误");
    return Promise.reject(error);
  });

//异步请求后，判断token是否过期
axios.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (error.response) {
      if (error.response.status ==='401') {
        localStorage.removeItem('Authorization');
        this.$router.push('/');
      }
    }
  });

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  render: h => h(App),
  template: '<App/>'
});
