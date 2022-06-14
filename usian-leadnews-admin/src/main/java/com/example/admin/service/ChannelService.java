package com.example.admin.service;

import com.usian.model.admin.dtos.ChannelAddDto;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.ResponseResult;

import java.util.List;

public interface ChannelService {
    ResponseResult queryChannelList(ChannelDto channelDto);

    ResponseResult addChannel(ChannelAddDto channelAddDto);

    ResponseResult updateChannel(ChannelAddDto channelAddDto,Integer id);

    ResponseResult deleteChannel(Integer id);

    List<AdChannel> findAll();
}
