import Cookies from 'js-cookie'

//获取token
export function getToken() {
  return Cookies.get('Admin-Token')
}
//存放token
export function setToken(token) {
  return Cookies.set('Admin-Token', token)
}

//移除token
export function removeToken() {
  return Cookies.remove('Admin-Token')
}
