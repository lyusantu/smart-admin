package com.lyusantu.easy.base.module.support.dict.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 字典
 */
@Data
public class DictValueAddForm {

    @Schema(description = "dictKeyId")
    @NotNull(message = "dictKeyId不能为空")
    private Long dictKeyId;

    @Schema(description = "编码")
    @NotBlank(message = "编码不能为空")
    @Length(max = 50,message = "编码太长了，不能超过50字符")
    private String valueCode;

    @Schema(description = "名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 50,message = "名称太长了，不能超过50字符")
    private String valueName;

    @Schema(description = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注")
    @Length(max = 500,message = "备注太长了")
    private String remark;

}
