package com.ch.personmis.controller;

import com.ch.personmis.entity.ClassInfo;
import com.ch.personmis.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级信息控制器
 */
@RestController
@RequestMapping("/api/classInfo")
@CrossOrigin
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    /**
     * 获取班级列表（分页）
     */
    @PostMapping("/list")
    public Map<String, Object> getClassList(@RequestBody Map<String, Object> map) {
        try {
            // 设置分页参数
            int page = Integer.parseInt(map.getOrDefault("page", "1").toString());
            int limit = Integer.parseInt(map.getOrDefault("limit", "10").toString());
            map.put("start", (page - 1) * limit);
            map.put("limit", limit);
            
            Map<String, Object> result = classInfoService.findClassList(map);
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
     * 根据ID获取班级信息
     */
    @GetMapping("/get/{id}")
    public Map<String, Object> getClassById(@PathVariable Integer id) {
        try {
            ClassInfo classInfo = classInfoService.findClassById(id);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", classInfo);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }

    /**
     * 添加班级信息
     */
    @PostMapping("/add")
    public Map<String, Object> addClass(@RequestBody ClassInfo classInfo) {
        try {
            int result = classInfoService.addClass(classInfo);
            Map<String, Object> map = new HashMap<>();
            if (result > 0) {
                map.put("code", 0);
                map.put("msg", "添加成功");
            } else {
                map.put("code", 500);
                map.put("msg", "添加失败");
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
     * 更新班级信息
     */
    @PostMapping("/update")
    public Map<String, Object> updateClass(@RequestBody ClassInfo classInfo) {
        try {
            int result = classInfoService.updateClass(classInfo);
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
     * 删除班级信息
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> deleteClass(@PathVariable Integer id) {
        try {
            int result = classInfoService.deleteClass(id);
            Map<String, Object> map = new HashMap<>();
            if (result > 0) {
                map.put("code", 0);
                map.put("msg", "删除成功");
            } else {
                map.put("code", 500);
                map.put("msg", "删除失败");
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
     * 获取所有班级（用于下拉选择）
     */
    @GetMapping("/getAll")
    public Map<String, Object> getAllClasses() {
        try {
            List<ClassInfo> classList = classInfoService.getAllClasses();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", classList);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", e.getMessage());
            return result;
        }
    }
}