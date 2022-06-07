package com.example.admin.service;

import com.usian.model.admin.dtos.SensitiveDto;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.common.dtos.ResponseResult;

public interface AdSensitiveService {
    ResponseResult find(SensitiveDto sensitiveDto);

    ResponseResult add(AdSensitive adSensitive);

    ResponseResult update(AdSensitive adSensitive);

    ResponseResult delete(Integer id);
}
