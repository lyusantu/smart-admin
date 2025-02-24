package com.lyusantu.easy.admin.module.pm.project.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 项目支付明细表 实体类
 *
 * @Author lyusantu
 * @Date 2025-02-24 10:14:42
 * @Copyright lyusantu
 */

@Data
@TableName("t_project_expenses")
public class ProjectExpensesEntity {

    /**
     * 费用ID
     */
    @TableId(type = IdType.AUTO)
    private Long expensesId;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 费用类型
     */
    private Integer expensesType;

    /**
     * 费用名称
     */
    private String expensesName;

    /**
     * 费用金额
     */
    private BigDecimal amount;

    /**
     * 发票信息
     */
    private String invoices;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
