<template>
    <NavMain></NavMain>
<div class="box1">
<br>
<el-form ref="selectForm" :model="selectForm" style="width:50%;"  label-width="50%" > 
  <el-form-item label="课程名称"  prop="courseName">
    <el-input v-model="selectForm.courseName"  placeholder="请输入课程名称"></el-input>
  </el-form-item>
 <el-form-item label="授课教师" prop="teacherName">
    <el-input v-model="selectForm.teacherName"  placeholder="请输入授课教师"></el-input>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="selectCoursesByCon">查询</el-button>
    <el-button @click="resetForm('selectForm')">重置</el-button>
    <el-button type="success" @click="handleAdd">新增课程</el-button>
  </el-form-item>
</el-form>
</div>
<div class="box2">
  <el-table
    :data="tableData"  :header-cell-style="headClass" 
     :cell-style="{ textAlign: 'center' }"
    :default-sort = "{prop: 'id', order: 'descending'}">
    <el-table-column prop="id" label="课程ID" sortable></el-table-column>
    <el-table-column prop="courseName" label="课程名称"></el-table-column>
    <el-table-column prop="teacherName"  label="授课教师"></el-table-column>
    <el-table-column prop="credit" label="学分"></el-table-column>
    <el-table-column prop="totalHours" label="总课时"></el-table-column>
    <el-table-column label="操作">
      <template #default="scope">
        <el-button
          size="mini"  type="success"
          @click="handleEdit(scope.$index, scope.row, 'update')">编辑</el-button>
       <el-button
          size="mini" type="primary"
          @click="handleEdit(scope.$index, scope.row, 'detail')">详情</el-button>
        <el-button
          size="mini" type="danger"
          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  </div>
 <div class="block">
    <el-pagination
      @current-change="handleCurrentChange"
      v-model:currentPage="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total">
    </el-pagination>
  </div>
  <el-dialog title="课程信息修改" v-model="courseDialogVisible"  width="100%">
   <el-form ref="addForm" :model="addForm" :rules="rules" style="width:100%;"  label-width="30%" >
   <el-row>
        <el-col :span="8">
            <el-form-item label="课程名称"  prop="courseName">
                <el-input v-model="addForm.courseName"  placeholder="请输入课程名称"></el-input>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="授课教师"  prop="teacherName">
                <el-input v-model="addForm.teacherName"  placeholder="请输入授课教师"></el-input>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="学分"  prop="credit">
                <el-input v-model="addForm.credit" type="number" placeholder="请输入学分"></el-input>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="总课时" prop="totalHours">
                <el-input v-model="addForm.totalHours" type="number" placeholder="请输入总课时"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="课程类型" prop="courseType">
                <el-select v-model="addForm.courseType" placeholder="请选择课程类型">
                    <el-option label="必修课" value="必修课"></el-option>
                    <el-option label="选修课" value="选修课"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="适用年级" prop="suitableGrade">
                <el-select v-model="addForm.suitableGrade" placeholder="请选择适用年级">
                    <el-option v-for="(item,index) in grades" :key="index" :label="item" :value="item"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="24">
            <el-form-item label="课程描述" prop="description">
                <el-input v-model="addForm.description" type="textarea" placeholder="请输入课程描述"></el-input>
            </el-form-item>
        </el-col>
    </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="courseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm('addForm')">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script>
  import NavMain from '@/components/NavMain.vue'
  export default {
    name: 'Course',
    components: {
      NavMain
    },
    data () {
      return {
        //查询表单
        selectForm: {},
        //表格数据
        tableData: [],
        //年级选项
        grades: ['一年级', '二年级', '三年级', '四年级', '五年级', '六年级'],
        //分页数据
        currentPage: 1,
        pageSize: 10,
        total: 0,
        //修改表单
        addForm: {},
        courseDialogVisible: false,
        //编辑类型
        editType: '',
        //修改表单验证规则
        rules: {
          courseName: [{required: true, message: '请输入课程名称', trigger: 'blur'}],
          teacherName: [{required: true, message: '请输入授课教师', trigger: 'blur'}],
          credit: [
            {required: true, message: '请输入学分', trigger: 'blur'},
            {type: 'number', min: 0.5, max: 10, message: '学分应在0.5到10之间', trigger: 'blur'}
          ],
          totalHours: [
            {required: true, message: '请输入总课时', trigger: 'blur'},
            {type: 'number', min: 1, message: '总课时不能小于1', trigger: 'blur'}
          ],
          courseType: [{required: true, message: '请选择课程类型', trigger: 'change'}],
          suitableGrade: [{required: true, message: '请选择适用年级', trigger: 'change'}]
        },
        //设置表头样式
        headClass:{
          backgroundColor:'#eef1f6',
          color:'#303133',
          fontWeight:'bold'
        }
      }
    },
    created () {
      this.selectCoursesByCon()
    },
    methods: {
      //根据条件查询课程
      selectCoursesByCon () {
        //构建请求参数
        const params = {
          page: this.currentPage,
          limit: this.pageSize,
          ...this.selectForm
        }
        
        //调用后端API获取课程数据
        this.$axios.post('/api/course/list', params)
          .then(response => {
            if (response.data.code === 0) {
              this.tableData = response.data.rows;
              this.total = response.data.total;
            } else {
              this.$message.error('获取课程列表失败: ' + response.data.msg);
            }
          })
          .catch(error => {
            this.$message.error('请求失败: ' + error.message);
          })
      },
      //处理编辑
      handleEdit (index, row, type) {
        this.editType = type
        this.addForm = JSON.parse(JSON.stringify(row))
        if (type === 'detail') {
          if (this.$refs.addForm) {
            this.$refs.addForm.disabled = true
          }
        } else {
          if (this.$refs.addForm) {
            this.$refs.addForm.disabled = false
          }
        }
        this.courseDialogVisible = true
      },
      //处理删除
      handleDelete (index, row) {
        this.$confirm('确定要删除该课程吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //调用后端API删除课程
          this.$axios.post(`/api/course/delete/${row.id}`)
            .then(response => {
              if (response.data.code === 0) {
                //重新查询数据
                this.selectCoursesByCon();
                this.$message({
                  type: 'success',
                  message: '删除成功'
                })
              } else {
                this.$message.error('删除失败: ' + response.data.msg);
              }
            })
            .catch(error => {
              this.$message.error('请求失败: ' + error.message);
            })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      //新增课程
      handleAdd() {
        this.editType = 'add'
        this.addForm = {
          courseType: '必修课',
          suitableGrade: '一年级'
        }
        this.courseDialogVisible = true
      },
      
      //重置表单
      resetForm(formName) {
        this.$refs[formName].resetFields()
        this.selectCoursesByCon()
      },
      
      //提交表单
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            //根据编辑类型调用不同的后端API
            if (this.editType === 'add') {
              //新增操作
              this.$axios.post('/api/course/add', this.addForm)
                .then(response => {
                  if (response.data.code === 0) {
                    //重新查询数据
                    this.selectCoursesByCon();
                    this.courseDialogVisible = false;
                    this.$message({
                      type: 'success',
                      message: '新增成功'
                    })
                  } else {
                    this.$message.error('新增失败: ' + response.data.msg);
                  }
                })
                .catch(error => {
                  this.$message.error('请求失败: ' + error.message);
                })
            } else if (this.editType === 'update') {
              //更新操作
              this.$axios.post('/api/course/update', this.addForm)
                .then(response => {
                  if (response.data.code === 0) {
                    //重新查询数据
                    this.selectCoursesByCon();
                    this.courseDialogVisible = false;
                    this.$message({
                      type: 'success',
                      message: '更新成功'
                    })
                  } else {
                    this.$message.error('更新失败: ' + response.data.msg);
                  }
                })
                .catch(error => {
                  this.$message.error('请求失败: ' + error.message);
                })
            }
          } else {
            this.$message({
              type: 'error',
              message: '表单验证失败'
            })
            return false
          }
        })
      },
      //分页处理
      handleCurrentChange (currentPage) {
        this.currentPage = currentPage
        this.selectCoursesByCon()
      }
    }
  }
</script>
<style>
  .box1 {
    margin-left: 50px;
    margin-right: 50px;
  }
  .box2 {
    margin-left: 50px;
    margin-right: 50px;
  }
  .block {
    margin-left: 50px;
    margin-right: 50px;
    margin-top: 20px;
    text-align: right;
  }
</style>