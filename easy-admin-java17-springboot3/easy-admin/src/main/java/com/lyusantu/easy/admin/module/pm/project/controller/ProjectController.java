package com.lyusantu.easy.admin.module.pm.project.controller;

import com.lyusantu.easy.admin.module.pm.project.domain.form.*;
import com.lyusantu.easy.admin.module.pm.project.domain.form.expenses.ProjectExpensesAddForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.expenses.ProjectExpensesUpdateForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.node.ProjectNodeAddForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.node.ProjectNodeUpdateForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.stage.ProjectStageAddForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.stage.ProjectStageUpdateForm;
import com.lyusantu.easy.admin.module.pm.project.domain.vo.*;
import com.lyusantu.easy.admin.module.pm.project.service.ProjectService;
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
 * 项目信息表 Controller
 *
 * @Author lyusantu
 * @Date 2025-02-20 10:29:51
 * @Copyright lyusantu
 */
@RequiredArgsConstructor
@RestController
@Tag(name = "项目信息表")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "分页查询")
    @PostMapping("/pm/project/queryPage")
    @SaCheckPermission("project:query")
    public ResponseDTO<PageResult<ProjectVO>> queryPage(@RequestBody @Valid ProjectQueryForm queryForm) {
        return ResponseDTO.ok(projectService.queryPage(queryForm));
    }

    @Operation(summary = "添加")
    @PostMapping("/pm/project/add")
    @SaCheckPermission("project:add")
    public ResponseDTO<String> add(@RequestBody @Valid ProjectAddForm addForm) {
        return projectService.add(addForm);
    }

    @Operation(summary = "更新")
    @PostMapping("/pm/project/update")
    @SaCheckPermission("project:update")
    public ResponseDTO<String> update(@RequestBody @Valid ProjectUpdateForm updateForm) {
        return projectService.update(updateForm);
    }

    @Operation(summary = "批量删除")
    @PostMapping("/pm/project/batchDelete")
    @SaCheckPermission("project:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return projectService.batchDelete(idList);
    }

    @Operation(summary = "单个删除")
    @GetMapping("/pm/project/delete/{projectId}")
    @SaCheckPermission("project:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long projectId) {
        return projectService.delete(projectId);
    }

    @Operation(summary = "项目详情")
    @GetMapping("/pm/project/detail/{projectId}")
    @SaCheckPermission("project:query")
    public ResponseDTO<ProjectDetailVO> detail(@PathVariable Long projectId) {
        return projectService.detail(projectId);
    }

    @Operation(summary = "添加项目节点")
    @PostMapping("/pm/project/node/add")
    @SaCheckPermission("project:add")
    public ResponseDTO<String> addProjectNode(@RequestBody @Valid ProjectNodeAddForm form) {
        return projectService.addProjectNode(form);
    }

    @Operation(summary = "更新项目节点")
    @PostMapping("/pm/project/node/update")
    @SaCheckPermission("project:update")
    public ResponseDTO<String> updateProjectNode(@RequestBody @Valid ProjectNodeUpdateForm form) {
        return projectService.updateProjectNode(form);
    }

    @Operation(summary = "项目节点列表")
    @GetMapping("/pm/project/node/list/{projectId}")
    @SaCheckPermission("node:query")
    public ResponseDTO<List<ProjectNodeListVO>> listProjectNode(@PathVariable Long projectId) {
        return projectService.listProjectNode(projectId);
    }

    @Operation(summary = "添加项目阶段")
    @PostMapping("/pm/project/stage/add")
    @SaCheckPermission("project:add")
    public ResponseDTO<String> addProjectStage(@RequestBody @Valid ProjectStageAddForm form) {
        return projectService.addProjectStage(form);
    }

    @Operation(summary = "更新项目阶段")
    @PostMapping("/pm/project/stage/update")
    @SaCheckPermission("project:update")
    public ResponseDTO<String> updateProjectStage(@RequestBody @Valid ProjectStageUpdateForm form) {
        return projectService.updateProjectStage(form);
    }

    @Operation(summary = "项目阶段详情")
    @GetMapping("/pm/project/stage/get/{stageId}")
    @SaCheckPermission("project:query")
    public ResponseDTO<ProjectStageVO> getStage(@PathVariable Long stageId) {
        return projectService.getStage(stageId);
    }

    @Operation(summary = "项目支出明细列表")
    @GetMapping("/pm/project/expenses/list/{projectId}")
    @SaCheckPermission("node:query")
    public ResponseDTO<List<ProjectExpensesVO>> listProjectExpenses(@PathVariable Long projectId) {
        return projectService.listProjectExpenses(projectId);
    }

    @Operation(summary = "添加支出明细")
    @PostMapping("/pm/project/expenses/add")
    @SaCheckPermission("project:add")
    public ResponseDTO<String> addProjectExpenses(@RequestBody @Valid ProjectExpensesAddForm form) {
        return projectService.addProjectExpenses(form);
    }

    @Operation(summary = "更新支出明细")
    @PostMapping("/pm/project/expenses/update")
    @SaCheckPermission("project:update")
    public ResponseDTO<String> updateProjectExpenses(@RequestBody @Valid ProjectExpensesUpdateForm form) {
        return projectService.updateProjectExpenses(form);
    }

    @Operation(summary = "删除支出明细")
    @GetMapping("/pm/project/expenses/delete/{expensesId}")
    @SaCheckPermission("project:delete")
    public ResponseDTO<String> deleteProjectExpenses(@PathVariable Long expensesId) {
        return projectService.deleteProjectExpenses(expensesId);
    }

    @Operation(summary = "我的任务")
    @GetMapping("/pm/project/myTask")
    public ResponseDTO<List<MyTaskVO>> myTask() {
        return projectService.myTask();
    }

}
