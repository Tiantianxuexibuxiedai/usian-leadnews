package com.example.admin.service;

import com.usian.model.admin.dtos.AdUserDto;
import com.usian.model.common.dtos.ResponseResult;

public interface LoginService {
    ResponseResult login(AdUserDto adUserDto);
}
