package com.usian.model.article.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ap_article")
public class ApArticle {

    @TableId(value = "id",type = IdType.ID_WORKER)
    private Long id;
    private String title;
    private Integer authorId;//文章作者的ID
    private String authorName;
    private Integer channelId;
    private String channelName       ;
    private String layout;
    private Integer flag;
    private String images;
    private String labels;
    private Integer likes;
    private Integer collection;
    private Integer comment;
    private Integer views;
    private Integer provinceId;
    private Integer cityId;
    private Integer countyId;
    private Date createdTime;
    private Date publishTime;
    private Integer syncStatus;
    private Integer origin;
}

