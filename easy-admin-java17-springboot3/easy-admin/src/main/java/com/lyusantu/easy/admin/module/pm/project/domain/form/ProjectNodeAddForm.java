package com.lyusantu.easy.admin.module.pm.project.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 项目节点新增
 */
@Data
public class ProjectNodeAddForm {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    @Schema(description = "节点名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;


}
