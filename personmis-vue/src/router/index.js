import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import ClassInfo from '../views/ClassInfo.vue'
import AddClass from '../views/AddClass.vue'
import Student from '../views/Student.vue'
import AddStudent from '../views/AddStudent.vue'
import Course from '../views/Course.vue'
import AddCourse from '../views/AddCourse.vue'
import Score from '../views/Score.vue'
import AddScore from '../views/AddScore.vue'

const routes = [
  //打开程序直接跳转到登录页面
  {
    path: '/',
    redirect: '/login',
    meta: { auth: false } // 登录不需要验证权限
  },
  {
    path: '/login',
    component: Login,
    meta: { auth: false }
  },
  //班级管理
  {
    path: '/classInfo',
    component: ClassInfo
  },
  {
    path: '/addClass',
    component: AddClass
  },
  //学生管理
  {
    path: '/student',
    component: Student
  },
  {
    path: '/addStudent',
    component: AddStudent
  },
  //课程管理
  {
    path: '/course',
    component: Course
  },
  {
    path: '/addCourse',
    component: AddCourse
  },
  //成绩管理
  {
    path: '/score',
    component: Score
  },
  {
    path: '/addScore',
    component: AddScore
  },
]
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
