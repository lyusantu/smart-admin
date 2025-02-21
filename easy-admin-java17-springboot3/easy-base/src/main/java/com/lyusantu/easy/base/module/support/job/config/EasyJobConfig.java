package com.lyusantu.easy.base.module.support.job.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * EasyJob 配置
 * 与配置文件参数对应
 */
@ConfigurationProperties(prefix = EasyJobConfig.CONFIG_PREFIX)
@Data
public class EasyJobConfig {

    public static final String CONFIG_PREFIX = "easy.job";

    /**
     * 任务执行核心线程数 偶数 默认2
     */
    private Integer corePoolSize = 2;

    /**
     * 任务延迟初始化 默认30秒
     */
    private Integer initDelay = 30;

    /**
     * 数据库配置检测-开关 默认开启
     */
    private Boolean dbRefreshEnabled = true;

    /**
     * 数据库配置检测-执行间隔 默认120秒
     */
    private Integer dbRefreshInterval = 120;
}
