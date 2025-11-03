package com.ch.personmis.service.impl;

import com.ch.personmis.entity.ClassInfo;
import com.ch.personmis.repository.ClassInfoMapper;
import com.ch.personmis.repository.StudentMapper;
import com.ch.personmis.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级信息服务实现类
 */
@Service
public class ClassInfoServiceImpl implements ClassInfoService {

    @Autowired
    private ClassInfoMapper classInfoMapper;
    
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Map<String, Object> findClassList(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        List<ClassInfo> classList = classInfoMapper.findClassList(map);
        int total = classInfoMapper.getClassCount(map);
        result.put("rows", classList);
        result.put("total", total);
        return result;
    }

    @Override
    public ClassInfo findClassById(Integer id) {
        return classInfoMapper.findClassById(id);
    }

    @Transactional
    @Override
    public int addClass(ClassInfo classInfo) {
        // 设置默认学生数量为0
        if (classInfo.getStudentCount() == null) {
            classInfo.setStudentCount(0);
        }
        return classInfoMapper.addClass(classInfo);
    }

    @Override
    public int updateClass(ClassInfo classInfo) {
        return classInfoMapper.updateClass(classInfo);
    }

    @Transactional
    @Override
    public int deleteClass(Integer id) {
        // 检查班级是否有学生
        Map<String, Object> map = new HashMap<>();
        map.put("classId", id);
        int studentCount = studentMapper.getStudentCount(map);
        if (studentCount > 0) {
            throw new RuntimeException("该班级下还有学生，无法删除");
        }
        return classInfoMapper.deleteClass(id);
    }

    @Override
    public List<ClassInfo> getAllClasses() {
        return classInfoMapper.getAllClasses();
    }

    @Override
    public int updateStudentCount(Integer classId) {
        Map<String, Object> map = new HashMap<>();
        map.put("classId", classId);
        int studentCount = studentMapper.getStudentCount(map);
        map.put("studentCount", studentCount);
        return classInfoMapper.updateStudentCount(map);
    }
}