package com.example.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.media.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface WmNewsMaterielMapper extends BaseMapper<WmNewsMaterial>{
}
