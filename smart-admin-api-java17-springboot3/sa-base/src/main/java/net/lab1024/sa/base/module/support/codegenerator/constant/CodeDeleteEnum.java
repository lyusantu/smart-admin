package net.lab1024.sa.base.module.support.codegenerator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 删除类型
 */
@Getter
@AllArgsConstructor
public enum CodeDeleteEnum implements BaseEnum {

    SINGLE("Single", "单个删除"),
    BATCH("Batch", "批量删除"),
    SINGLE_AND_BATCH("SingleAndBatch", "单个和批量删除");

    private final String value;

    private final String desc;

}
