package com.example.admin.controller;

import com.example.admin.service.ChannelService;
import com.usian.model.admin.dtos.ChannelAddDto;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    /**
     * 查询频道列表
     *
     * @param channelDto
     * @return
     */
    @PostMapping("/queryChannelList")
    public ResponseResult queryChannelList(@RequestBody ChannelDto channelDto) {
        return channelService.queryChannelList(channelDto);
    }

    /**
     * 添加
     */
    @PostMapping("/addChannel")
    public ResponseResult addChannel(@RequestBody @Validated ChannelAddDto channelAddDto) {
        return channelService.addChannel(channelAddDto);
    }

    /**
     * 修改
     */
    @PostMapping("/updateChannel")
    public ResponseResult updateChannel(@RequestBody ChannelAddDto channelAddDto, Integer id) {
        return channelService.updateChannel(channelAddDto, id);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteChannel")
    public ResponseResult deleteChannel(Integer id) {
        return channelService.deleteChannel(id);
    }
    /**
     * 查询全部频道
     */
    @GetMapping("/findAllChannel")
    public List<AdChannel> findAll(){
        return channelService.findAll();
    }
}
