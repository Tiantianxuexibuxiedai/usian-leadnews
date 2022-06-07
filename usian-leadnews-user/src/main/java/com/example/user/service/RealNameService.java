package com.example.user.service;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;

public interface RealNameService {
    ResponseResult check(AuthDto authDto);
}
