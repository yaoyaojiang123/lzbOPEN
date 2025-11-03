<template>
    <NavMain></NavMain>
    <div class="box">
    <el-form ref="addClassForm" :model="addClassForm" :rules="rules" label-width="30%" style="margin-left: 10%;margin-right: 10%;">
    <h3>新增班级</h3>
    <el-row>
        <el-col :span="8">
            <el-form-item label="班级名称" prop="className">
                <el-input v-model="addClassForm.className" placeholder="请输入班级名称"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="年级" prop="grade">
                <el-select v-model="addClassForm.grade" placeholder="请选择年级">
                    <el-option v-for="(item,index) in grades" :key="index" :label="item" :value="item"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="班主任" prop="headTeacher">
                <el-input v-model="addClassForm.headTeacher" placeholder="请输入班主任姓名"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="教室位置" prop="classroom">
                <el-input v-model="addClassForm.classroom" placeholder="请输入教室位置"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="入学年份" prop="enrollmentYear">
                <el-input v-model="addClassForm.enrollmentYear" placeholder="请输入入学年份"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="班级人数上限" prop="maxStudentCount">
                <el-input v-model="addClassForm.maxStudentCount" type="number" placeholder="请输入班级人数上限"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="24">
            <el-form-item label="备注" prop="remark">
                <el-input v-model="addClassForm.remark" type="textarea" placeholder="请输入备注信息"></el-input>
            </el-form-item>
        </el-col>
    </el-row>
    <el-form-item>
        <el-button type="primary" @click="submitForm('addClassForm')">提交</el-button>
        <el-button @click="resetForm('addClassForm')">重置</el-button>
    </el-form-item>
    </el-form>
    </div>
</template>
<script>
import NavMain from '@/components/NavMain.vue'
export default {
  name: 'AddClass',
  components: {
    NavMain
  },
  data() {
    return {
      addClassForm: {
        className: '',
        grade: '',
        headTeacher: '',
        classroom: '',
        enrollmentYear: '',
        maxStudentCount: 50,
        remark: ''
      },
      grades: ['一年级', '二年级', '三年级', '四年级', '五年级', '六年级'],
      rules: {
        className: [
          { required: true, message: '请输入班级名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        grade: [
          { required: true, message: '请选择年级', trigger: 'change' }
        ],
        headTeacher: [
          { required: true, message: '请输入班主任姓名', trigger: 'blur' }
        ],
        enrollmentYear: [
          { required: true, message: '请输入入学年份', trigger: 'blur' },
          { pattern: /^\d{4}$/, message: '请输入有效的年份（4位数字）', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //调用后端接口保存班级信息
          this.$axios.post('/api/classInfo/add', this.addClassForm)
            .then(response => {
              if (response.data.code === 0) {
                this.$message({
                  type: 'success',
                  message: '班级添加成功'
                })
                //提交成功后跳转到班级管理页面
                this.$router.push('/classInfo')
              } else {
                this.$message.error('班级添加失败: ' + response.data.msg)
              }
            })
            .catch(error => {
              this.$message.error('请求失败: ' + error.message)
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