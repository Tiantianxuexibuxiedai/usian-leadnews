package com.example.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.article.pojos.ApArticleContent;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ApArticleContentMapper extends BaseMapper<ApArticleContent> {
}
