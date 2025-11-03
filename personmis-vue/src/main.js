import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
//创建vue实例
const vapp = createApp(App)
//设置反向代理，前端请求默认发送到 http://localhost:8443/studentmis
const axios = require('axios')//使用 axios 来完成 ajax 请求。
//全局注册，之后可在其他组件中通过 this.$axios 发送数据
axios.defaults.baseURL = 'http://localhost:8443/studentmis'
//axios挂载到vue实例
vapp.config.globalProperties.$axios = axios
//阻止显示生产模式的消息
vapp.config.productionTip = false
installElementPlus(vapp)
vapp.use(store).use(router).mount('#app')
//验证是否登录
router.beforeEach((to,from,next)=>{
    // 如果是登录页面，不需要验证，直接通过
    if(to.path === '/login') {
      next();
      return;
    }
    
    // 对于其他页面，检查是否需要验证权限
    if(!to.matched.some(m=>m.meta.auth)){
      // 对需要验证的路由进行检查
      if (store.state.isLogin == '0') {
        alert("您没有登录，无权访问学生管理系统！")
        // 未登录则跳转到登陆界面，query:{ redirect: to.fullPath}表示把当前路由信息传递过去方便登录后跳转回来；
        next({
          path: '/login',
          query: {redirect: to.fullPath}
        })
      } else { // 已经登陆
        next() //正常跳转到设置好的页面
      }
    }else{
      next()
    }
  }
)

