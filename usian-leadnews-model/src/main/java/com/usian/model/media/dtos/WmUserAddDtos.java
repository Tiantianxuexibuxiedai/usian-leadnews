package com.usian.model.media.dtos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 自媒体用户信息表
 * </p>
 *
 * @author itusian
 */
@Data
public class WmUserAddDtos implements Serializable {


    private Integer apUserId;


    /**
     * 登录用户名
     */
    @NotBlank(message = "用户名不可以为空")
    private String name;

    /**
     * 登录密码
     */

    private String password;

    /**
     * 盐
     */

    private String salt;

    /**
     * 昵称
     */

    private String nickname;

    /**
     * 头像
     */

    private String image;

    /**
     * 归属地
     */

    private String location;

    /**
     * 手机号
     */

    private String phone;

    /**
     * 状态
     * 0 暂时不可用
     * 1 永久不可用
     * 9 正常可用
     */

    private Integer status;

    /**
     * 邮箱
     */

    private String email;

    /**
     * 账号类型
     * 0 个人
     * 1 企业
     * 2 子账号
     */

    private Integer type;

    /**
     * 运营评分
     */

    private Integer score;

    /**
     * 最后一次登录时间
     */

    private Date loginTime;


}