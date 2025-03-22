package com.lyusantu.easy.admin.module.pm.project.constant;

import com.lyusantu.easy.base.common.enumeration.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目阶段类型
 **/
@AllArgsConstructor
@Getter
public enum ProjectStageStateEnum implements BaseEnum {

    NOT_STARTED(0, "未开始"),
    IN_PROGRESS(1, "1进行中"),
    AHEAD_COMPLETED(2, "提前完成"),
    COMPLETED(3, "已完成"),
    DELAYED(4, "已延期"),
    CLOSED(5, "已关闭");


    private final Integer value;

    private final String desc;

}
