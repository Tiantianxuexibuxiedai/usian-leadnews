package com.usian.model.admin.dtos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class ChannelAddDto {
    /**
     * 频道名称
     */
    @NotBlank(message = "名称不可以为空")
    private String name;

    /**
     * 频道描述
     */

    private String description;

    /**
     * 是否默认频道
     */
    private Boolean isDefault;


    private Boolean status;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdTime;


    private Boolean is_delete;

    private Integer ord;

}
