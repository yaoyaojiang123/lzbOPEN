package com.ch.personmis.service;

import com.ch.personmis.entity.Score;
import java.util.List;
import java.util.Map;

/**
 * 成绩信息服务接口
 */
public interface ScoreService {
    /**
     * 获取成绩列表
     */
    Map<String, Object> findScoreList(Map<String, Object> map);
    
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
     * 检查成绩是否存在（相同学生相同课程）
     */
    boolean checkScoreExists(Integer studentId, Integer courseId);
    
    /**
     * 根据班级ID获取成绩统计
     */
    List<Map<String, Object>> getScoreStatisticsByClassId(Integer classId);
}