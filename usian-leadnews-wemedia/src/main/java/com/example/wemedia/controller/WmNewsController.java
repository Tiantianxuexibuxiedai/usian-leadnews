package com.example.wemedia.controller;

import com.example.wemedia.constants.CommonConstant;
import com.example.wemedia.feign.AdminFeign;
import com.example.wemedia.service.WmNewsService;
import com.example.wemedia.utils.FastDFSClientUtil;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.dtos.WmNewsTitleStatusPageDto;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.vos.WmNewsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//文章管理
@Slf4j
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
    @Autowired
    private KafkaTemplate kafkaTemplate;

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
        WmNews wmNews = wmNewsService.addWmNews(wmNewsDto);
        if (wmNews != null) {
            //发送Kafka消息
            log.info(wmNews.getId().toString());
            kafkaTemplate.send(CommonConstant.WM_NEWS_TOPIC, String.valueOf(wmNews.getId()));
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
    }

    //图片上传
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file) throws IOException {
        String url = fastDFSClientUtil.uploadFile(file);
        //拼接文件路径
        return ResponseResult.okResult(fileServerUrl + url);
    }

    /**
     * 根据文章id查询文章信息
     *
     * @param id
     * @return
     */
    //@GetMapping("/queryWnNewsById/{id}")
    @GetMapping("/queryWnNewsById")
    //路径变量
    //public WmNews queryWnNewsById(@PathVariable Integer id)
    public WmNews queryWnNewsById(@RequestParam Integer id) {
        return wmNewsService.queryWnNewsById(id);
    }

    /**
     * 根据文章id删除文章信息
     *
     * @param id
     */
    @GetMapping("/deleteWnNewsById")
    public ResponseResult deleteWnNewsById(@RequestParam Integer id) {
        return wmNewsService.deleteWnNewsById(id);
    }

    /**
     * 根据文章id修改文章信息
     *
     * @param id
     */
    @GetMapping("/updateWnNewsById")
    public Boolean updateWnNewsById(@RequestParam Integer id, @RequestParam Integer status) {
        return wmNewsService.updateWnNewsById(id, status);
    }

    /**
     * 根据文章状态和标题查询文章列表（人工审核3）
     *
     * @param wmNewsTitleStatusPageDto
     * @param
     * @return
     */
    @PostMapping("/queryWnNewsByParam")
    public List<WmNews> queryWnNewsByParam(@RequestBody WmNewsTitleStatusPageDto wmNewsTitleStatusPageDto) {
        return wmNewsService.queryWnNewsByParam(wmNewsTitleStatusPageDto);
    }

    /**
     * 根据文章id查询文章信息
     *
     * @param id
     * @return
     */
    //@GetMapping("/queryWnNewsById/{id}")
    @GetMapping("/queryWnNewsVoById")
    //路径变量
    //public WmNews queryWnNewsById(@PathVariable Integer id)
    public WmNewsVo queryWnNewsVoById(@RequestParam Integer id) {
        return wmNewsService.queryWnNewsVoById(id);
    }

    /**
     * 根据newsId更新articleId
     * @param
     * @return
     */
    @GetMapping("/updateArticleIdById")
    public int updateArticleIdById(Integer newsId,Long articleId) {
        return wmNewsService.updateArticleIdById(newsId,articleId);
    }

}

