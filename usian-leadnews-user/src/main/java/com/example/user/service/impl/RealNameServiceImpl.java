package com.example.user.service.impl;

import com.example.user.feign.ArticleFeign;
import com.example.user.feign.WemediaFeign;
import com.example.user.mapper.ApUserMapper;
import com.example.user.mapper.RealNameMapper;
import com.example.user.service.RealNameService;
import com.usian.model.article.dtos.ApAuthorAddDto;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmUserAddDtos;
import com.usian.model.media.pojos.WmUser;
import com.usian.model.user.dtos.AuthDto;
import com.usian.model.user.pojos.ApUser;
import com.usian.model.user.pojos.ApUserRealname;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class RealNameServiceImpl implements RealNameService {
    @Autowired
    private RealNameMapper realNameMapper;
    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private WemediaFeign wemediaFeign;

    @Override
    @GlobalTransactional
    public ResponseResult check(AuthDto authDto) {
        if (authDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        /*//1.修改状态
        LambdaQueryWrapper<ApUserRealname> queryWrapper = new LambdaQueryWrapper<>();
        if (authDto.getStatus()!=null){
            queryWrapper.eq(ApUserRealname::getStatus, authDto.getStatus());
            queryWrapper.eq(ApUserRealname::getId, authDto.getId());
        }
        ApUserRealname userRealname = realNameMapper.selectOne(queryWrapper);
        userRealname.setStatus(authDto.getStatus());
        //2.调用（wemedia微服务）根据用户名查询
        Integer userByName = wemediaFeign.findUserByName(userRealname.getName());
        if (userByName == 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 3.调用（wemedia微服务）保存用户信息
        WmUserAddDtos wmMaterialaddDtos = new WmUserAddDtos();
        BeanUtils.copyProperties(userByName, wmMaterialaddDtos);
        wemediaFeign.addWmUser(wmMaterialaddDtos);
        //4.调用（article微服务）根据用户名查询
        Integer userByName1 = articleFeign.findUserByName(userRealname.getName());
        if (userByName1 == 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //5.调用（article微服务）保存用户信息
        ApAuthorAddDto apAuthorAddDto = new ApAuthorAddDto();
        BeanUtils.copyProperties(userByName, apAuthorAddDto);
        articleFeign.addApAuthor(apAuthorAddDto);
        return ResponseResult.okResult(userByName);*/
        ApUserRealname apUserRealname = new ApUserRealname();
        if (authDto.getStatus() == 9) {
            //审核通过查询这条数据
            ApUserRealname realname = realNameMapper.selectById(authDto.getId());
            //判断
            if (realname == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
            if (realname.getId() == null) {
                log.info("userId不存在");
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
            ApUser apUser = apUserMapper.selectById(realname.getId());
            if (apUser == null) {
                log.info("appUser不存在");
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
            //2.调用（wemedia微服务）根据用户名查询
            Integer i = wemediaFeign.findUserByName(apUser.getName());
            if (i == 0) {
                //用户不存在
                WmUserAddDtos wmUserAddDtos = new WmUserAddDtos();
                //属性赋值将前者值赋到后者中
                BeanUtils.copyProperties(apUser, wmUserAddDtos);
                // 3.调用（wemedia微服务）保存用户信息
                WmUser wmUser = wemediaFeign.addWmUser(wmUserAddDtos);
                if (wmUser == null) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
                }
             //   int a = 1 / 0;//事务回滚报错用
                //4.调用（article微服务）根据用户名查询
                Integer n = articleFeign.findUserByName(apUser.getName());
                if (n == 0) {
                    //5.调用（article微服务）保存用户信息
                    ApAuthorAddDto apAuthorAddDto = new ApAuthorAddDto();
                    BeanUtils.copyProperties(apUser, apAuthorAddDto);
                    apAuthorAddDto.setName(apUser.getName());
                    apAuthorAddDto.setType(2);
                    apAuthorAddDto.setWmUserId(wmUser.getId());
                    ApAuthor apAuthor = articleFeign.addApAuthor(apAuthorAddDto);
                }
            }
        } else {
            //审核不通过写入拒绝原因
            apUserRealname.setReason(authDto.getMsg());
        }

        apUserRealname.setId(authDto.getId());
        apUserRealname.setStatus(authDto.getStatus());
        realNameMapper.updateById(apUserRealname);
        return ResponseResult.okResult(null);
    }
}
