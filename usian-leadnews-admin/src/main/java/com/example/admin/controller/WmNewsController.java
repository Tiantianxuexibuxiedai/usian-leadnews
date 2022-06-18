package com.example.admin.controller;

import com.example.admin.constants.CommonConstant;
import com.example.admin.feign.WmNewsFeign;
import com.example.admin.service.WnNewsCheckService;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmNewsTitleStatusPageDto;
import com.usian.model.media.pojos.WmNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manual")
public class WmNewsController {
    @Autowired
    private WmNewsFeign wmNewsFeign;
    @Autowired
    private WnNewsCheckService wnNewsCheckService;

    /**
     * 查询需要人工审核的信息
     *
     * @param wmNewsTitleStatusPageDto
     * @return
     */
    @PostMapping("/queryWmNewsByManualReview")
    public ResponseResult queryWmNewsByManualReview(@RequestBody WmNewsTitleStatusPageDto wmNewsTitleStatusPageDto) {
        List<WmNews> wmNewsList = wmNewsFeign.queryWnNewsByParam(wmNewsTitleStatusPageDto);
        if (CollectionUtils.isEmpty(wmNewsList)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.okResult(wmNewsList);
    }

    /**
     * 人工审核
     *
     * @return
     */
    @GetMapping("/auditing")
    public ResponseResult auditing(Integer newsId, Integer result) {
        if (result != 0 && result != 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (result == 0) {
            //驳回
            wmNewsFeign.updateWnNewsById(newsId, CommonConstant.AUDITING_FALL);
        }
        if (result == 1) {
            //通过
            wmNewsFeign.updateWnNewsById(newsId, CommonConstant.AUDITING_SUCCESS);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 人工审核
     * 查看详情
     *
     * @return
     */
    @GetMapping("/queryNewsVoById")
    public ResponseResult queryNewsVoById(Integer newsId) {
        return wnNewsCheckService.queryNewsVoById(newsId);
    }
}
