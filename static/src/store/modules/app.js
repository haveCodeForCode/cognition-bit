import Cookies from 'js-cookie'

//app,store缓存值,cookies中获取
const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  device: 'desktop',
  size: Cookies.get('size') || 'medium'
};

const mutations = {
  //切换工具栏
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened;
    state.sidebar.withoutAnimation = false;
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  //关闭工具栏
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0);
    state.sidebar.opened = false;
    state.sidebar.withoutAnimation = withoutAnimation
  },
  //切换设备
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  //设置大小
  SET_SIZE: (state, size) => {
    state.size = size;
    Cookies.set('size', size)
  }
};

const actions = {
  toggleSideBar({commit}) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({commit}, {withoutAnimation}) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({commit}, device) {
    commit('TOGGLE_DEVICE', device)
  },
  setSize({commit}, size) {
    commit('SET_SIZE', size)
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
