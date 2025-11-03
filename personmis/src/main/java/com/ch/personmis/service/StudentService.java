package com.ch.personmis.service;

import com.ch.personmis.entity.Student;
import java.util.List;
import java.util.Map;

/**
 * 学生信息服务接口
 */
public interface StudentService {
    /**
     * 获取学生列表
     */
    Map<String, Object> findStudentList(Map<String, Object> map);
    
    /**
     * 根据ID获取学生信息
     */
    Student findStudentById(Integer id);
    
    /**
     * 添加学生信息
     */
    int addStudent(Student student);
    
    /**
     * 更新学生信息
     */
    int updateStudent(Student student);
    
    /**
     * 删除学生信息
     */
    int deleteStudent(Integer id);
    
    /**
     * 根据班级ID获取学生列表
     */
    List<Student> findStudentsByClassId(Integer classId);
    
    /**
     * 批量删除学生
     */
    int batchDeleteStudents(Integer[] ids);
    
    /**
     * 根据学生编号查询学生
     */
    Student findStudentByNo(String studentNo);
}