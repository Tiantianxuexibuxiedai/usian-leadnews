package com.example.admin.feign;

import com.usian.model.media.dtos.WmNewsTitleStatusPageDto;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.vos.WmNewsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    Boolean updateWnNewsById(@RequestParam Integer id, @RequestParam Integer status);

    /**
     * 根据文章状态和标题查询文章列表（人工审核3）
     * 根据状态3 查询待人工审核的文章
     *
     * @param wmNewsTitleStatusPageDto
     * @param
     * @return
     */
    @PostMapping("/api/v1/wmNews/queryWnNewsByParam")
    List<WmNews> queryWnNewsByParam(@RequestBody WmNewsTitleStatusPageDto wmNewsTitleStatusPageDto);
    /**
     * 根据文章id查询文章信息
     *
     * @param id
     * @return
     */

    @GetMapping("/api/v1/wmNews/queryWnNewsVoById")
    WmNewsVo queryWnNewsVoById(@RequestParam Integer id);
    /**
     * 根据newsId更新articleId
     * @param
     * @return
     */
    @GetMapping("/api/v1/wmNews/updateArticleIdById")
    public int updateArticleIdById(Integer newsId,Long articleId);
}
