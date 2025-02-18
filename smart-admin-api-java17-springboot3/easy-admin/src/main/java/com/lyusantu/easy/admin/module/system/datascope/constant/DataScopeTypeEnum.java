package com.lyusantu.easy.admin.module.system.datascope.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 数据范围 类型
 */
@AllArgsConstructor
@Getter
public enum DataScopeTypeEnum implements BaseEnum {

    NOTICE(1, 20, "系统通知", "系统通知数据范围");

    private final Integer value;

    private final Integer sort;

    private final String name;

    private final String desc;

}
