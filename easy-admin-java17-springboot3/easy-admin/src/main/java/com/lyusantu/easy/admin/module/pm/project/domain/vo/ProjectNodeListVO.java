package com.lyusantu.easy.admin.module.pm.project.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目节点列表VO
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectNodeListVO {

    @Schema(description = "节点ID")
    private Long projectNodeId;

    @Schema(description = "节点模板名称")
    private String projectNodeName;

}
