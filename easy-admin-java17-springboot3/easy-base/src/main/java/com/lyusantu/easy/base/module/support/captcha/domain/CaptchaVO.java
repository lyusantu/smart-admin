package com.lyusantu.easy.base.module.support.captcha.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 图形验证码 VO
 */
@Data
public class CaptchaVO {

    @Schema(description = "验证码唯一标识")
    private String captchaUuid;

    @Schema(description = "验证码图片内容-生产环境无效")
    private String captchaText;

    @Schema(description = "验证码Base64图片")
    private String captchaBase64Image;

    @Schema(description = "过期时间（秒）")
    private Long expireSeconds;
}
