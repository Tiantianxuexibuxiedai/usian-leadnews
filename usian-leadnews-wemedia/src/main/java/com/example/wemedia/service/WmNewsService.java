package com.example.wemedia.service;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;

public interface WmNewsService {
    ResponseResult queryWmNewsList(WmNewsPageReqDto wmNewsPageReqDto);

    ResponseResult addWmNews(WmNewsDto wmNewsDto);
}
