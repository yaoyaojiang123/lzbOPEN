package com.ch.personmis.service.impl;

import com.ch.personmis.entity.Score;
import com.ch.personmis.repository.ScoreMapper;
import com.ch.personmis.repository.StudentMapper;
import com.ch.personmis.repository.CourseMapper;
import com.ch.personmis.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成绩信息服务实现类
 */
@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Map<String, Object> findScoreList(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        List<Score> scoreList = scoreMapper.findScoreList(map);
        int total = scoreMapper.getScoreCount(map);
        result.put("rows", scoreList);
        result.put("total", total);
        return result;
    }

    @Override
    public Score findScoreById(Integer id) {
        return scoreMapper.findScoreById(id);
    }

    @Transactional
    @Override
    public int addScore(Score score) {
        // 检查学生是否存在
        if (studentMapper.findStudentById(score.getStudentId()) == null) {
            throw new RuntimeException("学生不存在");
        }
        
        // 检查课程是否存在
        if (courseMapper.findCourseById(score.getCourseId()) == null) {
            throw new RuntimeException("课程不存在");
        }
        
        // 检查成绩是否在合理范围内
        if (score.getScore() != null) {
            if (score.getScore().compareTo(BigDecimal.ZERO) < 0 || score.getScore().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new RuntimeException("成绩必须在0-100之间");
            }
        }
        
        // 检查是否已存在该学生该课程的成绩
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", score.getStudentId());
        map.put("courseId", score.getCourseId());
        Score existingScore = scoreMapper.findScoreByStudentAndCourse(map);
        if (existingScore != null) {
            throw new RuntimeException("该学生已有该课程的成绩记录");
        }
        
        return scoreMapper.addScore(score);
    }

    @Override
    public int updateScore(Score score) {
        // 检查成绩是否在合理范围内
        if (score.getScore() != null) {
            if (score.getScore().compareTo(BigDecimal.ZERO) < 0 || score.getScore().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new RuntimeException("成绩必须在0-100之间");
            }
        }
        return scoreMapper.updateScore(score);
    }

    @Override
    public int deleteScore(Integer id) {
        return scoreMapper.deleteScore(id);
    }

    @Override
    public List<Score> findScoresByStudentId(Integer studentId) {
        return scoreMapper.findScoresByStudentId(studentId);
    }

    @Override
    public List<Score> findScoresByCourseId(Integer courseId) {
        return scoreMapper.findScoresByCourseId(courseId);
    }
    
    @Override
    public boolean checkScoreExists(Integer studentId, Integer courseId) {
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", studentId);
        map.put("courseId", courseId);
        Score score = scoreMapper.findScoreByStudentAndCourse(map);
        return score != null;
    }

    @Override
    public int batchDeleteScores(Integer[] ids) {
        return scoreMapper.batchDeleteScores(ids);
    }

    @Override
    public List<Map<String, Object>> getScoreStatisticsByClassId(Integer classId) {
        return scoreMapper.getScoreStatisticsByClassId(classId);
    }
}