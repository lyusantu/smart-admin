package com.lyusantu.easy.admin.module.pm.project.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 项目信息表 新建表单
 *
 * @Author lyusantu
 * @Date 2025-02-20 10:29:51
 * @Copyright lyusantu
 */

@Data
public class ProjectAddForm {

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目名称 不能为空")
    private String projectName;

    @Schema(description = "采购成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "采购成本 不能为空")
    private BigDecimal cost;

    @Schema(description = "项目开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目开始时间 不能为空")
    private LocalDate startTime;

    @Schema(description = "项目结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目结束时间 不能为空")
    private LocalDate endTime;

    @Schema(description = "项目模板")
    private Long nodeTemplateId;

}
