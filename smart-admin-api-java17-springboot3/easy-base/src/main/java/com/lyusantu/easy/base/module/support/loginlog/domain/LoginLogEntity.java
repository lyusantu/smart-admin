package com.lyusantu.easy.base.module.support.loginlog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志
 */
@TableName("t_login_log")
@Data
@Builder
public class LoginLogEntity {

    @TableId(type = IdType.AUTO)
    private Long loginLogId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录ip地区
     */
    private String loginIpRegion;

    /**
     * user-agent
     */
    private String userAgent;

    /**
     * 备注
     */
    private String remark;

    /**
     * 登录类型
     */
    private Integer loginResult;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
