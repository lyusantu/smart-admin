package com.lyusantu.easy.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 登录设备类型
 */
@AllArgsConstructor
@Getter
public enum LoginDeviceEnum implements BaseEnum {

    PC(1, "电脑端"),
    ANDROID(2, "安卓"),
    APPLE(3, "苹果"),
    H5(4, "H5"),
    WEIXIN_MP(5, "微信小程序");

    private final Integer value;
    private final String desc;

}
