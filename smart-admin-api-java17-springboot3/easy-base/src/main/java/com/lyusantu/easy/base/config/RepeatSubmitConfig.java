package com.lyusantu.easy.base.config;

import com.lyusantu.easy.base.common.constant.StringConst;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.module.support.repeatsubmit.RepeatSubmitAspect;
import com.lyusantu.easy.base.module.support.repeatsubmit.ticket.RepeatSubmitCaffeineTicket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 重复提交配置
 */
@Configuration
public class RepeatSubmitConfig {

    @Bean
    public RepeatSubmitAspect repeatSubmitAspect() {
        RepeatSubmitCaffeineTicket caffeineTicket = new RepeatSubmitCaffeineTicket(this::ticket);
        return new RepeatSubmitAspect(caffeineTicket);
    }

    /**
     * 获取指明某个用户的凭证
     */
    private String ticket(String servletPath) {
        Long userId = RequestUtil.getRequestUserId();
        if (null == userId) {
            return StringConst.EMPTY;
        }
        return servletPath + "_" + userId;
    }
}
