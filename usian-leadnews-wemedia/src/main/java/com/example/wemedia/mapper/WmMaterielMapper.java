package com.example.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.media.pojos.WmMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface WmMaterielMapper extends BaseMapper<WmMaterial> {
}
