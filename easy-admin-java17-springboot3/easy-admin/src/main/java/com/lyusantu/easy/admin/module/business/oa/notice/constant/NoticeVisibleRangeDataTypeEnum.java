package com.lyusantu.easy.admin.module.business.oa.notice.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 公告、通知 可见范围类型
 */
@Getter
@AllArgsConstructor
public enum NoticeVisibleRangeDataTypeEnum implements BaseEnum {

    EMPLOYEE(1, "员工"),
    DEPARTMENT(2, "部门");

    private final Integer value;

    private final String desc;
}
