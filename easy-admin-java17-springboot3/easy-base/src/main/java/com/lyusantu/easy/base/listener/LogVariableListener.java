package com.lyusantu.easy.base.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 将application.yaml中的日志路径变量:project.log-path注入到log4j2.xml
 */
@Order(value = LoggingApplicationListener.DEFAULT_ORDER - 1)
public class LogVariableListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String LOG_DIRECTORY = "project.log-directory";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEvent) {
        ConfigurableEnvironment environment = applicationEvent.getEnvironment();
        String filePath = environment.getProperty(LOG_DIRECTORY);
        if (filePath != null) {
            System.setProperty(LOG_DIRECTORY, filePath);
        }
    }
}
