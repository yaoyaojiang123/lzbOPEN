<template>
    <NavMain></NavMain>
    <div class="box">
    <el-form ref="addStudentForm" :model="addStudentForm" :rules="rules" label-width="30%" style="margin-left: 10%;margin-right: 10%;">
    <h3>新增学生</h3>
    <el-row>
        <el-col :span="8">
            <el-form-item label="学生姓名" prop="studentName">
                <el-input v-model="addStudentForm.studentName" placeholder="请输入学生姓名"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="性别" prop="sex">
                <el-radio v-model="addStudentForm.sex" label="男">男</el-radio>
                <el-radio v-model="addStudentForm.sex" label="女">女</el-radio>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="出生日期" prop="birthDate">
                <el-date-picker v-model="addStudentForm.birthDate" type="date" placeholder="选择出生日期" style="width:100%"></el-date-picker>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="addStudentForm.idCard" placeholder="请输入身份证号"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="班级" prop="classId">
                <el-select v-model="addStudentForm.classId" placeholder="请选择班级">
                    <el-option v-for="(item,index) in classes" :key="index" :label="item.className" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="学号" prop="studentId">
                <el-input v-model="addStudentForm.studentId" placeholder="请输入学号"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="联系电话" prop="phone">
                <el-input v-model="addStudentForm.phone" placeholder="请输入联系电话"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="家庭住址" prop="address">
                <el-input v-model="addStudentForm.address" placeholder="请输入家庭住址"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="家长姓名" prop="parentName">
                <el-input v-model="addStudentForm.parentName" placeholder="请输入家长姓名"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="家长联系电话" prop="parentPhone">
                <el-input v-model="addStudentForm.parentPhone" placeholder="请输入家长联系电话"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="24">
            <el-form-item label="备注" prop="remark">
                <el-input v-model="addStudentForm.remark" type="textarea" placeholder="请输入备注信息"></el-input>
            </el-form-item>
        </el-col>
    </el-row>
    <el-form-item>
        <el-button type="primary" @click="submitForm('addStudentForm')">提交</el-button>
        <el-button @click="resetForm('addStudentForm')">重置</el-button>
    </el-form-item>
    </el-form>
    </div>
</template>
<script>
import NavMain from '@/components/NavMain.vue'
export default {
  name: 'AddStudent',
  components: {
    NavMain
  },
  data() {
    return {
      addStudentForm: {
        studentName: '',
        sex: '',
        birthDate: '',
        idCard: '',
        classId: '',
        studentId: '',
        phone: '',
        address: '',
        parentName: '',
        parentPhone: '',
        remark: ''
      },
      classes: [], //从后端API获取班级数据
      rules: {
        studentName: [
          { required: true, message: '请输入学生姓名', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        birthDate: [
          { required: true, message: '请选择出生日期', trigger: 'change' }
        ],
        classId: [
          { required: true, message: '请选择班级', trigger: 'change' }
        ],
        studentId: [
          { required: true, message: '请输入学号', trigger: 'blur' }
        ],
        parentName: [
          { required: true, message: '请输入家长姓名', trigger: 'blur' }
        ],
        parentPhone: [
          { required: true, message: '请输入家长联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号码', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    //加载班级数据
    this.loadClasses();
  },
  methods: {
    //加载班级列表
    loadClasses() {
      this.$axios.get('/api/classInfo/getAll')
        .then(response => {
          if (response.data.code === 0) {
            this.classes = response.data.data;
          } else {
            this.$message.error('获取班级列表失败: ' + response.data.msg);
          }
        })
        .catch(error => {
          this.$message.error('请求失败: ' + error.message);
        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //调用后端API添加学生
          this.$axios.post('/api/student/add', this.addStudentForm)
            .then(response => {
              if (response.data.code === 0) {
                this.$message({
                  type: 'success',
                  message: '学生添加成功'
                })
                //提交成功后跳转到学生管理页面
                this.$router.push('/student')
              } else {
                this.$message.error('添加失败: ' + response.data.msg);
              }
            })
            .catch(error => {
              this.$message.error('请求失败: ' + error.message);
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