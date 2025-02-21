package com.lyusantu.easy.admin.module.pm.node.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 节点模板表 列表VO
 *
 * @Author lyusantu
 * @Date 2025-02-19 14:33:24
 * @Copyright lyusantu
 */

@Data
public class NodeVO {


    @Schema(description = "节点ID")
    private Long nodeId;

    @Schema(description = "节点模板名称")
    private String nodeName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否删除")
    private Integer deletedFlag;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
