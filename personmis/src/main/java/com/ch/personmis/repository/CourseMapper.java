package com.ch.personmis.repository;

import com.ch.personmis.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * 课程信息数据访问接口
 */
@Mapper
public interface CourseMapper {
    /**
     * 获取课程列表
     */
    List<Course> findCourseList(Map<String, Object> map);
    
    /**
     * 获取课程总数
     */
    int getCourseCount(Map<String, Object> map);
    
    /**
     * 根据ID获取课程信息
     */
    Course findCourseById(Integer id);
    
    /**
     * 添加课程信息
     */
    int addCourse(Course course);
    
    /**
     * 更新课程信息
     */
    int updateCourse(Course course);
    
    /**
     * 删除课程信息
     */
    int deleteCourse(Integer id);
    
    /**
     * 获取所有课程（用于下拉选择）
     */
    List<Course> getAllCourses();
    
    /**
     * 根据适用年级获取课程列表
     */
    List<Course> findCoursesByGrade(String grade);
    
    /**
     * 批量删除课程
     */
    int batchDeleteCourses(Integer[] ids);
    
    /**
     * 根据课程名称查询课程
     */
    Course findCourseByName(String courseName);
}