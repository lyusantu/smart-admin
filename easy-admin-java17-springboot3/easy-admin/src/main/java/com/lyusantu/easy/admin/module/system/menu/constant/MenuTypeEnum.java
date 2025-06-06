package com.lyusantu.easy.admin.module.system.menu.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 菜单类型枚举
 */
@AllArgsConstructor
@Getter
public enum MenuTypeEnum implements BaseEnum {

    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    POINTS(3, "功能点");

    private final Integer value;

    private final String desc;
}
