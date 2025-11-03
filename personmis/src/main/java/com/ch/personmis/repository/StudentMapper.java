package com.ch.personmis.repository;

import com.ch.personmis.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * 学生信息数据访问接口
 */
@Mapper
public interface StudentMapper {
    /**
     * 获取学生列表
     */
    List<Student> findStudentList(Map<String, Object> map);
    
    /**
     * 获取学生总数
     */
    int getStudentCount(Map<String, Object> map);
    
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