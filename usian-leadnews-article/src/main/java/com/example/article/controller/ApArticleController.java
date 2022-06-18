package com.example.article.controller;

import com.example.article.service.ApArticleService;
import com.usian.model.article.dtos.ApArticleAddDto;
import com.usian.model.article.dtos.ArticleReqDto;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ApArticleController {
    @Autowired
    private ApArticleService apArticleService;

    /**
     * 添加作品信息
     *
     * @param apArticleAddDto
     * @return
     */
    @PostMapping("/addArticle")
    public Long addArticle(@RequestBody ApArticleAddDto apArticleAddDto) {
        return apArticleService.addArticle(apArticleAddDto);

    }

    /**app端列表展示
     * 更新文章信息
     * @param articleReqDto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult queryArticleByChannel(@RequestBody ArticleReqDto articleReqDto) {
        return apArticleService.queryArticleByChannel(articleReqDto);
    }
}
