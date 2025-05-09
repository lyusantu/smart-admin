package com.lyusantu.easy.base.module.support.captcha.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 图形验证码 表单
 */

@Data
public class CaptchaForm {

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captchaCode;

    @Schema(description = "验证码uuid标识")
    @NotBlank(message = "验证码uuid标识不能为空")
    private String captchaUuid;
}
