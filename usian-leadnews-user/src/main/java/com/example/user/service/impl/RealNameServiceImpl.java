package com.example.user.service.impl;

import com.example.user.mapper.RealNameMapper;
import com.example.user.service.RealNameService;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealNameServiceImpl implements RealNameService {
    @Autowired
    private RealNameMapper realNameMapper;

    @Override
    public ResponseResult check(AuthDto authDto) {
        //1.修改状态
        //2.调用（wemedia微服务）根据用户名查询
       // 3.调用（wemedia微服务）保存用户信息
        //4.调用（article微服务）根据用户名查询
        //5.调用（article微服务）保存用户信息
        return null;
    }
}
