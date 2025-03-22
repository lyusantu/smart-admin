package com.lyusantu.easy.admin.module.pm.project.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MyTaskVO {

    @Schema(description = "阶段ID")
    private Long stageId;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "节点名称")
    private String projectNodeName;

    @Schema(description = "阶段名称")
    private String stageName;

    @Schema(description = "节点状态")
    private Integer state;

    @Schema(description = "项目开始时间")
    private LocalDate startTime;

    @Schema(description = "项目结束时间")
    private LocalDate endTime;

}
