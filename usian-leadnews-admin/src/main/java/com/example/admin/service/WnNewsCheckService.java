package com.example.admin.service;

import com.usian.model.common.dtos.ResponseResult;

public interface WnNewsCheckService {
    Boolean check(Integer id);



    ResponseResult queryNewsVoById(Integer newsId);
}
