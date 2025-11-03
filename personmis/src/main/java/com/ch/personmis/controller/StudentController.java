package com.ch.personmis.controller;

import com.ch.personmis.entity.Student;
import com.ch.personmis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 学生信息控制器
 */
@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    // 身份证号正则表达式
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");
    
    /**
     * 验证学生信息
     */
    private void validateStudent(Student student) {
        // 验证手机号
        if (student.getPhone() != null && !student.getPhone().isEmpty()) {
            if (!PHONE_PATTERN.matcher(student.getPhone()).matches()) {
                throw new RuntimeException("手机号格式不正确");
            }
        }
        
        // 注意：Student实体类中没有家长手机号和身份证属性，相关验证被注释掉
    // 如需添加这些字段，请先在Student实体类中添加相应属性及getter/setter方法
        
        // 验证学号
        if (student.getStudentNo() == null || student.getStudentNo().isEmpty()) {
            throw new RuntimeException("学号不能为空");
        }
        
        // 验证姓名
        if (student.getStudentName() == null || student.getStudentName().isEmpty()) {
            throw new RuntimeException("学生姓名不能为空");
        }
    }

    /**
     * 获取学生列表（分页）
     */
    @PostMapping("/list")
    public Map<String, Object> getStudentList(@RequestBody Map<String, Object> map) {
        try {
            // 参数校验
            if (map == null) {
                map = new HashMap<>();
            }
            
            // 设置分页参数，确保页码和每页数量为正数
            int page = Math.max(1, Integer.parseInt(map.getOrDefault("page", "1").toString()));
            int limit = Math.max(1, Math.min(100, Integer.parseInt(map.getOrDefault("limit", "10").toString())));
            map.put("start", (page - 1) * limit);
            map.put("limit", limit);
            
            // 调用服务层获取数据
            Map<String, Object> result = studentService.findStudentList(map);
            
            // 设置响应状态
            result.put("code", 0);
            result.put("msg", "success");
            return result;
        } catch (NumberFormatException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 400);
            result.put("msg", "页码或每页数量格式错误");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", "获取学生列表失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 根据ID获取学生信息
     */
    @GetMapping("/get/{id}")
    public Map<String, Object> getStudentById(@PathVariable Integer id) {
        try {
            Student student = studentService.findStudentById(id);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", student);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }

    /**
     * 添加学生信息
     */
    @PostMapping("/add")
    public Map<String, Object> addStudent(@RequestBody Student student) {
        try {
            // 验证学生信息
            validateStudent(student);
            
            // 设置默认状态
            if (student.getStatus() == null || student.getStatus().isEmpty()) {
                student.setStatus("在读");
            }
            
            // 调用服务层添加学生
            int result = studentService.addStudent(student);
            
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "添加成功");
                resultMap.put("data", student);  // 返回添加后的学生信息
            } else {
                resultMap.put("code", 500);
                resultMap.put("msg", "添加失败，未知错误");
            }
            return resultMap;
        } catch (RuntimeException e) {
            // 业务异常，返回具体错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 400);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        } catch (Exception e) {
            // 系统异常，返回通用错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "添加学生失败: " + e.getMessage());
            return resultMap;
        }
    }

    /**
     * 更新学生信息
     */
    @PostMapping("/update")
    public Map<String, Object> updateStudent(@RequestBody Student student) {
        try {
            // 验证学生ID
            if (student.getId() == null || student.getId() <= 0) {
                throw new RuntimeException("学生ID不能为空且必须大于0");
            }
            
            // 验证学生信息
            validateStudent(student);
            
            // 调用服务层更新学生
            int result = studentService.updateStudent(student);
            
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "更新成功");
                resultMap.put("data", student);  // 返回更新后的学生信息
            } else {
                resultMap.put("code", 404);
                resultMap.put("msg", "更新失败，未找到该学生");
            }
            return resultMap;
        } catch (RuntimeException e) {
            // 业务异常，返回具体错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 400);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        } catch (Exception e) {
            // 系统异常，返回通用错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "更新学生失败: " + e.getMessage());
            return resultMap;
        }
    }

    /**
     * 删除学生信息
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> deleteStudent(@PathVariable Integer id) {
        try {
            // 验证学生ID
            if (id == null || id <= 0) {
                throw new RuntimeException("学生ID不能为空且必须大于0");
            }
            
            // 调用服务层删除学生
            int result = studentService.deleteStudent(id);
            
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "删除成功");
            } else {
                resultMap.put("code", 404);
                resultMap.put("msg", "删除失败，未找到该学生");
            }
            return resultMap;
        } catch (RuntimeException e) {
            // 业务异常，返回具体错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 400);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        } catch (Exception e) {
            // 系统异常，返回通用错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "删除学生失败: " + e.getMessage());
            return resultMap;
        }
    }

    /**
     * 批量删除学生
     */
    @PostMapping("/batchDelete")
    public Map<String, Object> batchDeleteStudents(@RequestBody Map<String, Integer[]> map) {
        try {
            // 验证参数
            if (map == null || !map.containsKey("ids")) {
                throw new RuntimeException("请提供要删除的学生ID列表");
            }
            
            Integer[] ids = map.get("ids");
            if (ids == null || ids.length == 0) {
                throw new RuntimeException("删除列表不能为空");
            }
            
            // 验证ID是否有效
            for (Integer id : ids) {
                if (id == null || id <= 0) {
                    throw new RuntimeException("包含无效的学生ID");
                }
            }
            
            // 调用服务层批量删除学生
            int result = studentService.batchDeleteStudents(ids);
            
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "批量删除成功，共删除 " + result + " 条记录");
            } else {
                resultMap.put("code", 404);
                resultMap.put("msg", "批量删除失败，未找到符合条件的学生");
            }
            return resultMap;
        } catch (RuntimeException e) {
            // 业务异常，返回具体错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 400);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        } catch (Exception e) {
            // 系统异常，返回通用错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "批量删除学生失败: " + e.getMessage());
            return resultMap;
        }
    }

    /**
     * 根据班级ID获取学生列表
     */
    @GetMapping("/getByClassId/{classId}")
    public Map<String, Object> getStudentsByClassId(@PathVariable Integer classId) {
        try {
            List<Student> studentList = studentService.findStudentsByClassId(classId);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", studentList);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }
}