package com.lyusantu.easy.base.module.support.codegenerator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 查询条件类型
 */
@Getter
@AllArgsConstructor
public enum CodeQueryFieldQueryTypeEnum implements BaseEnum {

    LIKE("Like", "模糊查询"),
    EQUAL("Equal", "等于"),
    DATE_RANGE("DateRange", "日期范围"),
    DATE("Date", "指定日期"),
    ENUM("Enum", "枚举"),
    DICT("Dict", "字典"),
    ;

    private final String value;

    private final String desc;

}
