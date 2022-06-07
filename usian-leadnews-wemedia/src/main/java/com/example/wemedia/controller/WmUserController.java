package com.example.wemedia.controller;

import com.example.wemedia.service.WmUserService;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmUserAddDtos;
import com.usian.model.media.pojos.WmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class WmUserController {
    @Autowired
    private WmUserService wmUserService;

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    @GetMapping("/findUserByName")
    public Integer findUserByName(@RequestParam String name) {
        return wmUserService.findUserByName(name);
    }

    /**
     * 保存用户
     * @param wmUserAddDtos
     * @return
     */
    @PostMapping("/addWmUser")
    public WmUser addWmUser(@RequestBody @Validated WmUserAddDtos wmUserAddDtos){
        return wmUserService.addWmUser(wmUserAddDtos);
    }
}
