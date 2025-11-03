package com.ch.personmis.repository;

import com.ch.personmis.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * 成绩信息数据访问接口
 */
@Mapper
public interface ScoreMapper {
    /**
     * 获取成绩列表
     */
    List<Score> findScoreList(Map<String, Object> map);
    
    /**
     * 获取成绩总数
     */
    int getScoreCount(Map<String, Object> map);
    
    /**
     * 根据ID获取成绩信息
     */
    Score findScoreById(Integer id);
    
    /**
     * 添加成绩信息
     */
    int addScore(Score score);
    
    /**
     * 更新成绩信息
     */
    int updateScore(Score score);
    
    /**
     * 删除成绩信息
     */
    int deleteScore(Integer id);
    
    /**
     * 根据学生ID获取成绩列表
     */
    List<Score> findScoresByStudentId(Integer studentId);
    
    /**
     * 根据课程ID获取成绩列表
     */
    List<Score> findScoresByCourseId(Integer courseId);
    
    /**
     * 批量删除成绩
     */
    int batchDeleteScores(Integer[] ids);
    
    /**
     * 查询学生某门课程的成绩
     */
    Score findScoreByStudentAndCourse(Map<String, Object> map);
    
    /**
     * 根据班级ID获取成绩统计
     */
    List<Map<String, Object>> getScoreStatisticsByClassId(Integer classId);
}