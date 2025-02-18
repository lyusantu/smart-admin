package com.lyusantu.easy.admin.module.system.role.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.constant.AdminSwaggerTagConst;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleDataScopeUpdateForm;
import com.lyusantu.easy.admin.module.system.role.domain.vo.RoleDataScopeVO;
import com.lyusantu.easy.admin.module.system.role.service.RoleDataScopeService;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色的数据权限配置
 */
@RequiredArgsConstructor
@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_ROLE_DATA_SCOPE)
public class RoleDataScopeController {

    private final RoleDataScopeService roleDataScopeService;

    @Operation(summary = "获取某角色所设置的数据范围")
    @GetMapping("/role/dataScope/getRoleDataScopeList/{roleId}")
    public ResponseDTO<List<RoleDataScopeVO>> dataScopeListByRole(@PathVariable Long roleId) {
        return roleDataScopeService.getRoleDataScopeList(roleId);
    }

    @Operation(summary = "批量设置某角色数据范围")
    @PostMapping("/role/dataScope/updateRoleDataScopeList")
    @SaCheckPermission("system:role:dataScope:update")
    public ResponseDTO<String> updateRoleDataScopeList(@RequestBody @Valid RoleDataScopeUpdateForm roleDataScopeUpdateForm) {
        return roleDataScopeService.updateRoleDataScopeList(roleDataScopeUpdateForm);
    }


}
