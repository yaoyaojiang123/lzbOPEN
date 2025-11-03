package com.ch.personmis.controller;

import com.ch.personmis.entity.UserEntity;
import com.ch.personmis.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
public class AdminController {
    @Resource
    private AdminService adminService;
    
    // 登录接口 - 上下文路径已配置为/classmis，最终路径为/classmis/login
    @PostMapping(value = "/login")
    public String login(@RequestBody UserEntity userEntity, HttpSession session) {
        return adminService.login(userEntity, session);
    }
}
