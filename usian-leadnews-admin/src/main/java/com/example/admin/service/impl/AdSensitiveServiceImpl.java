package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.mapper.AdSensitiveMapper;
import com.example.admin.service.AdSensitiveService;
import com.usian.model.admin.dtos.SensitiveDto;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class AdSensitiveServiceImpl implements AdSensitiveService {
    @Autowired
    private AdSensitiveMapper adSensitiveMapper;

    @Override
    public ResponseResult find(SensitiveDto sensitiveDto) {
        //1.检查参数
        if (sensitiveDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.根据名称模糊分页查询
        Page<AdSensitive> page = new Page<>(sensitiveDto.getPage(), sensitiveDto.getSize());
        LambdaQueryWrapper<AdSensitive> adSensitiveLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判空
        if (StringUtils.isNotBlank(sensitiveDto.getName())) {
            adSensitiveLambdaQueryWrapper.like(AdSensitive::getSensitives, sensitiveDto.getName());
        }
        IPage<AdSensitive> result = adSensitiveMapper.selectPage(page, adSensitiveLambdaQueryWrapper);

        //3.结果返回
        log.info("-------------->:{}",result);
        log.info("-------------->:{}",result.getRecords());
        if (CollectionUtils.isEmpty(result.getRecords())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult add(AdSensitive adSensitive) {
        if (adSensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        adSensitiveMapper.insert(adSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult update(AdSensitive adSensitive) {
        //1.参数检查
        if (adSensitive == null || adSensitive.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.修改
        adSensitiveMapper.updateById(adSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult delete(Integer id) {
        //1.参数检查
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.判断当前敏感词是否存在
        AdSensitive adSensitive = adSensitiveMapper.selectById(id);
        if (adSensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //3.删除操作
        adSensitiveMapper.deleteById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
