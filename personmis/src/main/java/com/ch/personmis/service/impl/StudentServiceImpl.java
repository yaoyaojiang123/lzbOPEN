package com.ch.personmis.service.impl;

import com.ch.personmis.entity.Student;
import com.ch.personmis.repository.StudentMapper;
import com.ch.personmis.repository.ScoreMapper;
import com.ch.personmis.service.ClassInfoService;
import com.ch.personmis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生信息服务实现类
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private ScoreMapper scoreMapper;
    
    @Autowired
    private ClassInfoService classInfoService;

    @Override
    public Map<String, Object> findStudentList(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        List<Student> studentList = studentMapper.findStudentList(map);
        int total = studentMapper.getStudentCount(map);
        result.put("rows", studentList);
        result.put("total", total);
        return result;
    }

    @Override
    public Student findStudentById(Integer id) {
        return studentMapper.findStudentById(id);
    }

    @Transactional
    @Override
    public int addStudent(Student student) {
        // 检查学生编号是否已存在
        Student existingStudent = studentMapper.findStudentByNo(student.getStudentNo());
        if (existingStudent != null) {
            throw new RuntimeException("学生编号已存在");
        }
        
        int result = studentMapper.addStudent(student);
        // 更新班级学生数量
        classInfoService.updateStudentCount(student.getClassId());
        return result;
    }

    @Transactional
    @Override
    public int updateStudent(Student student) {
        // 获取原班级ID
        Student oldStudent = studentMapper.findStudentById(student.getId());
        Integer oldClassId = oldStudent.getClassId();
        Integer newClassId = student.getClassId();
        
        int result = studentMapper.updateStudent(student);
        
        // 如果班级发生变化，更新两个班级的学生数量
        if (!oldClassId.equals(newClassId)) {
            classInfoService.updateStudentCount(oldClassId);
            classInfoService.updateStudentCount(newClassId);
        }
        
        return result;
    }

    @Transactional
    @Override
    public int deleteStudent(Integer id) {
        // 获取学生信息
        Student student = studentMapper.findStudentById(id);
        Integer classId = student.getClassId();
        
        // 检查是否有成绩记录
        Map<String, Object> scoreMap = new HashMap<>();
        int scoreCount = scoreMapper.getScoreCount(scoreMap);
        if (scoreCount > 0) {
            throw new RuntimeException("该学生有成绩记录，无法删除");
        }
        
        int result = studentMapper.deleteStudent(id);
        // 更新班级学生数量
        classInfoService.updateStudentCount(classId);
        return result;
    }

    @Override
    public List<Student> findStudentsByClassId(Integer classId) {
        return studentMapper.findStudentsByClassId(classId);
    }

    @Transactional
    @Override
    public int batchDeleteStudents(Integer[] ids) {
        // 获取所有要删除的学生的班级ID
        Map<Integer, Integer> classIdMap = new HashMap<>();
        for (Integer id : ids) {
            Student student = studentMapper.findStudentById(id);
            if (student != null) {
                classIdMap.put(student.getClassId(), student.getClassId());
            }
        }
        
        int result = studentMapper.batchDeleteStudents(ids);
        
        // 更新涉及的班级的学生数量
        for (Integer classId : classIdMap.values()) {
            classInfoService.updateStudentCount(classId);
        }
        
        return result;
    }

    @Override
    public Student findStudentByNo(String studentNo) {
        return studentMapper.findStudentByNo(studentNo);
    }
}