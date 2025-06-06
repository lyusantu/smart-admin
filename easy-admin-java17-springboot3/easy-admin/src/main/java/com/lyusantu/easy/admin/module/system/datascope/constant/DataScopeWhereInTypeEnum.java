package com.lyusantu.easy.admin.module.system.datascope.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 数据范围 sql where
 */
@AllArgsConstructor
@Getter
public enum DataScopeWhereInTypeEnum implements BaseEnum {

    EMPLOYEE(0, "以员工IN"),
    DEPARTMENT(1, "以部门IN"),
    CUSTOM_STRATEGY(2, "自定义策略");

    private final Integer value;
    private final String desc;

}
