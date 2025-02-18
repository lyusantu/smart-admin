package com.lyusantu.easy.base.module.support.codegenerator.domain.model;

import com.lyusantu.easy.base.module.support.codegenerator.constant.CodeGeneratorPageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;
import com.lyusantu.easy.base.common.validator.enumeration.CheckEnum;

import java.util.List;

/**
 * 代码生成 增加、修改 模型
 */
@Data
public class CodeInsertAndUpdate {

    @NotNull(message = "3.增加、修改 是否支持增加、修改 不能为空")
    private Boolean isSupportInsertAndUpdate;

    @SchemaEnum(CodeGeneratorPageTypeEnum.class)
    @CheckEnum(value = CodeGeneratorPageTypeEnum.class, message = "3.增加、修改 增加、修改 页面类型 枚举值错误")
    private String pageType;

    @Schema(description = "宽度")
    private String width;

    @NotNull(message = "3.增加、修改 每行字段数量 不能为空")
    @Schema(description = "每行字段数量")
    private Integer countPerLine;

    @Schema(description = "字段列表")
    private List<CodeInsertAndUpdateField> fieldList;

}
