package com.lyusantu.easy.admin.config;

import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.interceptor.AdminInterceptor;
import com.lyusantu.easy.base.config.SwaggerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web相关配置
 */
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdminInterceptor adminInterceptor;

    /**
     * 拦截所有请求但是排查Swagger
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .excludePathPatterns(SwaggerConfig.SWAGGER_WHITELIST)
                .addPathPatterns("/**");
    }

    /**
     * 配置Swagger文档资源路径，保证doc.html和webjars资源可以被正确访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
