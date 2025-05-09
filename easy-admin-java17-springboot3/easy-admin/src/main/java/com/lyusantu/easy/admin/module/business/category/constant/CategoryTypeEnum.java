package com.lyusantu.easy.admin.module.business.category.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 分类类型 枚举
 */
@AllArgsConstructor
@Getter
public enum CategoryTypeEnum implements BaseEnum {

    /**
     * 1 商品
     */
    GOODS(1, "商品"),

    /**
     * 2 自定义
     */
    CUSTOM(2, "自定义"),

    ;

    private final Integer value;

    private final String desc;
}
