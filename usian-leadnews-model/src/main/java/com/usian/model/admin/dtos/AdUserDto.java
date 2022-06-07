package com.usian.model.admin.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdUserDto {
    /**
     * 用户名
     */
    @NotBlank(message = "名称不可以为空")
    private String name;

    /**
     * 密码
     */
    @NotBlank(message = "密码不可以为空")
    private String password;
}
