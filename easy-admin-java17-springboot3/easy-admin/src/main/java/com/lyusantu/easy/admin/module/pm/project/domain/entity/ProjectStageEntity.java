package com.lyusantu.easy.admin.module.pm.project.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 项目阶段表 实体类
 *
 * @Author lyusantu
 * @Date 2025-02-21 11:10:50
 * @Copyright lyusantu
 */

@Data
@TableName("t_project_stage")
public class ProjectStageEntity {

    /**
     * 阶段ID
     */
    @TableId(type = IdType.AUTO)
    private Long stageId;

    /**
     * 项目节点Id
     */
    private Long projectNodeId;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 优先级：0紧急、1重要、2一般、3普通
     */
    private Integer priority;

    /**
     * 负责人
     */
    private Long director;

    /**
     * 阶段开始时间
     */
    private LocalDate startTime;

    /**
     * 阶段结束时间
     */
    private LocalDate endTime;

    /**
     * 阶段完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 阶段内容
     */
    private String context;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：0未开始、1进行中、2提前完成、3已完成、4延期、5已关闭
     */
    private Integer state;

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

    /**
     * 延期原因
     */
    private String delayReason;

}
