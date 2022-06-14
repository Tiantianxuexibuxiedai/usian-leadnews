package com.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Mapper
@Component
public interface ApUserMapper extends BaseMapper<ApUser> {
}
