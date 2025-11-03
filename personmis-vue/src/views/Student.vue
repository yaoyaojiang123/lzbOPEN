<template>
    <NavMain></NavMain>
<div class="box1">
<br>
<el-form ref="selectForm" :model="selectForm" style="width:50%;"  label-width="50%" > 
  <el-form-item label="学生姓名"  prop="studentName">
    <el-input v-model="selectForm.studentName"  placeholder="请输入学生姓名"></el-input>
  </el-form-item>
 <el-form-item label="班级" prop="classId">
    <el-select v-model="selectForm.classId" placeholder="请选择班级">
      <el-option v-for="(item,index) in classes" :key="index" :label="item.className" :value="item.id"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="selectStudentsByCon">查询</el-button>
    <el-button @click="resetForm">重置</el-button>
  </el-form-item>
</el-form>
</div>
<div class="box2">
  <el-button type="primary" style="margin-bottom: 10px; margin-right: 10px" @click="handleAdd">添加学生</el-button>
  <el-button type="danger" style="margin-bottom: 10px" @click="batchDelete" :disabled="selectedRows.length === 0">批量删除</el-button>
  <el-table
    :data="tableData"  :header-cell-style="headClass" 
     :cell-style="{ textAlign: 'center' }"
    :default-sort = "{prop: 'studentNo', order: 'descending'}"
    @selection-change="handleSelectionChange"
    ref="multipleTable">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="studentNo" label="学号" sortable></el-table-column>
    <el-table-column prop="studentName" label="学生姓名"></el-table-column>
    <el-table-column prop="className"  label="所在班级"></el-table-column>
    <el-table-column prop="sex" label="性别"></el-table-column>
    <el-table-column prop="birthday" label="出生日期"></el-table-column>
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
  <el-dialog title="学生信息修改" v-model="studentDialogVisible"  width="100%">
   <el-form ref="addForm" :model="addForm" :rules="rules" style="width:100%;"  label-width="30%" >
   <el-row>
        <el-col :span="8">
            <el-form-item label="学生姓名"  prop="studentName">
                <el-input v-model="addForm.studentName"  placeholder="请输入学生姓名"></el-input>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="学号"  prop="studentNo">
                <el-input v-model="addForm.studentNo"  placeholder="请输入学号"></el-input>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="性别"  prop="sex">
                <el-radio v-model="addForm.sex" label="男">男</el-radio>
                <el-radio v-model="addForm.sex" label="女">女</el-radio>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="出生日期"  prop="birthday">
                <el-date-picker v-model="addForm.birthday" type="date" placeholder="选择出生日期" style="width:100%"></el-date-picker>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="addForm.idCard"  placeholder="请输入身份证号"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="班级" prop="classId">
                <el-select v-model="addForm.classId" placeholder="请选择班级">
                    <el-option v-for="(item,index) in classes" :key="index" :label="item.className" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="联系电话" prop="phone">
                <el-input v-model="addForm.phone"  placeholder="请输入联系电话"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="家庭住址" prop="address">
                <el-input v-model="addForm.address"  placeholder="请输入家庭住址"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="家长姓名" prop="parentName">
                <el-input v-model="addForm.parentName"  placeholder="请输入家长姓名"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="家长联系电话" prop="parentPhone">
                <el-input v-model="addForm.parentPhone"  placeholder="请输入家长联系电话"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="入学日期" prop="entryDate">
                <el-date-picker v-model="addForm.entryDate" type="date" placeholder="选择入学日期" style="width:100%"></el-date-picker>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="状态" prop="status">
                <el-select v-model="addForm.status" placeholder="选择状态">
                    <el-option label="在读" value="在读"></el-option>
                    <el-option label="休学" value="休学"></el-option>
                    <el-option label="退学" value="退学"></el-option>
                    <el-option label="毕业" value="毕业"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
    </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="studentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm('addForm')">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script>
  import NavMain from '@/components/NavMain.vue'
  export default {
    name: 'Student',
    components: {
      NavMain
    },
    data () {
      return {
        //查询表单
        selectForm: {},
        //表格数据
        tableData: [],
        //班级选项
        classes: [],
        //选中的行
        selectedRows: [],
  
        //分页数据
        currentPage: 1,
        pageSize: 10,
        total: 0,
        //修改表单
        addForm: {},
        studentDialogVisible: false,
        //编辑类型
        editType: '',
        //修改表单验证规则
        rules: {
          studentName: [{required: true, message: '请输入学生姓名', trigger: 'blur'}],
          studentNo: [{required: true, message: '请输入学号', trigger: 'blur'}],
          sex: [{required: true, message: '请选择性别', trigger: 'change'}],
          classId: [{required: true, message: '请选择班级', trigger: 'change'}],
          birthday: [{required: true, message: '请选择出生日期', trigger: 'change'}],
          entryDate: [{required: true, message: '请选择入学日期', trigger: 'change'}],
          status: [{required: true, message: '请选择状态', trigger: 'change'}],
          idCard: [
            {pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, 
             message: '请输入正确的身份证号码格式', trigger: 'blur'}
          ],
          phone: [
            {pattern: /^1[3-9]\d{9}$/, 
             message: '请输入正确的手机号码格式', trigger: 'blur'}
          ],
          parentPhone: [
            {pattern: /^1[3-9]\d{9}$/, 
             message: '请输入正确的手机号码格式', trigger: 'blur'}
          ]
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
      //加载班级数据
      this.loadClasses();
      //查询学生数据
      this.selectStudentsByCon()
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
      //根据条件查询学生
      selectStudentsByCon () {
        //构建请求参数
        const params = {
          page: this.currentPage,
          limit: this.pageSize,
          ...this.selectForm
        }
        
        //调用后端API获取学生数据
        this.$axios.post('/api/student/list', params)
          .then(response => {
            if (response.data.code === 0) {
              this.tableData = response.data.rows;
              this.total = response.data.total;
            } else {
              this.$message.error('获取学生列表失败: ' + response.data.msg);
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
        this.studentDialogVisible = true
      },
      //处理删除
      handleDelete (index, row) {
        this.$confirm('确定要删除该学生吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //调用后端API删除学生
          this.$axios.post(`/api/student/delete/${row.id}`)
            .then(response => {
              if (response.data.code === 0) {
                //重新查询数据
                this.selectStudentsByCon();
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
      //处理添加学生
      handleAdd() {
        this.editType = 'add'
        this.addForm = {
          status: '在读' // 设置默认状态为在读
        }
        this.studentDialogVisible = true
      },
      //提交表单
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            //根据编辑类型调用不同的后端API
            if (this.editType === 'update') {
              //更新操作
              this.$axios.post('/api/student/update', this.addForm)
                .then(response => {
                  if (response.data.code === 0) {
                    //重新查询数据
                    this.selectStudentsByCon();
                    this.studentDialogVisible = false;
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
            } else if (this.editType === 'add') {
              //添加操作
              this.$axios.post('/api/student/add', this.addForm)
                .then(response => {
                  if (response.data.code === 0) {
                    //重新查询数据
                    this.selectStudentsByCon();
                    this.studentDialogVisible = false;
                    this.$message({
                      type: 'success',
                      message: '添加成功'
                    })
                  } else {
                    this.$message.error('添加失败: ' + response.data.msg);
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
        this.selectStudentsByCon()
      },
      //重置表单
      resetForm() {
        this.$refs.selectForm.resetFields()
        this.selectForm = {}
        this.selectStudentsByCon()
      },
      //处理选择变化
      handleSelectionChange(selection) {
        this.selectedRows = selection
      },
      //批量删除
      batchDelete() {
        if (this.selectedRows.length === 0) {
          this.$message.warning('请选择要删除的学生')
          return
        }
        
        const ids = this.selectedRows.map(row => row.id)
        
        this.$confirm(`确定要删除选中的${this.selectedRows.length}个学生吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //调用后端API批量删除学生
          this.$axios.post('/api/student/batchDelete', { ids: ids })
            .then(response => {
              if (response.data.code === 0) {
                //重新查询数据
                this.selectStudentsByCon()
                //清空选择
                this.$refs.multipleTable.clearSelection()
                this.$message({
                  type: 'success',
                  message: '删除成功'
                })
              } else {
                this.$message.error('删除失败: ' + response.data.msg)
              }
            })
            .catch(error => {
              this.$message.error('请求失败: ' + error.message)
            })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
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