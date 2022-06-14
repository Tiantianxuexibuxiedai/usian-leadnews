package com.example.user.feign;

import com.usian.model.media.dtos.WmUserAddDtos;
import com.usian.model.media.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "usian-leadnews-wemedia")
public interface WemediaFeign {
    @GetMapping("/api/v1/user/findUserByName")
    Integer findUserByName(@RequestParam String name);

    @PostMapping("/api/v1/user/addWmUser")
    WmUser addWmUser(@RequestBody @Validated WmUserAddDtos wmUserAddDtos);
}
