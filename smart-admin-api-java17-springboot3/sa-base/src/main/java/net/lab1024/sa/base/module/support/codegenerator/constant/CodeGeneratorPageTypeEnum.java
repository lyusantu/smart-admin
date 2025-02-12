package net.lab1024.sa.base.module.support.codegenerator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 页面类型
 */
@Getter
@AllArgsConstructor
public enum CodeGeneratorPageTypeEnum implements BaseEnum {

    MODAL("modal", "弹窗"),
    DRAWER("drawer", "抽屉"),
    PAGE("page", "新页面");

    private final String value;

    private final String desc;

}
