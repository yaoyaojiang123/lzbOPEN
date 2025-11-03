package com.ch.personmis.service.impl;

import com.ch.personmis.entity.Course;
import com.ch.personmis.repository.CourseMapper;
import com.ch.personmis.repository.ScoreMapper;
import com.ch.personmis.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程信息服务实现类
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public Map<String, Object> findCourseList(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        List<Course> courseList = courseMapper.findCourseList(map);
        int total = courseMapper.getCourseCount(map);
        result.put("rows", courseList);
        result.put("total", total);
        return result;
    }

    @Override
    public Course findCourseById(Integer id) {
        return courseMapper.findCourseById(id);
    }

    @Transactional
    @Override
    public int addCourse(Course course) {
        // 检查课程名称是否已存在
        Course existingCourse = courseMapper.findCourseByName(course.getCourseName());
        if (existingCourse != null) {
            throw new RuntimeException("课程名称已存在");
        }
        return courseMapper.addCourse(course);
    }

    @Override
    public int updateCourse(Course course) {
        return courseMapper.updateCourse(course);
    }

    @Transactional
    @Override
    public int deleteCourse(Integer id) {
        // 检查是否有成绩记录
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", id);
        int scoreCount = scoreMapper.getScoreCount(map);
        if (scoreCount > 0) {
            throw new RuntimeException("该课程有成绩记录，无法删除");
        }
        return courseMapper.deleteCourse(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.getAllCourses();
    }

    @Override
    public List<Course> findCoursesByGrade(String grade) {
        return courseMapper.findCoursesByGrade(grade);
    }
    


    @Transactional
    @Override
    public int batchDeleteCourses(Integer[] ids) {
        // 检查是否有成绩记录
        for (Integer id : ids) {
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", id);
            int scoreCount = scoreMapper.getScoreCount(map);
            if (scoreCount > 0) {
                throw new RuntimeException("部分课程有成绩记录，无法删除");
            }
        }
        return courseMapper.batchDeleteCourses(ids);
    }

    @Override
    public Course findCourseByName(String courseName) {
        return courseMapper.findCourseByName(courseName);
    }
}