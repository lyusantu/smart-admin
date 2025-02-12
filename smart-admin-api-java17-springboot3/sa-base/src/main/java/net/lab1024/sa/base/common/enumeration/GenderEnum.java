package net.lab1024.sa.base.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举类
 */
@AllArgsConstructor
@Getter
public enum GenderEnum implements BaseEnum {

    UNKNOWN(0, "未知"),
    MAN(1, "男"),
    WOMAN(2, "女");

    private final Integer value;

    private final String desc;
}
