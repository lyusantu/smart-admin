package com.lyusantu.easy.admin.module.pm.project.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 项目信息表 列表VO
 *
 * @Author lyusantu
 * @Date 2025-02-20 10:29:51
 * @Copyright lyusantu
 */

@Data
public class ProjectVO {

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "采购成本")
    private BigDecimal cost;

    @Schema(description = "项目开始时间")
    private LocalDate startTime;

    @Schema(description = "项目结束时间")
    private LocalDate endTime;

    @Schema(description = "项目实际完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "创建者")
    private Long createUserId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改者")
    private Long updateUserId;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
