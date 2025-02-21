package com.lyusantu.easy.admin.module.pm.node.domain.form;

import com.lyusantu.easy.base.common.domain.page.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 节点模板表 分页查询表单
 *
 * @Author lyusantu
 * @Date 2025-02-19 14:33:24
 * @Copyright lyusantu
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class NodeQueryForm extends PageParam {

    @Schema(description = "节点名称")
    private String node_name;

}
