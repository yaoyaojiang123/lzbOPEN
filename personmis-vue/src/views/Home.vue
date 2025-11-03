<template>
  <div class="home">
    <NavMain></NavMain>
    <div class="container">
      <h1>学生管理系统</h1>
      <p>欢迎使用学生管理系统，请通过左侧菜单进行操作</p>
      <div class="stats">
        <div class="stat-item">
          <div class="stat-icon bg-purple"></div>
          <div class="stat-number">{{ classCount }}</div>
          <div class="stat-label">班级总数</div>
        </div>
        <div class="stat-item">
          <div class="stat-icon bg-pink"></div>
          <div class="stat-number">{{ studentCount }}</div>
          <div class="stat-label">学生总数</div>
        </div>
        <div class="stat-item">
          <div class="stat-icon bg-blue"></div>
          <div class="stat-number">{{ courseCount }}</div>
          <div class="stat-label">课程总数</div>
        </div>
        <div class="stat-item">
          <div class="stat-icon bg-green"></div>
          <div class="stat-number">{{ scoreCount }}</div>
          <div class="stat-label">成绩记录</div>
        </div>
      </div>
      <div class="description">
        <h3>系统功能介绍</h3>
        <p>管理班级信息，包括班级基本信息的增删改查；管理学生信息，包括学生档案的增删改查；管理课程信息，包括课程设置和教师安排；录入管理学生成绩，支持成绩统计分析和各类统计报表，为教学管理提供数据支持。</p>
      </div>
    </div>
  </div>
</template>

<script>
import NavMain from '@/components/NavMain.vue'

export default {
  name: 'Home',
  components: {
    NavMain
  },
  data() {
    return {
      classCount: 3,
      studentCount: 127,
      courseCount: 4,
      scoreCount: 508
    }
  }
}
</script>

<style scoped>
.home {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
}
.container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
.container h1 {
  color: #2c3e50;
  margin-bottom: 10px;
}
.container p {
  color: #666;
  margin-bottom: 30px;
}
.stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 40px;
  flex-wrap: wrap;
}
.stat-item {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  flex: 0 0 calc(25% - 20px);
  text-align: center;
  margin-bottom: 20px;
  transition: transform 0.3s;
}
.stat-item:hover {
  transform: translateY(-5px);
}
.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 auto 15px;
}
.bg-purple {
  background-color: #9c27b0;
}
.bg-pink {
  background-color: #e91e63;
}
.bg-blue {
  background-color: #2196f3;
}
.bg-green {
  background-color: #4caf50;
}
.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 5px;
}
.stat-label {
  color: #666;
  font-size: 14px;
}
.description {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 25px;
}
.description h3 {
  color: #2c3e50;
  margin-bottom: 15px;
}
.description p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 0;
}

@media (max-width: 768px) {
  .stat-item {
    flex: 0 0 calc(50% - 20px);
  }
}

@media (max-width: 480px) {
  .stat-item {
    flex: 0 0 100%;
  }
}
</style>
