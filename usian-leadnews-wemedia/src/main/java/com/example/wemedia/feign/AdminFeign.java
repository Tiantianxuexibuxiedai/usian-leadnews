package com.example.wemedia.feign;

import com.usian.model.admin.pojos.AdChannel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "usian-leadnews-admin")
public interface AdminFeign {
    @GetMapping("/api/v1/channel/findAllChannel")
    List<AdChannel> findAll();
}
