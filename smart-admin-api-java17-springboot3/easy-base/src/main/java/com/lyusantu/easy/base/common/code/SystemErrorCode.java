package com.lyusantu.easy.base.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统错误码
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {

    /**
     * 系统错误
     */
    SYSTEM_ERROR(10001, "系统似乎出现了点小问题"),

    ;

    private final int code;

    private final String msg;

    private final String level;

    SystemErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_SYSTEM;
    }

}

