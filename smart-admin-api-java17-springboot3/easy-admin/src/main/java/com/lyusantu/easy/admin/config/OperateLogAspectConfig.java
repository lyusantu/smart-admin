package com.lyusantu.easy.admin.config;

import com.lyusantu.easy.base.module.support.operatelog.core.OperateLogAspect;
import com.lyusantu.easy.base.module.support.operatelog.core.OperateLogConfig;
import org.springframework.context.annotation.Configuration;

/**
 * 操作日志切面配置
 */
@Configuration
public class OperateLogAspectConfig extends OperateLogAspect {

    /**
     * 配置信息
     */
    @Override
    public OperateLogConfig getOperateLogConfig() {
        return OperateLogConfig.builder()
                // 动态设定核心线程池大小
                .corePoolSize(Runtime.getRuntime().availableProcessors())
                // 适当配置队列容量,避免内存占用过高
                .queueCapacity(1000)
                .build();
    }

}
