<template>
    <NavMain></NavMain>
<div class="box1">
<br>
<el-form ref="addForm" :model="addForm" :rules="rules" style="width:80%;"  label-width="30%" >
  <el-row>
    <el-col :span="8">
      <el-form-item label="学生"  prop="studentId">
        <el-select v-model="addForm.studentId" placeholder="请选择学生" @change="onStudentChange">
          <el-option v-for="(item,index) in students" :key="index" :label="`${item.studentName}(${item.studentNo})`" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="班级" prop="classId">
        <el-select v-model="addForm.classId" placeholder="请选择班级" @change="onClassChange">
          <el-option v-for="(item,index) in classes" :key="index" :label="item.className" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="课程"  prop="courseId">
        <el-select v-model="addForm.courseId" placeholder="请选择课程" @change="checkScoreDuplicate">
          <el-option v-for="(item,index) in courses" :key="index" :label="`${item.courseName}(${item.teacherName})`" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="8">
      <el-form-item label="成绩"  prop="score">
        <el-input v-model="addForm.score" type="number" placeholder="请输入成绩（0-100）" min="0" max="100" step="0.01"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="学期" prop="semester">
        <el-select v-model="addForm.semester" placeholder="请选择学期">
          <el-option label="第一学期" value="第一学期"></el-option>
          <el-option label="第二学期" value="第二学期"></el-option>
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="考试日期" prop="examDate">
        <el-date-picker v-model="addForm.examDate" type="date" placeholder="选择考试日期" style="width:100%"></el-date-picker>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="8">
      <el-form-item label="考试类型" prop="examType">
        <el-select v-model="addForm.examType" placeholder="请选择考试类型">
          <el-option label="期中考试" value="期中考试"></el-option>
          <el-option label="期末考试" value="期末考试"></el-option>
          <el-option label="平时测验" value="平时测验"></el-option>
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="16">
      <el-form-item label="备注" prop="remark">
        <el-input v-model="addForm.remark" type="textarea" placeholder="请输入备注信息"></el-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-form-item>
    <el-button type="primary" @click="submitForm('addForm')">提交</el-button>
    <el-button @click="resetForm('addForm')">重置</el-button>
    <el-button @click="back">返回</el-button>
  </el-form-item>
