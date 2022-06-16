package com.example.article.service.impl;

import com.example.article.mapper.ApArticleConfigMapper;
import com.example.article.mapper.ApArticleContentMapper;
import com.example.article.mapper.ApArticleMapper;
import com.example.article.service.ApArticleService;
import com.usian.model.article.dtos.ApArticleAddDto;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.article.pojos.ApArticleConfig;
import com.usian.model.article.pojos.ApArticleContent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApArticleServiceImpl implements ApArticleService {
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Override
    public Boolean addArticle(ApArticleAddDto apArticleAddDto) {
        //保存到文章表
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(apArticleAddDto, apArticle);
        apArticle.setCreatedTime(new Date());
        apArticle.setFlag(0);
        apArticleMapper.insert(apArticle);
        //保存到articleConfig文章配置表
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
        apArticleConfig.setIsForward(false);
        apArticleConfig.setComment(false);
        apArticleConfig.setIsDelete(false);
        apArticleConfigMapper.insert(apArticleConfig);
        //保存到文章内容表
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(apArticle.getId());
        apArticleContent.setContent(apArticleAddDto.getContent());
        apArticleContentMapper.insert(apArticleContent);
        return true;
    }
}
