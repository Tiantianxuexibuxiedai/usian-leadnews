package com.example.user.feign;

import com.usian.model.article.dtos.ApAuthorAddDto;
import com.usian.model.article.pojos.ApAuthor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "usian-leadnews-article")
public interface ArticleFeign {

    @GetMapping("/api/v1/author/findUserByName")
    Integer findUserByName(@RequestParam String name);

    @PostMapping("/api/v1/author/addApAuthor")
    ApAuthor addApAuthor(@RequestBody @Validated ApAuthorAddDto apAuthorAddDto);
}
