package com.example.article.service;

import com.usian.model.article.dtos.ApArticleAddDto;
import com.usian.model.article.dtos.ArticleReqDto;
import com.usian.model.common.dtos.ResponseResult;

public interface ApArticleService {
    Long addArticle(ApArticleAddDto apArticleAddDto);

    ResponseResult queryArticleByChannel(ArticleReqDto articleReqDto);
}
