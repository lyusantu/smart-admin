package com.lyusantu.easy.admin.module.pm.project.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 项目阶段表 列表VO
 *
 * @Author lyusantu
 * @Date 2025-02-21 11:10:50
 * @Copyright lyusantu
 */

@Data
public class ProjectStageVO {


    @Schema(description = "阶段ID")
    private Long stageId;

    @Schema(description = "项目节点Id")
    private Long projectNodeId;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "阶段名称")
    private String stageName;

    @Schema(description = "优先级：0紧急、1重要、2一般、3普通")
    private Integer priority;

    @Schema(description = "负责人")
    private Long director;

    @Schema(description = "阶段开始时间")
    private LocalDateTime startTime;

    @Schema(description = "阶段结束时间")
    private LocalDateTime endTime;

    @Schema(description = "阶段完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "阶段内容")
    private String context;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：0未开始、1进行中、2提前完成、3已完成、4延期、5已关闭")
    private Integer state;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "延期原因")
    private String delayReason;

}
