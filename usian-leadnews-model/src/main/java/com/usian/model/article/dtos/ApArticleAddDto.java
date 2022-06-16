package com.usian.model.article.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ApArticleAddDto {

    /**
     * 作者ID
     */
    private Integer authorId;
    /**
     * 作者名称
     */
    private Integer authorName;

    /**
     * 标题
     */
    private String title;

    /**
     * 图文内容
     */
    private String content;

    /**
     * 文章布局
     * 0 无图文章
     * 1 单图文章
     * 3 多图文章
     */

    private Integer layout;

    /**
     * 图文频道ID
     */
    private Integer channelId;
    /**
     * 图文频道名称
     */
    private String channelName;

    /**
     * 标签
     */
    private String labels;

    /**
     * 定时发布时间，不定时则为空
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;
    /**
     * //图片用逗号分隔
     */
    private String images;

}

