package com.usian.model.article.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * APP已发布文章配置表
 * </p>
 *
 * @author itheima
 */


@TableName("ap_article_config")
@Data
public class ApArticleConfig {

    @TableId(value = "id",type = IdType.ID_WORKER)
    private Long id;

    /**
     * 文章id
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * 是否可评论
     */
    @TableField("is_comment")
    private Boolean isComment;

    /**
     * 是否转发
     */
    @TableField("is_forward")
    private Boolean isForward;

    /**
     * 是否下架
     */
    @TableField("is_down")
    private Boolean isDown;

    /**
     * 是否已删除
     */
    @TableField("is_delete")
    private Boolean isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Boolean getComment() {
        return isComment;
    }

    public void setComment(Boolean comment) {
        isComment = comment;
    }

    public Boolean getForward() {
        return isForward;
    }

    public void setForward(Boolean forward) {
        isForward = forward;
    }

    public Boolean getDown() {
        return isDown;
    }

    public void setDown(Boolean down) {
        isDown = down;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }
}