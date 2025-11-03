<template>
    <NavMain></NavMain>
<div class="box1">
<br>
<el-form ref="selectForm" :model="selectForm" style="width:50%;"  label-width="50%" > 
  <el-form-item label="班级名称"  prop="className">
    <el-input v-model="selectForm.className"  placeholder="请输入班级名称"></el-input>
  </el-form-item>
 <el-form-item label="年级" prop="grade">
    <el-select v-model="selectForm.grade" placeholder="请选择年级">
      <el-option v-for="(item,index) in grades" :key="index" :label="item" :value="item"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="selectClassesByCon">查询</el-button>
  </el-form-item>
</el-form>
</div>
<div class="box2">
  <el-table
    :data="tableData"  :header-cell-style="headClass" 
     :cell-style="{ textAlign: 'center' }"
    :default-sort = "{prop: 'id', order: 'descending'}">
    <el-table-column prop="id" label="ID" sortable></el-table-column>
    <el-table-column prop="className" label="班级名称"></el-table-column>
    <el-table-column prop="grade" label="年级"></el-table-column>
    <el-table-column prop="studentCount" label="学生人数"></el-table-column>
    <el-table-column prop="headTeacher" label="班主任"></el-table-column>
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
  <el-dialog title="班级修改" v-model="classDialogVisible"  width="100%">
   <el-form ref="addForm" :model="addForm" :rules="rules" style="width:100%;"  label-width="30%" >
   <el-row>
        <el-col :span="8">
            <el-form-item label="班级名称"  prop="className">
                <el-input v-model="addForm.className"  placeholder="请输入班级名称"></el-input>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="年级"  prop="grade">
                <el-select v-model="addForm.grade" placeholder="请选择年级">
                    <el-option v-for="(item,index) in grades" :key="index" :label="item" :value="item"></el-option>
                </el-select>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="班主任"  prop="headTeacher">
                <el-input v-model="addForm.headTeacher"  placeholder="请输入班主任姓名"></el-input>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="教室位置" prop="classroom">
                <el-input v-model="addForm.classroom"  placeholder="请输入教室位置"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="入学年份" prop="enrollmentYear">
                <el-input v-model="addForm.enrollmentYear"  placeholder="请输入入学年份"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="备注" prop="remark">
                <el-input v-model="addForm.remark"  placeholder="请输入备注信息"></el-input>
            </el-form-item>
        </el-col>
    </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="classDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm('addForm')">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script>
  import NavMain from '@/components/NavMain.vue'
  export default {
    name: 'ClassInfo',
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
        classDialogVisible: false,
        //编辑类型
        editType: '',
        //修改表单验证规则
        rules: {
          className: [{required: true, message: '请输入班级名称', trigger: 'blur'}],
          grade: [{required: true, message: '请选择年级', trigger: 'change'}],
          headTeacher: [{required: true, message: '请输入班主任姓名', trigger: 'blur'}]
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
      this.selectClassesByCon()
    },
    methods: {
      //根据条件查询班级
      selectClassesByCon () {
        //构建请求参数
        const params = {
          page: this.currentPage,
          limit: this.pageSize,
          ...this.selectForm
        }
        
        //调用后端API获取班级数据
        this.$axios.post('/api/classInfo/list', params)
          .then(response => {
            if (response.data.code === 0) {
              this.tableData = response.data.rows;
              this.total = response.data.total;
            } else {
              this.$message.error('获取班级列表失败: ' + response.data.msg);
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
        this.classDialogVisible = true
      },
      //处理删除
      handleDelete (index, row) {
        this.$confirm('确定要删除该班级吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //调用后端API删除班级
          this.$axios.post(`/api/classInfo/delete/${row.id}`)
            .then(response => {
              if (response.data.code === 0) {
                //重新查询数据
                this.selectClassesByCon();
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
      //提交表单
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            //根据编辑类型调用不同的后端API
            if (this.editType === 'update') {
              //更新操作
              this.$axios.post('/api/classInfo/update', this.addForm)
                .then(response => {
                  if (response.data.code === 0) {
                    //重新查询数据
                    this.selectClassesByCon();
                    this.classDialogVisible = false;
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
        this.selectClassesByCon()
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