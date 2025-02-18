package com.lyusantu.easy.base.module.support.apiencrypt.advice;

import com.alibaba.fastjson.JSONObject;
import com.lyusantu.easy.base.module.support.apiencrypt.annotation.ApiDecrypt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.common.util.StringUtil;
import com.lyusantu.easy.base.module.support.apiencrypt.domain.ApiEncryptForm;
import com.lyusantu.easy.base.module.support.apiencrypt.service.ApiEncryptService;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 解密
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class DecryptRequestAdvice extends RequestBodyAdviceAdapter {

    private static final String ENCODING = "UTF-8";

    private final ApiEncryptService apiEncryptService;

    /**
     * 判断当前请求是否需要解密处理
     * 即通过方法/方法参数/方式所在类中是否含有@ApiDecrypt注解进行判断
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(ApiDecrypt.class) ||
                methodParameter.hasParameterAnnotation(ApiDecrypt.class) ||
                methodParameter.getContainingClass().isAnnotationPresent(ApiDecrypt.class);
    }

    /**
     * 在请求体读取之前执行，主要用于对加密的数据进行解密
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            // 读取原始数据并转为字符串
            String bodyStr = IOUtils.toString(inputMessage.getBody(), ENCODING);
            // 转换字符串为解密对象并判断是否需要解密
            ApiEncryptForm apiEncryptForm = JSONObject.parseObject(bodyStr, ApiEncryptForm.class);
            if (StringUtil.isEmpty(apiEncryptForm.getEncryptData())) {
                return inputMessage;
            }
            // 解密并将解密后的数据转为输入流后再通过DecryptHttpInputMessage包装后返回
            String decrypt = apiEncryptService.decrypt(apiEncryptForm.getEncryptData());
            return new DecryptHttpInputMessage(inputMessage.getHeaders(), IOUtils.toInputStream(decrypt, ENCODING));
        } catch (Exception e) {
            log.error("", e);
            return inputMessage;
        }
    }

    /**
     * 在请求体被读取并转为对象后执行
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 处理空请求体
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @AllArgsConstructor
    @Getter
    static class DecryptHttpInputMessage implements HttpInputMessage {
        private final HttpHeaders headers;
        private final InputStream body;
    }

}
