package com.lyusantu.easy.base.module.support.job.config;

import com.lyusantu.easy.base.module.support.job.core.EasyJobLauncher;
import com.lyusantu.easy.base.module.support.job.repository.EasyJobRepository;
import com.lyusantu.easy.base.module.support.job.core.EasyJob;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 定时任务 配置
 */
@Configuration
@EnableConfigurationProperties(EasyJobConfig.class)
@ConditionalOnProperty(
        prefix = EasyJobConfig.CONFIG_PREFIX,
        name = "enabled",
        havingValue = "true"
)
public class EasyJobAutoConfiguration {

    private final EasyJobConfig jobConfig;

    private final EasyJobRepository jobRepository;

    private final List<EasyJob> jobInterfaceList;

    public EasyJobAutoConfiguration(EasyJobConfig jobConfig,
                                    EasyJobRepository jobRepository,
                                    List<EasyJob> jobInterfaceList) {
        this.jobConfig = jobConfig;
        this.jobRepository = jobRepository;
        this.jobInterfaceList = jobInterfaceList;
    }

    /**
     * 定时任务启动器
     *
     * @return
     */
    @Bean
    public EasyJobLauncher initJobLauncher(RedissonClient redissonClient) {
        return new EasyJobLauncher(jobConfig, jobRepository, jobInterfaceList, redissonClient);
    }
}
