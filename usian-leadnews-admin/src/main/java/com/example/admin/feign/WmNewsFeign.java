package com.example.admin.feign;

import com.usian.model.media.pojos.WmNews;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "usian-leadnews-wemedia")
public interface WmNewsFeign {
    /**
     * 根据文章id查询文章信息
     *
     * @param id
     */
    @GetMapping("/api/v1/wmNews/queryWnNewsById")
    WmNews queryWnNewsById(@RequestParam Integer id);
    /**
     * 根据文章id修改文章状态
     *
     * @param id
     */
    @GetMapping("/api/v1/wmNews/updateWnNewsById")
    Boolean updateWnNewsById(@RequestParam Integer id,@RequestParam Integer status);
}
