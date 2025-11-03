package com.ch.personmis.service;

import com.ch.personmis.entity.Course;
import java.util.List;
import java.util.Map;

/**
 * 课程信息服务接口
 */
public interface CourseService {
    /**
     * 获取课程列表
     */
    Map<String, Object> findCourseList(Map<String, Object> map);
    
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
     * 获取所有课程
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
     * 根据课程名称查找课程
     */
    Course findCourseByName(String courseName);
}