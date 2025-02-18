package com.lyusantu.easy.admin.module.system.role.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.constant.AdminSwaggerTagConst;
import com.lyusantu.easy.admin.module.system.employee.domain.vo.EmployeeVO;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleEmployeeQueryForm;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleEmployeeUpdateForm;
import com.lyusantu.easy.admin.module.system.role.domain.vo.RoleSelectedVO;
import com.lyusantu.easy.admin.module.system.role.service.RoleEmployeeService;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色的员工
 */
@RequiredArgsConstructor
@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_ROLE_EMPLOYEE)
public class RoleEmployeeController {

    private final RoleEmployeeService roleEmployeeService;

    @Operation(summary = "查询某个角色下的员工列表")
    @PostMapping("/role/employee/queryEmployee")
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(@Valid @RequestBody RoleEmployeeQueryForm roleEmployeeQueryForm) {
        return roleEmployeeService.queryEmployee(roleEmployeeQueryForm);
    }

    @Operation(summary = "获取某个角色下的所有员工列表(无分页)")
    @GetMapping("/role/employee/getAllEmployeeByRoleId/{roleId}")
    public ResponseDTO<List<EmployeeVO>> listAllEmployeeRoleId(@PathVariable Long roleId) {
        return ResponseDTO.ok(roleEmployeeService.getAllEmployeeByRoleId(roleId));
    }

    @Operation(summary = "从角色成员列表中移除员工")
    @GetMapping("/role/employee/removeEmployee")
    @SaCheckPermission("system:role:employee:delete")
    public ResponseDTO<String> removeEmployee(Long employeeId, Long roleId) {
        return roleEmployeeService.removeRoleEmployee(employeeId, roleId);
    }

    @Operation(summary = "从角色成员列表中批量移除员工")
    @PostMapping("/role/employee/batchRemoveRoleEmployee")
    @SaCheckPermission("system:role:employee:batch:delete")
    public ResponseDTO<String> batchRemoveEmployee(@Valid @RequestBody RoleEmployeeUpdateForm updateForm) {
        return roleEmployeeService.batchRemoveRoleEmployee(updateForm);
    }

    @Operation(summary = "角色成员列表中批量添加员工")
    @PostMapping("/role/employee/batchAddRoleEmployee")
    @SaCheckPermission("system:role:employee:add")
    public ResponseDTO<String> addEmployeeList(@Valid @RequestBody RoleEmployeeUpdateForm addForm) {
        return roleEmployeeService.batchAddRoleEmployee(addForm);
    }

    @Operation(summary = "获取员工所有选中的角色和所有角色")
    @GetMapping("/role/employee/getRoles/{employeeId}")
    public ResponseDTO<List<RoleSelectedVO>> getRoleByEmployeeId(@PathVariable Long employeeId) {
        return ResponseDTO.ok(roleEmployeeService.getRoleInfoListByEmployeeId(employeeId));
    }
}
