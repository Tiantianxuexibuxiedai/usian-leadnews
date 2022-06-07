package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.admin.mapper.AdUserMapper;
import com.example.admin.service.LoginService;
import com.usian.model.admin.dtos.AdUserDto;
import com.usian.model.admin.pojos.AdUser;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.utils.common.AppJwtUtil;
import com.usian.utils.common.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AdUserMapper adUserMapper;

    @Override
    public ResponseResult login(AdUserDto adUserDto) {
        if (StringUtils.isEmpty(adUserDto.getName()) || StringUtils.isEmpty(adUserDto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "用户名或密码不能为空");
        }
        //查询用户信息
        LambdaQueryWrapper<AdUser> adUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adUserLambdaQueryWrapper.eq(AdUser::getName, adUserDto.getName());
        List<AdUser> adUsers = adUserMapper.selectList(adUserLambdaQueryWrapper);
        //判断用户名是否存在
        if (CollectionUtils.isEmpty(adUsers)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        //判断用户名是否重复
        if (adUsers.size() > 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
        }
        //拿到第一个数据
        AdUser adUser = adUsers.get(0);
        //加密盐值
        String pass = MD5Utils.encodeWithSalt(adUserDto.getPassword(), adUser.getSalt());
        //比较数据库密文
        if (pass.equals(adUser.getPassword())) {
            //登陆成功给前台用户信息和token
            HashMap<Object, Object> map = new HashMap<>();
            //用户密码姓名清空
            adUser.setPassword("");
            adUser.setName("");
            //返回用户信息
            map.put("addUser", adUser);
            //返回token
            map.put("token", AppJwtUtil.getToken(Long.valueOf(adUser.getId())));
            return ResponseResult.okResult(map);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
    }
}
