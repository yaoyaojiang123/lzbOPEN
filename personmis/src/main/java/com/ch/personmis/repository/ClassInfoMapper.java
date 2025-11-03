package com.ch.personmis.repository;

import com.ch.personmis.entity.ClassInfo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * 班级信息数据访问接口
 */
@Mapper
public interface ClassInfoMapper {
    /**
     * 获取班级列表
     */
    List<ClassInfo> findClassList(Map<String, Object> map);
    
    /**
     * 获取班级总数
     */
    int getClassCount(Map<String, Object> map);
    
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
     * 获取所有班级（用于下拉选择）
     */
    List<ClassInfo> getAllClasses();
    
    /**
     * 更新班级学生数量
     */
    int updateStudentCount(Map<String, Object> map);
}