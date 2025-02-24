package com.lyusantu.easy.admin.module.pm.project.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 项目支付明细表 列表VO
 *
 * @Author lyusantu
 * @Date 2025-02-24 10:14:42
 * @Copyright lyusantu
 */
@Data
public class ProjectExpensesVO {


    @Schema(description = "序号")
    private Integer serialNumber;

    @Schema(description = "费用ID")
    private Long expensesId;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "费用类型")
    private Integer expensesType;

    @Schema(description = "费用名称")
    private String expensesName;

    @Schema(description = "费用金额")
    private BigDecimal amount;

    @Schema(description = "发票信息")
    private String invoices;

    @Schema(description = "发票数量")
    private Integer invoicesCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
