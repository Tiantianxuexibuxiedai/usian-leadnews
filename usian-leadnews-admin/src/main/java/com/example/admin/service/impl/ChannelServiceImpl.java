package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.mapper.ChannelMapper;
import com.example.admin.service.ChannelService;
import com.usian.model.admin.dtos.ChannelAddDto;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {
    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public ResponseResult queryChannelList(ChannelDto channelDto) {
        if (channelDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaQueryWrapper<AdChannel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (channelDto.getName() != null && !channelDto.getName().equals("")) {
            lambdaQueryWrapper.like(AdChannel::getName, channelDto.getName());
        }
        if (channelDto.getStatus() != null) {
            lambdaQueryWrapper.eq(AdChannel::getStatus, channelDto.getStatus());
        }
        //分页
        Page<AdChannel> page = new Page<>(channelDto.getPage(), channelDto.getSize());

        IPage<AdChannel> adChannelPage = channelMapper.selectPage(page, lambdaQueryWrapper);
        log.info("----------.:{}", adChannelPage);
        if (CollectionUtils.isEmpty(adChannelPage.getRecords())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return ResponseResult.okResult(adChannelPage);
    }

    @Override
    public ResponseResult addChannel(@Valid ChannelAddDto channelAddDto) {
        if (channelAddDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        AdChannel adChannel = new AdChannel();
        BeanUtils.copyProperties(channelAddDto, adChannel);
        //adChannel.setCreatedTime(new Date());
        adChannel.setIsDefault(false);
        adChannel.setIsDelete(false);
        int insert = channelMapper.insert(adChannel);
        if (insert > 0) {
            return PageResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        return PageResponseResult.errorResult(AppHttpCodeEnum.FALL);
    }

    @Override
    public ResponseResult updateChannel(ChannelAddDto channelAddDto, Integer id) {
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //查询要修改的数据
        LambdaQueryWrapper<AdChannel> adChannelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adChannelLambdaQueryWrapper.eq(AdChannel::getId, id);
        AdChannel adChannel = channelMapper.selectOne(adChannelLambdaQueryWrapper);
        //对adChannel修改
        adChannel.setStatus(channelAddDto.getStatus());
        adChannel.setIsDelete(channelAddDto.getIs_delete());
        adChannel.setCreatedTime(channelAddDto.getCreatedTime());
        adChannel.setDescription(channelAddDto.getDescription());
        adChannel.setName(channelAddDto.getName());
        adChannel.setOrd(channelAddDto.getOrd());
        adChannel.setIsDefault(channelAddDto.getIsDefault());
        //修改
        channelMapper.updateById(adChannel);
        return PageResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult deleteChannel(Integer id) {
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // channelMapper.deleteById(id);
        LambdaQueryWrapper<AdChannel> adChannelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adChannelLambdaQueryWrapper.eq(AdChannel::getId, id);
        AdChannel adChannel = channelMapper.selectOne(adChannelLambdaQueryWrapper);
        adChannel.setIsDelete(false);
        adChannel.setStatus(false);
        channelMapper.updateById(adChannel);
        return PageResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
