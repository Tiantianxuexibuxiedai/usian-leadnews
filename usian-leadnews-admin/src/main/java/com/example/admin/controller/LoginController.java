package com.example.admin.controller;

import com.example.admin.service.LoginService;
import com.usian.model.admin.dtos.AdUserDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoginController {
    @Autowired
    private LoginService loginService;
    /**
     * admin登录功能
     * @param
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody @Validated AdUserDto adUserDto) {
        return loginService.login(adUserDto);
    }

}
