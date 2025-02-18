package com.lyusantu.easy.base.common.domain;


import com.lyusantu.easy.base.common.enumeration.SystemEnvironmentEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统环境
 */
@AllArgsConstructor
@Getter
public class SystemEnvironment {

    /**
     * 是否是生产环境
     */
    private boolean isProd;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 当前环境
     */
    private SystemEnvironmentEnum currentEnvironment;
}
