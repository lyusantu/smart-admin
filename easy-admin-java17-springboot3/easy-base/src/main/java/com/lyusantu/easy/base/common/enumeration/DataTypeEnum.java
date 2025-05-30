package com.lyusantu.easy.base.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据类型枚举类
 */

@Getter
@AllArgsConstructor
public enum DataTypeEnum implements BaseEnum {

    NORMAL(1, "普通数据"),
    ENCRYPT(10, "加密数据");

    private final Integer value;

    private final String desc;

}
