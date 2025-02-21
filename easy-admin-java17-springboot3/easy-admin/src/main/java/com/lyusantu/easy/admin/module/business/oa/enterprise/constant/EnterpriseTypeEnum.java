package com.lyusantu.easy.admin.module.business.oa.enterprise.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 企业类型
 */
@AllArgsConstructor
@Getter
public enum EnterpriseTypeEnum implements BaseEnum {

    NORMAL(1, "有限企业"),
    FOREIGN(2, "外资企业");

    private final Integer value;
    private final String desc;

}
