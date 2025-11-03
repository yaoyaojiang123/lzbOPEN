package com.ch.personmis.service;

import com.ch.personmis.entity.ClassInfo;
import java.util.List;
import java.util.Map;

/**
 * 班级信息服务接口
 */
public interface ClassInfoService {
    /**
     * 获取班级列表
     */
    Map<String, Object> findClassList(Map<String, Object> map);
    
    /**
     * 根据ID获取班级信息
     */
    ClassInfo findClassById(Integer id);
    
    /**
     * 添加班级信息
     */
    int addClass(ClassInfo classInfo);
    
    /**
     * 更新班级信息
     */
    int updateClass(ClassInfo classInfo);
    
    /**
     * 删除班级信息
     */
    int deleteClass(Integer id);
    
    /**
     * 获取所有班级
     */
    List<ClassInfo> getAllClasses();
    
    /**
     * 更新班级学生数量
     */
    int updateStudentCount(Integer classId);
}