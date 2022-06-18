package com.example.wemedia.mapper;


import com.usian.model.media.vos.WmNewsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NewsMapper {
    WmNewsVo queryWnNewsVoById(@Param("id") Integer id);
}
