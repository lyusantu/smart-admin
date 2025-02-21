package com.lyusantu.easy.base.module.support.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 系统配置常量类
 */
@Getter
@AllArgsConstructor
public enum ConfigKeyEnum implements BaseEnum {

    SUPER_PASSWORD("super_password", "万能密码"),
    LEVEL3_PROTECT_CONFIG("level3_protect_config", "三级等保配置");

    private final String value;

    private final String desc;
}
