package com.lyusantu.easy.admin.module.system.position.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 职务表 新建表单
 */
@Data
public class PositionAddForm {

    @Schema(description = "职务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "职务名称 不能为空")
    private String positionName;

    @Schema(description = "职级")
    private String level;

    @Schema(description = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

}
