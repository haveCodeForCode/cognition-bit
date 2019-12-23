//******store vuex 全局状态变量管理******

//引入vue
import Vue from 'vue';
import Vuex from 'vuex';

//引用模块
import app from './modules/app'
import user from './modules/user'

//应用vuex
Vue.use(Vuex);

export default new Vuex.Store({

  modules: {
    //系统
    app,
    //用户
    user
  }

  //******模板***
  // state: {
  // ***声明存放值**
  //   user:xxx,
  //   Menu:xxx
  // },
  // mutations: {
  //  **全局同步函数
  //   // 修改token，并将token存入localStorage
  //  [setState](state, value) {
  // 　　　　　　state.xxx = value
  // 　}
  // }
});
