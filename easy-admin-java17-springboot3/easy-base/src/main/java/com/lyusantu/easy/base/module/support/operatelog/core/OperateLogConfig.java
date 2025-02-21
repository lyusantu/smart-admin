package com.lyusantu.easy.base.module.support.operatelog.core;

import lombok.Builder;
import lombok.Data;
import com.lyusantu.easy.base.module.support.operatelog.domain.OperateLogEntity;

import java.util.function.Function;

/**
 * 配置
 */
@Data
@Builder
public class OperateLogConfig {

    /**
     * 操作日志存储方法
     */
    private Function<OperateLogEntity, Boolean> saveFunction;

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 队列大小
     */
    private Integer queueCapacity;


}
