package com.lyusantu.easy.admin.module.pm.node.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节点模版列表VO
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NodeTemplateListVO {

    @Schema(description = "节点ID")
    private Long nodeId;

    @Schema(description = "节点模板名称")
    private String nodeName;

}
