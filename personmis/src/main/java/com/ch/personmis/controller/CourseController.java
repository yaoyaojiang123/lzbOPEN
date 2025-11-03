package com.ch.personmis.controller;

import com.ch.personmis.entity.Course;
import com.ch.personmis.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 课程信息控制器
 * 负责处理课程相关的HTTP请求
 */

/**
 * 课程信息控制器
 */
@RestController
@RequestMapping("/api/course")
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取课程列表（分页）
     */
    // 验证课程名称
    private static final Pattern COURSE_NAME_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,50}$");
    // 验证教师姓名
    private static final Pattern TEACHER_NAME_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z]{1,20}$");
    
    /**
     * 获取课程列表（分页）
     */
    @PostMapping("/list")
    public Map<String, Object> getCourseList(@RequestBody Map<String, Object> map) {
        try {
            // 验证分页参数
            int page = validatePageParam(map.getOrDefault("page", "1").toString());
            int limit = validateLimitParam(map.getOrDefault("limit", "10").toString());
            map.put("start", (page - 1) * limit);
            map.put("limit", limit);
            
            Map<String, Object> result = courseService.findCourseList(map);
            result.put("code", 0);
            result.put("msg", "success");
            return result;
        } catch (NumberFormatException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 400);
            result.put("msg", "分页参数格式错误");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", "获取课程列表失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 验证页码参数
     */
    private int validatePageParam(String pageStr) {
        int page = Integer.parseInt(pageStr);
        if (page < 1) {
            return 1;
        }
        return page;
    }
    
    /**
     * 验证每页数量参数
     */
    private int validateLimitParam(String limitStr) {
        int limit = Integer.parseInt(limitStr);
        if (limit < 1) {
            return 10;
        }
        if (limit > 100) {
            return 100;
        }
        return limit;
    }

    /**
     * 根据ID获取课程信息
     */
    @GetMapping("/get/{id}")
    public Map<String, Object> getCourseById(@PathVariable Integer id) {
        try {
            Course course = courseService.findCourseById(id);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", course);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }

    /**
     * 更新课程信息
     */
    /**
     * 添加课程
     */
    @PostMapping("/add")
    public Map<String, Object> addCourse(@RequestBody Course course) {
        try {
            // 验证课程信息
            validateCourse(course);
            
            // 检查课程名称是否已存在
            Course existingCourse = courseService.findCourseByName(course.getCourseName());
            if (existingCourse != null) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("code", 400);
                resultMap.put("msg", "课程名称已存在");
                return resultMap;
            }
            
            int result = courseService.addCourse(course);
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "添加成功");
                resultMap.put("data", course);
            } else {
                resultMap.put("code", 500);
                resultMap.put("msg", "添加失败");
            }
            return resultMap;
        } catch (IllegalArgumentException e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 400);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "添加课程失败: " + e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 更新课程
     */
    @PostMapping("/update")
    public Map<String, Object> updateCourse(@RequestBody Course course) {
        try {
            // 验证课程信息
            validateCourse(course);
            
            // 检查课程名称是否被其他课程使用
            Course existingCourse = courseService.findCourseByName(course.getCourseName());
            if (existingCourse != null && !existingCourse.getId().equals(course.getId())) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("code", 400);
                resultMap.put("msg", "课程名称已被其他课程使用");
                return resultMap;
            }
            
            int result = courseService.updateCourse(course);
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "更新成功");
                resultMap.put("data", course);
            } else {
                resultMap.put("code", 500);
                resultMap.put("msg", "更新失败");
            }
            return resultMap;
        } catch (IllegalArgumentException e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 400);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "更新课程失败: " + e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 验证课程信息
     */
    private void validateCourse(Course course) {
        // 验证课程名称
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空");
        }
        if (!COURSE_NAME_PATTERN.matcher(course.getCourseName()).matches()) {
            throw new IllegalArgumentException("课程名称格式不正确，只能包含中文、英文、数字，长度不超过50个字符");
        }
        
        // 验证教师姓名
        if (course.getTeacherName() == null || course.getTeacherName().trim().isEmpty()) {
            throw new IllegalArgumentException("教师姓名不能为空");
        }
        if (!TEACHER_NAME_PATTERN.matcher(course.getTeacherName()).matches()) {
            throw new IllegalArgumentException("教师姓名格式不正确，只能包含中文、英文，长度不超过20个字符");
        }
        
        // 验证学分
        if (course.getCredit() == null || course.getCredit() <= 0 || course.getCredit() > 10) {
            throw new IllegalArgumentException("学分必须在0.5到10之间");
        }
        
        // 验证总课时
        if (course.getTotalHours() == null || course.getTotalHours() <= 0) {
            throw new IllegalArgumentException("总课时必须大于0");
        }
        
        // 验证课程类型
        if (course.getCourseType() == null || course.getCourseType().trim().isEmpty()) {
            throw new IllegalArgumentException("课程类型不能为空");
        }
        
        // 验证适用年级
        if (course.getSuitableGrade() == null || course.getSuitableGrade().trim().isEmpty()) {
            throw new IllegalArgumentException("适用年级不能为空");
        }
    }

    /**
     * 删除课程信息
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> deleteCourse(@PathVariable Integer id) {
        try {
            int result = courseService.deleteCourse(id);
            Map<String, Object> map = new HashMap<>();
            if (result > 0) {
                map.put("code", 0);
                map.put("msg", "删除成功");
            } else {
                map.put("code", 500);
                map.put("msg", "删除失败");
            }
            return map;
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", 500);
            map.put("msg", e.getMessage());
            return map;
        }
    }

    /**
     * 批量删除课程
     */
    /**
     * 批量删除课程
     */
    @PostMapping("/batchDelete")
    public Map<String, Object> batchDeleteCourses(@RequestBody Map<String, Integer[]> map) {
        try {
            Integer[] ids = map.get("ids");
            // 验证参数
            if (ids == null || ids.length == 0) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("code", 400);
                resultMap.put("msg", "请选择要删除的课程");
                return resultMap;
            }
            
            int result = courseService.batchDeleteCourses(ids);
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "删除成功");
                resultMap.put("data", "删除了" + result + "条记录");
            } else {
                resultMap.put("code", 500);
                resultMap.put("msg", "删除失败");
            }
            return resultMap;
        } catch (RuntimeException e) {
            // 处理业务异常
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 400);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "批量删除课程失败: " + e.getMessage());
            return resultMap;
        }
    }

    /**
     * 获取所有课程（用于下拉选择）
     */
    @GetMapping("/getAll")
    public Map<String, Object> getAllCourses() {
        try {
            List<Course> courseList = courseService.getAllCourses();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", courseList);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }

    /**
     * 根据适用年级获取课程列表
     */
    @GetMapping("/getByGrade/{grade}")
    public Map<String, Object> getCoursesByGrade(@PathVariable String grade) {
        try {
            List<Course> courseList = courseService.findCoursesByGrade(grade);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", courseList);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }
}