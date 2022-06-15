package com.example.wemedia.service;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.pojos.WmNews;

public interface WmNewsService {
    ResponseResult queryWmNewsList(WmNewsPageReqDto wmNewsPageReqDto);

    WmNews addWmNews(WmNewsDto wmNewsDto);

    WmNews queryWnNewsById(Integer id);

    ResponseResult deleteWnNewsById(Integer id);

    Boolean updateWnNewsById(Integer id, Integer status);
}
