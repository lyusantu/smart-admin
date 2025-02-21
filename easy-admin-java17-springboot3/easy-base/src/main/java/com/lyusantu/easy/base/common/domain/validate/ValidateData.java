package com.lyusantu.easy.base.common.domain.validate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 校验数据是否为空的包装类
 */
@Data
public class ValidateData<T> {

    @NotNull(message = "数据不能为空哦")
    private T data;
}
