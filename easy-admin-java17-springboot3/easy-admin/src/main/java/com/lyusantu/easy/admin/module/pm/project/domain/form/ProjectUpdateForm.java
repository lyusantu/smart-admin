package com.lyusantu.easy.admin.module.pm.project.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目信息表 更新表单
 *
 * @Author lyusantu
 * @Date 2025-02-20 10:29:51
 * @Copyright lyusantu
 */

@Data
public class ProjectUpdateForm {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目ID 不能为空")
    private Long projectId;

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

}
