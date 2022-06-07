package com.example.admin.controller;

import com.example.admin.service.AdSensitiveService;
import com.usian.model.admin.dtos.SensitiveDto;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensitive")
public class AdSensitiveController {
    @Autowired
    private AdSensitiveService adSensitiveService;

    /**
     * 查询
     *
     * @param sensitiveDto
     * @return
     */
    @PostMapping("/findAdSensitive")
    public ResponseResult findAdSensitive(@RequestBody SensitiveDto sensitiveDto) {
        return adSensitiveService.find(sensitiveDto);
    }

    /**
     * 添加
     *
     * @param adSensitive
     * @return
     */
    @PostMapping("/addAdSensitive")
    public ResponseResult addAdSensitive(@RequestBody AdSensitive adSensitive) {
        return adSensitiveService.add(adSensitive);
    }

    /**
     * 修改
     *
     * @param adSensitive
     * @return
     */
    @PostMapping("/updateAdSensitive")
    public ResponseResult updateAdSensitive(@RequestBody AdSensitive adSensitive) {
        return adSensitiveService.update(adSensitive);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteAdSensitive")
    public ResponseResult deleteAdSensitive(Integer id) {
        return adSensitiveService.delete(id);
    }
}
