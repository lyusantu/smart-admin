package com.lyusantu.easy.admin.module.pm.project.domain.form.expenses;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectExpensesAddForm {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    @Schema(description = "费用类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "费用类型不能为空")
    private Integer expensesType;

    @Schema(description = "费用名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "费用名称不能为空")
    private String expensesName;

    @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "费用金额不能为空")
    private BigDecimal amount;

    @Schema(description = "发票信息")
    private JSONArray invoices;

}
