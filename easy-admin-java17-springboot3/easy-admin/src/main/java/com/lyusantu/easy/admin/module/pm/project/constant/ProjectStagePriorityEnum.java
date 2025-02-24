package com.lyusantu.easy.admin.module.pm.project.constant;

import com.lyusantu.easy.base.common.enumeration.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目阶段优先级
 **/
@AllArgsConstructor
@Getter
public enum ProjectStagePriorityEnum implements BaseEnum {

    EMERGENCY(0, "紧急"),
    IMPORTANT(1, "重要"),
    GENERAL(2, "一般"),
    ORDINARY(3, "普通");

    private final Integer value;

    private final String desc;

}
