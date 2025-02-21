package com.lyusantu.easy.admin.module.pm.node.domain.form;

import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeListVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 节点模板表 更新表单
 *
 * @Author lyusantu
 * @Date 2025-02-19 14:33:24
 * @Copyright lyusantu
 */

@Data
public class NodeUpdateForm {

    @Schema(description = "节点ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "节点ID不能为空")
    private Long nodeId;

    @Schema(description = "节点模版名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "节点模版名称不能为空")
    private String nodeName;

    @Schema(description = "描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;

    private List<NodeListVO> list;

}
