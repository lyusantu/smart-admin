package com.lyusantu.easy.admin.module.pm.project.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
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

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "负责人")
    private Long director;

    @Schema(description = "阶段开始时间")
    private LocalDate startTime;

    @Schema(description = "阶段结束时间")
    private LocalDate endTime;

    @Schema(description = "阶段内容")
    private String context;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "延期原因")
    private String delayReason;

}
