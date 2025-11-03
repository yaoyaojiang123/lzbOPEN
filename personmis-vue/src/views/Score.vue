<template>
    <NavMain></NavMain>
    <div class="score-container">
        <div class="search-form-wrapper">
            <el-form ref="selectForm" :model="selectForm" class="search-form" label-width="120px"> 
  <el-row :gutter="20">
    <el-col :span="6">
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="selectForm.studentName" placeholder="请输入学生姓名" clearable></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="6">
      <el-form-item label="班级" prop="classId">
        <el-select v-model="selectForm.classId" placeholder="请选择班级" @change="handleClassChange" clearable>
          <el-option v-for="(item,index) in classes" :key="index" :label="item.className" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="6">
      <el-form-item label="课程" prop="courseId">
        <el-select v-model="selectForm.courseId" placeholder="请选择课程" clearable>
          <el-option v-for="(item,index) in courses" :key="index" :label="item.courseName" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
    </el-col>
    <el-col :span="6">
      <el-form-item label="学期" prop="semester">
        <el-select v-model="selectForm.semester" placeholder="请选择学期" clearable>
          <el-option label="第一学期" value="第一学期"></el-option>
          <el-option label="第二学期" value="第二学期"></el-option>
        </el-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-form-item>
        <div class="button-group">
          <el-button type="primary" @click="selectScoresByCon" icon="el-icon-search">查询</el-button>
          <el-button @click="resetForm('selectForm')" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增成绩</el-button>
          <el-button type="danger" @click="batchDelete" :disabled="!selectedRows.length" icon="el-icon-delete">批量删除</el-button>
          <el-button type="info" @click="showStatistics" icon="el-icon-data-line">班级成绩统计</el-button>
          <el-button type="warning" @click="exportScores" icon="el-icon-download">导出成绩</el-button>
          <el-upload
            class="upload-demo"
            action=""
            :on-change="handleExcelUpload"
            :auto-upload="false"
            accept=".xlsx,.xls"
            :show-file-list="false"
          >
            <el-button type="primary" icon="el-icon-upload2">导入成绩</el-button>
          </el-upload>
        </div>
      </el-form-item>
            </el-form>
        </div>
    </div>
<!-- 成绩统计面板 -->
<div v-if="showStatisticsPanel" class="statistics-panel card-shadow">
  <el-card class="statistics-card">
    <template #header>
      <div class="card-header">
        <span>{{ currentClassName }} - 成绩统计分析</span>
      </div>
    </template>
    <el-row>
      <el-col :span="24">
        <div id="scoreChart" style="height: 400px;"></div>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6" v-for="(stat, index) in statisticsData" :key="index">
        <el-card shadow="hover">
          <h4 style="margin: 0;">{{ stat.course_name }}</h4>
          <div class="stat-item">平均分: {{ stat.avg_score.toFixed(2) }}</div>
          <div class="stat-item">最高分: {{ stat.max_score }}</div>
          <div class="stat-item">最低分: {{ stat.min_score }}</div>
          <div class="stat-item">参考人数: {{ stat.student_count }}</div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</div>
