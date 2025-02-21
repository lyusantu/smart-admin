package com.lyusantu.easy.base.module.support.changelog.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]
 */

@AllArgsConstructor
@Getter
public enum ChangeLogTypeEnum implements BaseEnum {

    MAJOR_UPDATE(1, "重大更新"),
    FUNCTION_UPDATE(2, "功能更新"),
    BUG_FIX(3, "Bug修复");

    private final Integer value;

    private final String desc;
}
