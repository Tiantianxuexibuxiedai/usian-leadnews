package com.example.wemedia.service;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.dtos.WmNewsTitleStatusPageDto;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.vos.WmNewsVo;

import java.util.List;

public interface WmNewsService {
    ResponseResult queryWmNewsList(WmNewsPageReqDto wmNewsPageReqDto);

    WmNews addWmNews(WmNewsDto wmNewsDto);

    WmNews queryWnNewsById(Integer id);

    ResponseResult deleteWnNewsById(Integer id);

    Boolean updateWnNewsById(Integer id, Integer status);

    List<WmNews> queryWnNewsByParam(WmNewsTitleStatusPageDto wmNewsTitleStatusPageDto);

    WmNewsVo queryWnNewsVoById(Integer id);


    int updateArticleIdById(Integer newsId, Long articleId);
}
