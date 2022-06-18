package com.example.article.mapper;

import com.usian.model.article.dtos.ArticleReqDto;
import com.usian.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ArticleMapper {
    List<ApArticle> queryArticleByChannel(ArticleReqDto articleReqDto);
}
