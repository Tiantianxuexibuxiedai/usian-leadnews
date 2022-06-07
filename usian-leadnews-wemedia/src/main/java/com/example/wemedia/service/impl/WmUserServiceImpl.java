package com.example.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.wemedia.mapper.WmUserMapper;
import com.example.wemedia.service.WmUserService;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmUserAddDtos;
import com.usian.model.media.pojos.WmUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class WmUserServiceImpl implements WmUserService {

    @Autowired
    private WmUserMapper wmUserMapper;

    @Override
    public Integer findUserByName(String name) {
        if (name == null || name.equals("")) {
            return 1;
        }
        LambdaQueryWrapper<WmUser> wmUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wmUserLambdaQueryWrapper.eq(WmUser::getName, name);
        List<WmUser> wmUsers = wmUserMapper.selectList(wmUserLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(wmUsers)) {
            return 0;
        }
        return 1;
    }

    @Override
    public WmUser addWmUser(WmUserAddDtos wmUserAddDtos) {
        WmUser wmUser = new WmUser();
        BeanUtils.copyProperties(wmUserAddDtos, wmUser);
        wmUser.setCreatedTime(new Date());
        int i = wmUserMapper.insert(wmUser);
        if (i == 0) {
            return null;
        }
        return wmUser;
    }
}
