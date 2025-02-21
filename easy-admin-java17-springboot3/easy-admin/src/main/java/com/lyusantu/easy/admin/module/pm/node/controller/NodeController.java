package com.lyusantu.easy.admin.module.pm.node.controller;

import com.lyusantu.easy.admin.module.pm.node.domain.form.NodeAddForm;
import com.lyusantu.easy.admin.module.pm.node.domain.form.NodeQueryForm;
import com.lyusantu.easy.admin.module.pm.node.domain.form.NodeUpdateForm;
import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeListVO;
import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeTemplateListVO;
import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeVO;
import com.lyusantu.easy.admin.module.pm.node.service.NodeService;
import com.lyusantu.easy.base.common.domain.validate.ValidateList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 节点模板表 Controller
 *
 * @Author lyusantu
 * @Date 2025-02-19 14:33:24
 * @Copyright lyusantu
 */

@RequiredArgsConstructor
@RestController
@Tag(name = "节点模板表")
public class NodeController {

    private final NodeService nodeService;

    @Operation(summary = "分页查询")
    @PostMapping("/pm/node/queryPage")
    @SaCheckPermission("node:query")
    public ResponseDTO<PageResult<NodeVO>> queryPage(@RequestBody @Valid NodeQueryForm queryForm) {
        return ResponseDTO.ok(nodeService.queryPage(queryForm));
    }

    @Operation(summary = "添加")
    @PostMapping("/pm/node/add")
    @SaCheckPermission("node:add")
    public ResponseDTO<String> add(@RequestBody @Valid NodeAddForm addForm) {
        return nodeService.add(addForm);
    }

    @Operation(summary = "更新")
    @PostMapping("/pm/node/update")
    @SaCheckPermission("node:update")
    public ResponseDTO<String> update(@RequestBody @Valid NodeUpdateForm updateForm) {
        return nodeService.update(updateForm);
    }

    @Operation(summary = "批量删除")
    @PostMapping("/pm/node/batchDelete")
    @SaCheckPermission("node:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return nodeService.batchDelete(idList);
    }

    @Operation(summary = "单个删除")
    @GetMapping("/pm/node/delete/{nodeId}")
    @SaCheckPermission("node:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long nodeId) {
        return nodeService.delete(nodeId);
    }

    @Operation(summary = "节点列表")
    @GetMapping("/pm/node/listNode/{nodeId}")
    @SaCheckPermission("node:query")
    public ResponseDTO<List<NodeListVO>> listNode(@PathVariable Long nodeId) {
        return ResponseDTO.ok(nodeService.listNode(nodeId));
    }

    @Operation(summary = "模版列表")
    @PostMapping("/pm/node/listNodeTemplate")
    @SaCheckPermission("node:query")
    public ResponseDTO<List<NodeTemplateListVO>> listNodeTemplate() {
        return ResponseDTO.ok(nodeService.listNodeTemplate());
    }

}
