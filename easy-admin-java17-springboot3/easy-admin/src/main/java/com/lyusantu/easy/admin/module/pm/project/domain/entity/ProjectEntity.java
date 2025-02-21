package com.lyusantu.easy.admin.module.pm.project.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 项目信息表 实体类
 *
 * @Author lyusantu
 * @Date 2025-02-20 10:29:51
 * @Copyright lyusantu
 */

@Data
@TableName("t_project")
public class ProjectEntity {

    /**
     * 项目ID
     */
    @TableId(type = IdType.AUTO)
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 采购成本
     */
    private BigDecimal cost;

    /**
     * 项目开始时间
     */
    private LocalDate startTime;

    /**
     * 项目结束时间
     */
    private LocalDate endTime;

    /**
     * 项目实际完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 状态：0未开始、1进行中、2已挂起、3已完成、4已关闭 5已延期
     */
    private Integer state;

    /**
     * 创建者
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改者
     */
    private Long updateUserId;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
