package com.example.admin.feign;

import com.usian.model.article.dtos.ApArticleAddDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "usian-leadnews-article")
public interface ApArticleFeign {
    /**
     * 添加作品信息
     * @param apArticleAddDto
     * @return
     */
    @PostMapping("/api/v1/article/addArticle")
    Boolean addArticle(@RequestBody ApArticleAddDto apArticleAddDto);
}
