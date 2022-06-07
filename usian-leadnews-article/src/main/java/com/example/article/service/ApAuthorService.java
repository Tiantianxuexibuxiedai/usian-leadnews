package com.example.article.service;

import com.usian.model.article.dtos.ApAuthorAddDto;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.media.dtos.WmUserAddDtos;
import com.usian.model.media.pojos.WmUser;

public interface ApAuthorService {
    Integer findUserByName(String name);

    ApAuthor addApAuthor(ApAuthorAddDto apAuthorAddDto);
}
