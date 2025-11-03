<template>
 <h2>学生管理系统</h2>
<el-menu
  :default-active="activeIndex"
  class="el-menu-demo"
  mode="horizontal"
  @select="handleSelect"
  background-color="#545c64"
  text-color="#fff"
  active-text-color="#ffd04b">
  <el-submenu index="1">
    <template #title>班级管理</template>
    <el-menu-item index="/classInfo">管理班级</el-menu-item>
    <el-menu-item index="/addClass">新增班级</el-menu-item>
  </el-submenu>
  <el-submenu index="2">
    <template #title>学生管理</template>
    <el-menu-item index="/student">管理学生</el-menu-item>
    <el-menu-item index="/addStudent">新增学生</el-menu-item>
  </el-submenu>
  <el-submenu index="3">
    <template #title>课程管理</template>
    <el-menu-item index="/course">管理课程</el-menu-item>
    <el-menu-item index="/addCourse">新增课程</el-menu-item>
  </el-submenu>
  <el-submenu index="4">
    <template #title>成绩管理</template>
    <el-menu-item index="/score">管理成绩</el-menu-item>
    <el-menu-item index="/addScore">录入成绩</el-menu-item>
  </el-submenu>

</el-menu>
</template>
<script>
  export default {
    name: 'NavMain',
    data() {
      return {
        //班级信息中心点亮
        activeIndex: '1'
      }
    },
    methods: {
        handleSelect(key, keyPath) {
            // 使用路由跳转代替嵌套的router-link
            if (key && key.startsWith('/')) {
                this.$router.push(key);
            }
            console.log(key, keyPath);
        }
    }
  }
</script>
<style>
   .el-menu-item {
       text-decoration: none;
       color: #fff;
   }
   .el-menu-item:hover {
       background-color: rgba(255,255,255,0.1);
   }
   .el-menu-item.is-active {
       color: #ffd04b;
   }
   .el-menu--horizontal {
       line-height: 60px;
   }
</style>
