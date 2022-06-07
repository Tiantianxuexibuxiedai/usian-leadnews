package com.example.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.article.pojos.ApAuthor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ApAuthorMapper extends BaseMapper<ApAuthor> {
}
