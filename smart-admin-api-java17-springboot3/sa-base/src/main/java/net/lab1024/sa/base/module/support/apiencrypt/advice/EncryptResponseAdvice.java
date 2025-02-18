package net.lab1024.sa.base.module.support.apiencrypt.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.enumeration.DataTypeEnum;
import net.lab1024.sa.base.module.support.apiencrypt.annotation.ApiEncrypt;
import net.lab1024.sa.base.module.support.apiencrypt.service.ApiEncryptService;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 加密
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<ResponseDTO> {

    private final ObjectMapper objectMapper;

    private final ApiEncryptService apiEncryptService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(ApiEncrypt.class) || returnType.getContainingClass().isAnnotationPresent(ApiEncrypt.class);
    }

    @Override
    public ResponseDTO beforeBodyWrite(ResponseDTO body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null || body.getData() == null) {
            return body;
        }
        try {
            body.setData(apiEncryptService.encrypt(objectMapper.writeValueAsString(body.getData())));
            body.setDataType(DataTypeEnum.ENCRYPT.getValue());
            return body;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}


