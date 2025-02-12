package net.lab1024.sa.base.config;

import lombok.RequiredArgsConstructor;
import net.lab1024.sa.base.module.support.heartbeat.HeartBeatService;
import net.lab1024.sa.base.module.support.heartbeat.core.HeartBeatManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 心跳配置
 */
@RequiredArgsConstructor
@Configuration
public class HeartBeatConfig {

    /**
     * 间隔时间
     */
    @Value("${heart-beat.interval-seconds}")
    private Long intervalSeconds;

    private final HeartBeatService heartBeatService;

    @Bean
    public HeartBeatManager heartBeatManager() {
        return new HeartBeatManager(intervalSeconds * 1000L, heartBeatService);
    }


}
