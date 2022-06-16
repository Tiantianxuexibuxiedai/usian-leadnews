package com.example.article.controller;

import com.example.article.service.ApArticleService;
import com.usian.model.article.dtos.ApArticleAddDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
public class ApArticleController {
    @Autowired
    private ApArticleService apArticleService;

    /**
     * 添加作品信息
     * @param apArticleAddDto
     * @return
     */
    @PostMapping("/addArticle")
    public Boolean addArticle(@RequestBody ApArticleAddDto apArticleAddDto) {
        return apArticleService.addArticle(apArticleAddDto);

    }

}
