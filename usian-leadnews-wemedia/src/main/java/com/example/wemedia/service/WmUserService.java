package com.example.wemedia.service;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmUserAddDtos;
import com.usian.model.media.pojos.WmUser;

public interface WmUserService {
    Integer findUserByName(String name);

    WmUser addWmUser(WmUserAddDtos wmUserAddDtos);
}
