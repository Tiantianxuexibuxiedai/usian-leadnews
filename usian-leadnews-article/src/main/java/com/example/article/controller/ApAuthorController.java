package com.example.article.controller;

import com.example.article.service.ApAuthorService;
import com.usian.model.article.dtos.ApAuthorAddDto;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.media.dtos.WmUserAddDtos;
import com.usian.model.media.pojos.WmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/author")
public class ApAuthorController {
    @Autowired
    private ApAuthorService apAuthorService;


    /**
     * 根据名称查询
     * @param name
     * @return
     */
    @GetMapping("/findUserByName")
    public Integer findUserByName(@RequestParam String name) {

        return apAuthorService.findUserByName(name);
    }

    /**
     * 保存用户
     * @param apAuthorAddDto
     * @return
     */
    @PostMapping("/addApAuthor")
    public ApAuthor addApAuthor(@RequestBody @Validated ApAuthorAddDto apAuthorAddDto){
        return apAuthorService.addApAuthor(apAuthorAddDto);
    }
}
