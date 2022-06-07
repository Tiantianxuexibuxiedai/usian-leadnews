package com.example.user.controller;

import com.example.user.service.RealNameService;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import com.usian.model.user.dtos.UserRelationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class RealNameController {
    @Autowired
    private RealNameService realNameService;

    /**
     * 实名认证审核
     * @param authDto
     * @return
     */
    @PostMapping("/realnameCheck")
    public ResponseResult check(@RequestBody AuthDto authDto) {
        return realNameService.check(authDto);
    }
}
