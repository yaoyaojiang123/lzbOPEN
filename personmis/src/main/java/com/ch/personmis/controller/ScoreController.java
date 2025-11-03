package com.ch.personmis.controller;

import com.ch.personmis.entity.Score;
import com.ch.personmis.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成绩信息控制器
 */
@RestController
@RequestMapping("/api/score")
@CrossOrigin
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    /**
     * 获取成绩列表（分页）
     */
    @PostMapping("/list")
    public Map<String, Object> getScoreList(@RequestBody Map<String, Object> map) {
        try {
            // 设置分页参数
            int page = Integer.parseInt(map.getOrDefault("page", "1").toString());
            int limit = Integer.parseInt(map.getOrDefault("limit", "10").toString());
            map.put("start", (page - 1) * limit);
            map.put("limit", limit);
            
            Map<String, Object> result = scoreService.findScoreList(map);
            result.put("code", 0);
            result.put("msg", "success");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }

    /**
     * 根据ID获取成绩信息
     */
    @GetMapping("/get/{id}")
    public Map<String, Object> getScoreById(@PathVariable Integer id) {
        try {
            Score score = scoreService.findScoreById(id);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", score);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }

    /**
     * 检查成绩是否存在（相同学生相同课程）
     */
    @GetMapping("/checkExists/{studentId}/{courseId}")
    public Map<String, Object> checkScoreExists(@PathVariable Integer studentId, @PathVariable Integer courseId) {
        try {
            boolean exists = scoreService.checkScoreExists(studentId, courseId);
            Map<String, Object> result = new HashMap<>();
            result.put("exists", exists);
            result.put("code", 0);
            result.put("msg", "success");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("exists", false);
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    /**
     * 添加成绩信息
     */
    @PostMapping("/add")
    public Map<String, Object> addScore(@RequestBody Score score) {
        try {
            int result = scoreService.addScore(score);
            Map<String, Object> map = new HashMap<>();
            if (result > 0) {
                map.put("code", 0);
                map.put("msg", "添加成功");
                map.put("success", true);
            } else {
                map.put("code", 500);
                map.put("msg", "添加失败");
                map.put("success", false);
            }
            return map;
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", 500);
            map.put("msg", e.getMessage());
            map.put("success", false);
            return map;
        }
    }

    /**
     * 更新成绩信息
     */
    @PostMapping("/update")
    public Map<String, Object> updateScore(@RequestBody Score score) {
        try {
            int result = scoreService.updateScore(score);
            Map<String, Object> map = new HashMap<>();
            if (result > 0) {
                map.put("code", 0);
                map.put("msg", "更新成功");
            } else {
                map.put("code", 500);
                map.put("msg", "更新失败");
            }
            return map;
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", 500);
            map.put("msg", e.getMessage());
            return map;
        }
    }

    /**
     * 删除成绩信息
     */
    /**
     * 删除成绩
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> deleteScore(@PathVariable Integer id) {
        try {
            // 验证ID参数
            if (id == null || id <= 0) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("code", 400);
                resultMap.put("msg", "成绩ID无效");
                return resultMap;
            }
            
            int result = scoreService.deleteScore(id);
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "删除成功");
            } else {
                resultMap.put("code", 500);
                resultMap.put("msg", "删除失败");
            }
            return resultMap;
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "删除成绩失败: " + e.getMessage());
            return resultMap;
        }
    }

    /**
     * 批量删除成绩
     */
    @PostMapping("/batchDelete")
    public Map<String, Object> batchDeleteScores(@RequestBody Map<String, Integer[]> map) {
        try {
            Integer[] ids = map.get("ids");
            // 验证参数
            if (ids == null || ids.length == 0) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("code", 400);
                resultMap.put("msg", "请选择要删除的成绩");
                return resultMap;
            }
            
            // 验证ID有效性
            for (Integer id : ids) {
                if (id == null || id <= 0) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("code", 400);
                    resultMap.put("msg", "成绩ID无效");
                    return resultMap;
                }
            }
            
            int result = scoreService.batchDeleteScores(ids);
            Map<String, Object> resultMap = new HashMap<>();
            if (result > 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", "删除成功");
                resultMap.put("data", "删除了" + result + "条记录");
            } else {
                resultMap.put("code", 500);
                resultMap.put("msg", "删除失败");
            }
            return resultMap;
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 500);
            resultMap.put("msg", "批量删除成绩失败: " + e.getMessage());
            return resultMap;
        }
    }

    /**
     * 根据学生ID获取成绩列表
     */
    @GetMapping("/getByStudentId/{studentId}")
    public Map<String, Object> getScoresByStudentId(@PathVariable Integer studentId) {
        try {
            // 验证学生ID
            if (studentId == null || studentId <= 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 400);
                result.put("msg", "学生ID无效");
                return result;
            }
            
            List<Score> scoreList = scoreService.findScoresByStudentId(studentId);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", scoreList);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", "查询学生成绩失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 根据课程ID获取成绩列表
     */
    @GetMapping("/getByCourseId/{courseId}")
    public Map<String, Object> getScoresByCourseId(@PathVariable Integer courseId) {
        try {
            List<Score> scoreList = scoreService.findScoresByCourseId(courseId);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", scoreList);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }

    /**
     * 根据班级ID获取成绩统计
     */
    @GetMapping("/getStatisticsByClassId/{classId}")
    public Map<String, Object> getScoreStatisticsByClassId(@PathVariable Integer classId) {
        try {
            List<Map<String, Object>> statistics = scoreService.getScoreStatisticsByClassId(classId);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", statistics);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }
}