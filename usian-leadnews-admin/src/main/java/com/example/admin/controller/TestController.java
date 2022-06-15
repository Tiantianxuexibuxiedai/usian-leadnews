package com.example.admin.controller;

import com.example.admin.pojo.RequestParam;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

public class TestController {

    public ResponseResult test(@RequestBody RequestParam requestParam){
        return null;
    }
}
