package com.usian.model.article.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP文章作者信息表
 * </p>
 *
 * @author itusian
 */
@Data

public class ApAuthorAddDto implements Serializable {



    /**
     * 作者名称
     */
    @NotBlank(message = "用户名不可以为空")
    private String name;

    /**
     * 0 爬取数据
     1 签约合作商
     2 平台自媒体人

     */

    private Integer type;

    /**
     * 社交账号ID
     */

    private Integer userId;

    /**
     * 创建时间
     */

    private Date createdTime;

    /**
     * 自媒体账号
     */

    private Integer wmUserId;

}