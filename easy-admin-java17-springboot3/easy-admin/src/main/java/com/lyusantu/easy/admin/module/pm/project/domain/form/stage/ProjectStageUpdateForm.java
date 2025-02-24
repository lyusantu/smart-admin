package com.lyusantu.easy.admin.module.pm.project.domain.form.stage;

import com.lyusantu.easy.admin.module.pm.project.constant.ProjectStagePriorityEnum;
import com.lyusantu.easy.base.common.validator.enumeration.CheckEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 项目节点新增
 */
@Data
public class ProjectStageUpdateForm {

    @Schema(description = "阶段ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "阶段ID不能为空")
    private Long stageId;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    @Schema(description = "项目节点ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目节点ID不能为空")
    private Long projectNodeId;

    @Schema(description = "阶段名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "阶段名称不能为空")
    private String stageName;

    @Schema(description = "优先级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "请选择优先级")
    @CheckEnum(message = "优先级", value = ProjectStagePriorityEnum.class, required = true)
    private Integer priority;

    @Schema(description = "负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "请选择负责人")
    private Long director;

    @Schema(description = "阶段开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "请选择阶段开始时间")
    private LocalDate startTime;

    @Schema(description = "阶段结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "请选择阶段结束时间")
    private LocalDate endTime;

    @Schema(description = "阶段内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "阶段内容不能为空")
    private String context;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED)
    private String remark;


}