</el-form>
</div>
</template>
<script>
  import NavMain from '@/components/NavMain.vue'
  export default {
    name: 'AddScore',
    components: {
      NavMain
    },
    data () {
      return {
        //添加表单
        addForm: {},
        //班级选项
        classes: [],
        //课程选项
        courses: [],
        //学生选项
        allStudents: [],
        //根据班级筛选后的学生
        students: [],
        //加载状态
        loading: false,
        //检查重复状态
        checkingDuplicate: false,
        //添加表单验证规则
        rules: {
          studentId: [{required: true, message: '请选择学生', trigger: 'change'}],
          classId: [{required: true, message: '请选择班级', trigger: 'change'}],
          courseId: [{required: true, message: '请选择课程', trigger: 'change'}],
          score: [{required: true, message: '请输入成绩', trigger: 'blur'},
                 {type: 'number', min: 0, max: 100, message: '成绩应在0到100之间', trigger: 'blur'},
                 {validator: this.validateScore, trigger: 'blur'}],
          semester: [{required: true, message: '请选择学期', trigger: 'change'}],
          examDate: [{required: true, message: '请选择考试日期', trigger: 'change'}],
          examType: [{required: true, message: '请选择考试类型', trigger: 'change'}]
        }
      }
    },
    created () {
      //加载必要的数据
      this.loadClasses()
      this.loadCourses()
      this.loadStudents()
    },
    methods: {
      //当班级变化时，筛选该班级的学生
      onClassChange(value) {
        if (value) {
          this.students = this.allStudents.filter(student => student.classId === value)
          //重置学生选择
          this.addForm.studentId = ''
        } else {
          this.students = this.allStudents
        }
      },
      
      //当学生变化时，自动填充班级信息
      onStudentChange(value) {
        if (value) {
          const student = this.allStudents.find(s => s.id === value)
          if (student && student.classId) {
            this.addForm.classId = student.classId
          }
        }
        //检查是否重复
        this.checkScoreDuplicate()
      },
      
      //检查成绩是否重复
      checkScoreDuplicate() {
        if (this.addForm.studentId && this.addForm.courseId) {
          this.checkingDuplicate = true
          this.$axios.get(`/api/score/checkExists/${this.addForm.studentId}/${this.addForm.courseId}`)
            .then(response => {
              if (response.data && response.data.exists) {
                this.$message.warning('该学生已有该课程的成绩记录，请检查！')
              }
            })
            .catch(error => {
              console.error('检查成绩重复失败:', error)
            })
            .finally(() => {
              this.checkingDuplicate = false
            })
        }
      },
      
      //成绩验证器
      validateScore(rule, value, callback) {
        if (value === '' || value === null || value === undefined) {
          callback()
          return
        }
        
        //检查成绩格式
        if (!/^\d+(\.\d{1,2})?$/.test(value.toString())) {
          callback(new Error('成绩格式不正确，最多保留两位小数'))
          return
        }
        
        //检查成绩范围
        const scoreNum = Number(value)
        if (scoreNum < 0 || scoreNum > 100) {
          callback(new Error('成绩应在0到100之间'))
          return
        }
        
        callback()
      },
      
      //加载班级数据
      loadClasses() {
        this.$axios.get('/api/classInfo/getAll')
          .then(response => {
            if (response.data && response.data.success) {
              this.classes = response.data.data || []
            }
          })
          .catch(error => {
            console.error('加载班级数据失败:', error)
            this.$message({
              type: 'error',
              message: '加载班级数据失败'
            })
          })
      },
      
      //加载课程数据
    loadCourses() {
      this.$axios.post('/api/course/list', {})
        .then(response => {
          if (response.data && response.data.success) {
            this.courses = response.data.data.records || []
          }
        })
        .catch(error => {
          console.error('加载课程数据失败:', error)
          this.$message({
            type: 'error',
            message: '加载课程数据失败'
          })
        })
      },
      
      //加载学生数据
      loadStudents() {
        this.$axios.post('/api/student/list', {})
          .then(response => {
            if (response.data && response.data.code === 0) {
              this.allStudents = response.data.records || []
              this.students = this.allStudents // 初始化时显示所有学生
            } else {
              console.error('加载学生数据失败:', response.data.msg)
              this.$message({
                type: 'error',
                message: '加载学生数据失败: ' + (response.data.msg || '')
              })
            }
          })
          .catch(error => {
            console.error('加载学生数据失败:', error)
            this.$message({
              type: 'error',
              message: '加载学生数据失败'
            })
          })
      },
      //提交表单
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            //先检查是否重复
            if (this.addForm.studentId && this.addForm.courseId) {
              this.checkingDuplicate = true
              this.$axios.get(`/api/score/checkExists/${this.addForm.studentId}/${this.addForm.courseId}`)
                .then(checkResponse => {
                  if (checkResponse.data && checkResponse.data.exists) {
                    this.$message.error('该学生已有该课程的成绩记录，无法重复添加！')
                    return
                  }
                  
                  //没有重复，提交表单
                  this.loading = true
                  this.$axios.post('/api/score/add', this.addForm)
                    .then(response => {
                      if (response.data && response.data.success) {
                        this.$message({
                          type: 'success',
                          message: '添加成功',
                          duration: 2000
                        })
                        //延迟跳转，让用户看到成功提示
                        setTimeout(() => {
                          this.$router.push('/score')
                        }, 1500)
                      } else {
                        this.$message({
                          type: 'error',
                          message: response.data ? response.data.message : '添加失败'
                        })
                      }
                    })
                    .catch(error => {
                      console.error('添加成绩失败:', error)
                      this.$message({
                        type: 'error',
                        message: '网络错误，请稍后重试'
                      })
                    })
                    .finally(() => {
                      this.loading = false
                    })
                })
                .catch(error => {
                  console.error('检查成绩重复失败:', error)
                  this.$message.error('检查成绩重复失败，请稍后重试')
                })
                .finally(() => {
                  this.checkingDuplicate = false
                })
            }
          } else {
            this.$message({
              type: 'error',
              message: '表单验证失败，请检查必填项和格式'
            })
            return false
          }
        })
      },
      //重置表单
      resetForm (formName) {
        this.$refs[formName].resetFields()
      },
      //返回成绩管理页面
      back () {
        this.$router.push('/score')
      }
    }
  }
</script>
<style>
  .box1 {
    margin-left: 50px;
    margin-right: 50px;
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
  .el-form-item__label {
    font-weight: bold;
  }
  .el-form-item {
    margin-bottom: 20px;
  }
  .el-button {
    margin-right: 10px;
  }
  .el-button--primary {
    background-color: #1890ff;
    border-color: #1890ff;
  }
</style>