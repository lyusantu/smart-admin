package com.lyusantu.easy.admin.module.system.datascope.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;


/**
 * 数据范围 种类
 */
@AllArgsConstructor
@Getter
public enum DataScopeViewTypeEnum implements BaseEnum {

    ME(0, 0, "本人"),
    DEPARTMENT(1, 5, "本部门"),
    DEPARTMENT_AND_SUB(2, 10, "本部门及下属子部门"),
    ALL(10, 100, "全部");

    private final Integer value;
    private final Integer level;
    private final String desc;

}
