package com.example.wemedia.controller;

import com.example.wemedia.feign.AdminFeign;
import com.example.wemedia.service.WmNewsService;
import com.example.wemedia.utils.FastDFSClientUtil;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//文章管理
@RestController
@RequestMapping("/api/v1/wmNews")
public class WmNewsController {
    @Value("${fileServerUrl}")
    private String fileServerUrl;
    @Autowired
    private WmNewsService wmNewsService;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;

    /**
     * 根据参数查询文章列表
     *
     * @param wmNewsPageReqDto
     * @return
     */
    @PostMapping("/queryWmNewsList")
    public ResponseResult queryWmNewsList(@RequestBody WmNewsPageReqDto wmNewsPageReqDto) {
        return wmNewsService.queryWmNewsList(wmNewsPageReqDto);
    }

    /**
     * 查询频道信息
     *
     * @return
     */
    @GetMapping("/queryChannelList")
    public ResponseResult queryChannelList() {
        List<AdChannel> channelList = adminFeign.findAll();
        if (channelList == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
        }
        if (CollectionUtils.isEmpty(channelList)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.okResult(channelList);
    }

    /**
     * 添加文章
     *
     * @param wmNewsDto
     * @return
     */
    @PostMapping("/addWmNews")
    public ResponseResult addWmNews(@RequestBody WmNewsDto wmNewsDto) {
        return wmNewsService.addWmNews(wmNewsDto);
    }

    //图片上传
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file) throws IOException {
        String url = fastDFSClientUtil.uploadFile(file);
        //拼接文件路径
        return ResponseResult.okResult(fileServerUrl + url);
    }
}
