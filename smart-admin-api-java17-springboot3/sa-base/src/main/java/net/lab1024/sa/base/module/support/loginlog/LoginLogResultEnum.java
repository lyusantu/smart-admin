package net.lab1024.sa.base.module.support.loginlog;

import lombok.AllArgsConstructor;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 登录类型
 */
@AllArgsConstructor
public enum LoginLogResultEnum implements BaseEnum {

    LOGIN_SUCCESS(0, "登录成功"),
    LOGIN_FAIL(1, "登录失败"),
    LOGIN_OUT(2, "退出登录");

    private final Integer type;
    private final String desc;

    @Override
    public Integer getValue() {
        return type;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
