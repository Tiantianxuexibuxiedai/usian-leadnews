package com.example.article.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.article.mapper.ApAuthorMapper;
import com.example.article.service.ApAuthorService;
import com.usian.model.article.dtos.ApAuthorAddDto;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.media.dtos.WmUserAddDtos;
import com.usian.model.media.pojos.WmUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApAuthorServiceImpl implements ApAuthorService {
    @Autowired
    private ApAuthorMapper apAuthorMapper;

    @Override
    public Integer findUserByName(String name) {
        if (name == null || name.equals("")) {
            return 1;
        }
        LambdaQueryWrapper<ApAuthor> wmUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wmUserLambdaQueryWrapper.eq(ApAuthor::getName, name);
        List<ApAuthor> wmUsers = apAuthorMapper.selectList(wmUserLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(wmUsers)) {
            return 0;
        }
        return 1;
    }

    @Override
    public ApAuthor addApAuthor(ApAuthorAddDto apAuthorAddDto) {
        ApAuthor apAuthor = new ApAuthor();
      BeanUtils.copyProperties(apAuthorAddDto,apAuthor);
        apAuthor.setCreatedTime(new Date());
        int i = apAuthorMapper.insert(apAuthor);
        if (i == 0) {
            return null;
        }
        return apAuthor;
    }
}