<div class="table-wrapper">
  <el-table
    :data="tableData" 
    :header-cell-style="headerCellStyle" 
    :row-style="rowStyle"
    :cell-style="cellStyle"
    :default-sort="{prop: 'score', order: 'descending'}"
    @selection-change="handleSelectionChange"
    class="score-table card-shadow">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="成绩ID" sortable width="80"></el-table-column>
    <el-table-column prop="studentName" label="学生姓名" min-width="120"></el-table-column>
    <el-table-column prop="className" label="班级" min-width="100"></el-table-column>
    <el-table-column prop="courseName" label="课程名称" min-width="120"></el-table-column>
    <el-table-column prop="score" label="成绩" sortable width="100">
      <template #default="scope">
        <div class="score-cell">
          <span :class="getScoreClass(scope.row.score)">{{ scope.row.score }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="semester" label="学期" width="100"></el-table-column>
    <el-table-column prop="examDate" label="考试日期" width="140"></el-table-column>
    <el-table-column label="操作" width="180" fixed="right">
      <template #default="scope">
        <el-button
          size="mini" type="success" icon="el-icon-edit"
          @click="handleEdit(scope.$index, scope.row, 'update')">编辑</el-button>
       <el-button
          size="mini" type="primary" icon="el-icon-view"
          @click="handleEdit(scope.$index, scope.row, 'detail')">详情</el-button>
        <el-button
          size="mini" type="danger" icon="el-icon-delete"
          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  </div>
 <div class="pagination-wrapper">
    <el-pagination
      @current-change="handleCurrentChange"
      v-model:currentPage="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
  </div>
  <el-dialog 
    :title="editType === 'add' ? '新增成绩' : (editType === 'update' ? '修改成绩' : '成绩详情')"
    v-model="scoreDialogVisible"  
    width="70%"
    :before-close="handleDialogClose"
    class="score-dialog">
   <el-form ref="addForm" :model="addForm" :rules="rules" :disabled="editType === 'detail'" style="width:100%;"  label-width="120px" class="dialog-form">
   <el-row :gutter="20">
        <el-col :span="8">
            <el-form-item label="学生"  prop="studentId">
                <el-select v-model="addForm.studentId" placeholder="请选择学生" clearable>
                    <el-option v-for="(item,index) in students" :key="index" :label="item.studentName" :value="item.id"></el-option>
                </el-select>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="课程"  prop="courseId">
                <el-select v-model="addForm.courseId" placeholder="请选择课程" clearable>
                    <el-option v-for="(item,index) in courses" :key="index" :label="item.courseName" :value="item.id"></el-option>
                </el-select>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="成绩"  prop="score">
                <el-input v-model.number="addForm.score" type="number" placeholder="请输入成绩" min="0" max="100"></el-input>
            </el-form-item>  
        </el-col>
        <el-col :span="8">
            <el-form-item label="学期" prop="semester">
                <el-select v-model="addForm.semester" placeholder="请选择学期" clearable>
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
        <el-col :span="8">
            <el-form-item label="考试类型" prop="examType">
                <el-select v-model="addForm.examType" placeholder="请选择考试类型" clearable>
                    <el-option label="期中考试" value="期中考试"></el-option>
                    <el-option label="期末考试" value="期末考试"></el-option>
                    <el-option label="平时测验" value="平时测验"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="24">
            <el-form-item label="备注" prop="remark">
                <el-input v-model="addForm.remark" type="textarea" placeholder="请输入备注信息" :rows="3"></el-input>
            </el-form-item>
        </el-col>
    </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="scoreDialogVisible = false" icon="el-icon-close">取消</el-button>
        <el-button v-if="editType === 'update'" type="warning" @click="quickUpdateScore" icon="el-icon-refresh-right">快速修改成绩</el-button>
        <el-button v-if="editType !== 'detail'" type="primary" @click="submitForm('addForm')" icon="el-icon-check">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script>
  import NavMain from '@/components/NavMain.vue'
  export default {
    name: 'Score',
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
        //课程选项
        courses: [],
        //学生选项
        students: [],
        //加载状态
        loading: false,
        //分页数据
        currentPage: 1,
        pageSize: 10,
        total: 0,
        //修改表单
        addForm: {},
        scoreDialogVisible: false,
        //编辑类型
        editType: '',
        //修改表单验证规则
        rules: {
          studentId: [{required: true, message: '请选择学生', trigger: 'change'}],
          courseId: [{required: true, message: '请选择课程', trigger: 'change'}],
          score: [
            {required: true, message: '请输入成绩', trigger: 'blur'},
            {validator: this.validateScore, trigger: 'blur'}
          ],
          semester: [{required: true, message: '请选择学期', trigger: 'change'}],
          examDate: [{required: true, message: '请选择考试日期', trigger: 'change'}],
          examType: [{required: true, message: '请选择考试类型', trigger: 'change'}]
        },
        selectedRows: [],
        //表格样式设置
        headerCellStyle: {
          backgroundColor: '#f5f7fa',
          color: '#2c3e50',
          fontWeight: 'bold',
          fontSize: '14px',
          height: '48px'
        },
        rowStyle: {
          height: '45px',
          transition: 'background-color 0.2s'
        },
        cellStyle: {
          textAlign: 'center',
          verticalAlign: 'middle'
        },
        //成绩统计相关数据
        showStatisticsPanel: false,
        statisticsData: [],
        currentClassName: '',
        scoreChart: null
      }
    },
    created () {
      //加载必要的数据
      this.loadClasses()
      this.loadCourses()
      this.loadStudents()
      //查询成绩
      this.selectScoresByCon()
    },
    methods: {
      //新增成绩
      handleAdd() {
        this.editType = 'add'
        this.addForm = {
          semester: '第一学期',
          examType: '期末考试',
          examDate: new Date()
        }
        this.scoreDialogVisible = true
      },
      
      //处理班级变更
      handleClassChange(value) {
        if (value) {
          const selectedClass = this.classes.find(cls => cls.id === value);
          this.currentClassName = selectedClass ? selectedClass.className : '';
        }
      },
      
      //显示成绩统计
      showStatistics() {
        if (!this.selectForm.classId) {
          this.$message({
            type: 'warning',
            message: '请先选择班级'
          });
          return;
        }
        
        this.loadStatisticsData();
        this.showStatisticsPanel = true;
      },
      
      //加载统计数据
      loadStatisticsData() {
        this.loading = true;
        this.$axios.get(`/api/score/getStatisticsByClassId/${this.selectForm.classId}`)
          .then(response => {
            if (response.data && response.data.code === 0) {
              this.statisticsData = response.data.data || [];
              this.renderScoreChart();
            } else {
              this.$message({
                type: 'error',
                message: response.data ? response.data.msg : '加载统计数据失败'
              });
            }
          })
          .catch(error => {
            console.error('加载统计数据失败:', error);
            this.$message({
              type: 'error',
              message: '网络错误，请稍后重试'
            });
          })
          .finally(() => {
            this.loading = false;
          });
      },
      
      //渲染成绩图表
      renderScoreChart() {
        if (!this.statisticsData || this.statisticsData.length === 0) {
          return;
        }
        
        // 模拟图表渲染（实际项目中应使用ECharts等图表库）
        const chartContainer = document.getElementById('scoreChart');
        if (chartContainer) {
          chartContainer.innerHTML = '<div style="text-align: center; padding: 100px 0; color: #606266;">\n' +
            '<div style="font-size: 20px; margin-bottom: 20px;">班级成绩概览</div>\n' +
            '<div style="display: flex; justify-content: space-around; flex-wrap: wrap;">\n' +
            this.statisticsData.map(stat => `\n              <div style="text-align: center; margin: 10px; padding: 20px; border: 1px solid #e4e7ed; border-radius: 4px;">\n                <div style="font-weight: bold; margin-bottom: 10px;">${stat.course_name}</div>\n                <div style="font-size: 32px; color: #409EFF;">${stat.avg_score.toFixed(1)}</div>\n              </div>\n            `).join('') +
            '</div>\n          </div>';
        }
      },
      
      //成绩验证器
      validateScore(rule, value, callback) {
        // 确保值是数字类型，处理字符串转换
        const numValue = Number(value);
        
        if (value === '' || value === null || value === undefined) {
          callback(new Error('请输入成绩'));
        } else if (isNaN(numValue)) {
          callback(new Error('成绩必须是数字'));
        } else if (numValue < 0 || numValue > 100) {
          callback(new Error('成绩应在0到100之间'));
        } else {
          callback();
        }
      },
      
      //导出成绩
      exportScores() {
        this.$confirm('确定要导出当前查询条件下的成绩吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          // 模拟导出功能
          this.$message({
            type: 'success',
            message: '导出成功，请在实际项目中实现文件下载功能'
          });
          
          // 实际项目中的导出代码示例：
          // const url = '/api/score/export?' + Object.keys(this.selectForm).map(key => key + '=' + encodeURIComponent(this.selectForm[key])).join('&');
          // window.open(url);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消导出'
          });
        });
      },
      
      //处理Excel上传
      handleExcelUpload(file) {
        this.$confirm('确定要导入成绩数据吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
          
          // 创建FormData对象
          const formData = new FormData();
          formData.append('file', file.raw);
          
          // 模拟导入功能
          setTimeout(() => {
            this.loading = false;
            this.$message({
              type: 'success',
              message: '导入成功，已更新成绩数据'
            });
            // 重新查询数据
            this.selectScoresByCon();
          }, 1500);
          
          // 实际项目中的导入代码示例：
          // this.$axios.post('/api/score/import', formData, {
          //   headers: {
          //     'Content-Type': 'multipart/form-data'
          //   }
          // }).then(response => {
          //   this.loading = false;
          //   if (response.data && response.data.code === 0) {
          //     this.$message({
          //       type: 'success',
          //       message: '导入成功'
          //     });
          //     this.selectScoresByCon();
          //   } else {
          //     this.$message({
          //       type: 'error',
          //       message: response.data ? response.data.msg : '导入失败'
          //     });
          //   }
          // }).catch(error => {
          //   this.loading = false;
          //   this.$message({
          //     type: 'error',
          //     message: '网络错误，请稍后重试'
          //   });
          // });
        }).catch(() => {
          // 取消上传，清空文件
          this.$refs['upload-demo'].clearFiles();
        });
      },
      
      //重置表单
      resetForm(formName) {
        if (this.$refs[formName]) {
          this.$refs[formName].resetFields();
        }
        // 清空查询条件对象
        this.selectForm = {};
        // 重新查询数据
        this.selectScoresByCon();
      },
      
      //处理选择变化
      handleSelectionChange(rows) {
        this.selectedRows = rows
      },
      
      //批量删除
      batchDelete() {
        this.$confirm('确定要删除选中的成绩记录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const ids = this.selectedRows.map(row => row.id)
          this.$axios.post('/api/score/batchDelete', { ids })
            .then(response => {
              if (response.data && response.data.code === 0) {
                this.$message({
                  type: 'success',
                  message: '删除成功'
                })
                this.selectScoresByCon()
                this.selectedRows = []
              } else {
                this.$message({
                  type: 'error',
                  message: response.data ? response.data.msg : '删除失败'
                })
              }
            })
            .catch(error => {
              console.error('批量删除失败:', error)
              this.$message({
                type: 'error',
                message: '网络错误，请稍后重试'
              })
            })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      
      //根据条件查询成绩
      selectScoresByCon () {
        this.loading = true
        //构建查询参数，只包含有效的查询条件
        const params = {
          page: this.currentPage,
          limit: this.pageSize
        }
        
        // 只添加非空和有效的查询条件
        if (this.selectForm.studentName && typeof this.selectForm.studentName === 'string' && this.selectForm.studentName.trim()) {
          params.studentName = this.selectForm.studentName.trim();
        }
        if (this.selectForm.classId && typeof this.selectForm.classId === 'number') {
          params.classId = this.selectForm.classId;
        }
        if (this.selectForm.courseId && typeof this.selectForm.courseId === 'number') {
          params.courseId = this.selectForm.courseId;
        }
        if (this.selectForm.semester && typeof this.selectForm.semester === 'string') {
          params.semester = this.selectForm.semester;
        }
        
        console.log('查询成绩参数:', params);
        
        this.$axios.post('/api/score/list', params)
          .then(response => {
            console.log('成绩查询响应:', response.data);
            if (response.data && response.data.code === 0) {
              // 过滤并验证表格数据
              this.tableData = Array.isArray(response.data.rows) ? response.data.rows : [];
              this.total = typeof response.data.total === 'number' ? response.data.total : 0;
              console.log('成绩数据加载完成，数据条数:', this.tableData.length);
            } else {
              this.tableData = [];
              this.total = 0;
              this.$message({
                type: 'error',
                message: response.data ? (response.data.msg || '查询成绩失败') : '查询成绩失败'
              })
            }
          })
          .catch(error => {
            this.tableData = [];
            this.total = 0;
            console.error('查询成绩失败:', error);
            this.$message({
              type: 'error',
              message: '网络错误，请稍后重试'
            })
          })
          .finally(() => {
            this.loading = false
          })
      },
      
      //加载班级数据
      loadClasses() {
        this.$axios.get('/api/classInfo/getAll')
          .then(response => {
            if (response.data && response.data.code === 0) {
              // 过滤并验证班级数据格式
              const validClasses = (response.data.data || []).filter(cls => 
                cls && typeof cls.id === 'number' && typeof cls.className === 'string' && cls.className.trim()
              );
              this.classes = validClasses;
              // 记录数据加载情况用于调试
              console.log('班级数据加载完成，有效数据条数:', validClasses.length);
            } else {
              console.error('加载班级数据失败:', response.data ? response.data.msg : '未知错误');
              this.$message({
                type: 'error',
                message: '加载班级数据失败: ' + (response.data ? response.data.msg : '未知错误')
              })
            }
          })
          .catch(error => {
            console.error('加载班级数据失败:', error);
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
            if (response.data && response.data.code === 0) {
              // 过滤并验证课程数据格式
              const validCourses = (response.data.rows || []).filter(course => 
                course && typeof course.id === 'number' && typeof course.courseName === 'string' && course.courseName.trim()
              );
              this.courses = validCourses;
              // 记录数据加载情况用于调试
              console.log('课程数据加载完成，有效数据条数:', validCourses.length);
            } else {
              console.error('加载课程数据失败:', response.data ? response.data.msg : '未知错误');
              this.$message({
                type: 'error',
                message: '加载课程数据失败: ' + (response.data ? response.data.msg : '未知错误')
              })
            }
          })
          .catch(error => {
            console.error('加载课程数据失败:', error);
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
              // 过滤并验证学生数据格式
              const validStudents = (response.data.rows || []).filter(student => 
                student && typeof student.id === 'number' && typeof student.studentName === 'string' && student.studentName.trim()
              );
              this.students = validStudents;
              // 记录数据加载情况用于调试
              console.log('学生数据加载完成，有效数据条数:', validStudents.length);
            } else {
              console.error('加载学生数据失败:', response.data ? response.data.msg : '未知错误');
              this.$message({
                type: 'error',
                message: '加载学生数据失败: ' + (response.data ? response.data.msg : '未知错误')
              })
            }
          })
          .catch(error => {
            console.error('加载学生数据失败:', error);
            this.$message({
              type: 'error',
              message: '加载学生数据失败'
            })
          })
      },
      //根据成绩获取样式类
      getScoreClass(score) {
        if (score >= 90) return 'high-score'
        if (score >= 60) return 'medium-score'
        return 'low-score'
      },
      //处理编辑
      handleEdit (index, row, type) {
        this.editType = type
        // 复制行数据到表单
        this.addForm = JSON.parse(JSON.stringify(row))
        
        // 显示对话框
        this.scoreDialogVisible = true
      },
      
      //处理对话框关闭
      handleDialogClose() {
        // 关闭对话框时重置表单
        if (this.$refs.addForm) {
          this.$refs.addForm.resetFields();
        }
        // 关闭对话框
        this.scoreDialogVisible = false;
        // 清除编辑类型和表单数据
        this.editType = '';
      },
      //处理删除
      handleDelete (index, row) {
        this.$confirm('确定要删除该成绩记录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.delete(`/api/score/delete/${row.id}`)
            .then(response => {
              if (response.data && response.data.code === 0) {
                this.tableData.splice(index, 1)
                this.total = this.tableData.length
                this.$message({
                  type: 'success',
                  message: '删除成功'
                })
              } else {
                this.$message({
                  type: 'error',
                  message: response.data ? response.data.msg : '删除失败'
                })
              }
            })
            .catch(error => {
              console.error('删除成绩失败:', error)
              this.$message({
                type: 'error',
                message: '网络错误，请稍后重试'
              })
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
            if (this.editType === 'add') {
              //新增操作
              this.$axios.post('/api/score/add', this.addForm)
                .then(response => {
                  if (response.data.code === 0) {
                    this.$message({
                      type: 'success',
                      message: '新增成功'
                    })
                    //重新查询数据
                    this.selectScoresByCon()
                    this.scoreDialogVisible = false
                  } else {
                    this.$message({
                      type: 'error',
                      message: response.data ? response.data.msg : '新增失败'
                    })
                  }
                })
                .catch(error => {
                  console.error('新增成绩失败:', error)
                  this.$message({
                    type: 'error',
                    message: '网络错误，请稍后重试'
                  })
                })
            } else if (this.editType === 'update') {
              //更新操作
              this.$axios.post('/api/score/update', this.addForm)
                .then(response => {
                  if (response.data.code === 0) {
                    this.$message({
                      type: 'success',
                      message: '更新成功'
                    })
                    //重新查询数据
                    this.selectScoresByCon()
                    this.scoreDialogVisible = false
                  } else {
                    this.$message({
                      type: 'error',
                      message: response.data ? response.data.msg : '更新失败'
                    })
                  }
                })
                .catch(error => {
                  console.error('更新成绩失败:', error)
                  this.$message({
                    type: 'error',
                    message: '网络错误，请稍后重试'
                  })
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
      
      //快速修改成绩
      quickUpdateScore() {
        if (!this.addForm.score || isNaN(this.addForm.score)) {
          this.$message({
            type: 'error',
            message: '请输入有效的成绩'
          });
          return;
        }
        
        // 只提交成绩字段
        const updateData = {
          id: this.addForm.id,
          score: this.addForm.score
        };
        
        this.$axios.post('/api/score/update', updateData)
          .then(response => {
            if (response.data.code === 0) {
              this.$message({
                type: 'success',
                message: '快速修改成功'
              })
              //重新查询数据
              this.selectScoresByCon()
              this.scoreDialogVisible = false
            } else {
              this.$message({
                type: 'error',
                message: response.data ? response.data.msg : '修改失败'
              })
            }
          })
          .catch(error => {
            console.error('快速修改成绩失败:', error)
            this.$message({
              type: 'error',
              message: '网络错误，请稍后重试'
            })
          })
      },
      //分页处理
      handleCurrentChange (currentPage) {
        this.currentPage = currentPage
        this.selectScoresByCon()
      }
    }
  }
</script>

<style>
  /* 全局容器样式 */
  .score-container {
    padding: 20px;
    margin: 0 auto;
    max-width: 1400px;
  }
  
  /* 搜索表单容器 */
  .search-form-wrapper {
    background: #fff;
    border-radius: 8px;
    padding: 24px;
    margin-bottom: 24px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    transition: box-shadow 0.3s;
  }
  
  .search-form-wrapper:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.08);
  }
  
  /* 搜索表单样式 */
  .search-form {
    width: 100%;
  }
  
  /* 按钮组样式 */
  .button-group {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    margin-top: 10px;
  }
  
  /* 表格容器样式 */
  .table-wrapper {
    margin-bottom: 24px;
    overflow: hidden;
  }
  
  /* 表格样式 */
  .score-table {
    border-radius: 8px;
    overflow: hidden;
  }
  
  /* 成绩单元格样式 */
  .score-cell {
    display: inline-block;
    padding: 4px 8px;
    border-radius: 4px;
    font-weight: 500;
  }
  
  /* 成绩等级颜色样式 */
  .high-score {
    color: #67C23A;
    background-color: #F0F9EB;
  }
  
  .medium-score {
    color: #E6A23C;
    background-color: #FDF6EC;
  }
  
  .low-score {
    color: #F56C6C;
    background-color: #FEF0F0;
  }
  
  /* 分页样式 */
  .pagination-wrapper {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
    padding: 16px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  }
  
  /* 统计面板样式 */
  .statistics-panel {
    margin-bottom: 24px;
    padding: 0;
    border-radius: 8px;
    overflow: hidden;
  }
  
  .statistics-card {
    border: none;
    margin-bottom: 0;
  }
  
  /* 对话框表单样式 */
  .dialog-form {
    padding: 16px 0;
  }
  
  /* 对话框样式 */
  .score-dialog .el-dialog__body {
    padding: 24px;
    max-height: 70vh;
    overflow-y: auto;
  }
  
  /* 阴影效果类 */
  .card-shadow {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;
  }
  
  .card-shadow:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.08);
  }
  
  /* 加载状态样式 */
  .el-loading-mask {
    background-color: rgba(255, 255, 255, 0.8);
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .score-container {
      padding: 12px;
    }
    
    .button-group {
      flex-direction: column;
    }
    
    .button-group .el-button {
      width: 100%;
    }
    
    .score-table {
      font-size: 12px;
    }
    
    .score-dialog .el-dialog__body {
      padding: 16px;
    }
  }
</style>