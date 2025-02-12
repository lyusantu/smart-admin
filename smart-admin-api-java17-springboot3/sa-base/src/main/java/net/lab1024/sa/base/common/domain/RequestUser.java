package net.lab1024.sa.base.common.domain;

import net.lab1024.sa.base.common.enumeration.UserTypeEnum;

/**
 * 请求用户
 */
public interface RequestUser {

    /**
     * 获取请求用户ID
     *
     * @return 用户ID
     */
    Long getUserId();

    /**
     * 获取请求用户名称
     *
     * @return 用户名称
     */
    String getUserName();

    /**
     * 获取用户类型
     */
    UserTypeEnum getUserType();

    /**
     * 获取请求的IP
     *
     * @return 请求IP
     */
    String getIp();

    /**
     * 获取请求UA
     *
     * @return UA
     */
    String getUserAgent();

}
