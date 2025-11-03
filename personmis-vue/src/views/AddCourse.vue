<template>
    <NavMain></NavMain>
    <div class="box">
    <el-form ref="addCourseForm" :model="addCourseForm" :rules="rules" label-width="30%" style="margin-left: 10%;margin-right: 10%;">
    <h3>新增课程</h3>
    <el-row>
        <el-col :span="8">
            <el-form-item label="课程名称" prop="courseName">
                <el-input v-model="addCourseForm.courseName" placeholder="请输入课程名称"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="授课教师" prop="teacherName">
                <el-input v-model="addCourseForm.teacherName" placeholder="请输入授课教师"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="学分" prop="credit">
                <el-input v-model.number="addCourseForm.credit" type="number" placeholder="请输入学分" @change="handleCreditChange"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="总课时" prop="totalHours">
                <el-input v-model.number="addCourseForm.totalHours" type="number" placeholder="请输入总课时" @change="handleHoursChange"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="课程类型" prop="courseType">
                <el-select v-model="addCourseForm.courseType" placeholder="请选择课程类型">
                    <el-option label="必修课" value="必修课"></el-option>
                    <el-option label="选修课" value="选修课"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="适用年级" prop="suitableGrade">
                <el-select v-model="addCourseForm.suitableGrade" placeholder="请选择适用年级">
                    <el-option v-for="(item,index) in grades" :key="index" :label="item" :value="item"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="24">
            <el-form-item label="课程描述" prop="description">
                <el-input v-model="addCourseForm.description" type="textarea" placeholder="请输入课程描述"></el-input>
            </el-form-item>
        </el-col>
    </el-row>
    <el-form-item>
        <el-button type="primary" @click="submitForm('addCourseForm')">提交</el-button>
        <el-button @click="resetForm('addCourseForm')">重置</el-button>
    </el-form-item>
    </el-form>
    </div>
</template>
<script>
import NavMain from '@/components/NavMain.vue'
export default {
  name: 'AddCourse',
  components: {
    NavMain
  },
  data() {
    
    return {
      addCourseForm: {
        courseName: '',
        teacherName: '',
        credit: null,
        totalHours: null,
        courseType: '必修课',
        suitableGrade: '',
        description: ''
      },
      grades: ['一年级', '二年级', '三年级', '四年级', '五年级', '六年级'],
      rules: {
        courseName: [
          { required: true, message: '请输入课程名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        teacherName: [
          { required: true, message: '请输入授课教师', trigger: 'blur' }
        ],
        credit: [
          { required: true, message: '请输入学分', trigger: 'blur' },
          { type: 'number', min: 0.5, max: 10, message: '学分应在0.5到10之间', trigger: 'blur' }
        ],
        totalHours: [
          { required: true, message: '请输入总课时', trigger: 'blur' },
          { type: 'number', min: 1, max: 200, message: '总课时应在1到200之间', trigger: 'blur' }
        ],
        suitableGrade: [
          { required: true, message: '请选择适用年级', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    handleCreditChange() {
      // 确保值为数字类型
      if (this.addCourseForm.credit !== null && this.addCourseForm.credit !== undefined) {
        this.addCourseForm.credit = Number(this.addCourseForm.credit);
      }
    },
    handleHoursChange() {
      // 确保值为数字类型且为整数
      if (this.addCourseForm.totalHours !== null && this.addCourseForm.totalHours !== undefined) {
        this.addCourseForm.totalHours = Math.floor(Number(this.addCourseForm.totalHours));
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //调用后端接口保存课程信息
          this.$axios.post('/api/course/add', this.addCourseForm)
            .then(response => {
              if (response.data && response.data.success) {
                this.$message({
                  type: 'success',
                  message: '课程添加成功'
                })
                //提交成功后跳转到课程管理页面
                this.$router.push('/course')
              } else {
                this.$message({
                  type: 'error',
                  message: response.data ? response.data.message : '课程添加失败'
                })
              }
            })
            .catch(error => {
              console.error('添加课程失败:', error)
              this.$message({
                type: 'error',
                message: '网络错误，请稍后重试'
              })
            })
        } else {
          this.$message({
            type: 'error',
            message: '表单验证失败，请检查输入'
          })
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
<style scoped>
.box {
  margin-top: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 5px;
}
h3 {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
}
</style>