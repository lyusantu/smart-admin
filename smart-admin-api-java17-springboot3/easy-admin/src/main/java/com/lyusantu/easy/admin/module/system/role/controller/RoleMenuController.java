package com.lyusantu.easy.admin.module.system.role.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lyusantu.easy.admin.module.system.role.service.RoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.constant.AdminSwaggerTagConst;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleMenuUpdateForm;
import com.lyusantu.easy.admin.module.system.role.domain.vo.RoleMenuTreeVO;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

/**
 * 角色的菜单
 */
@RequiredArgsConstructor
@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_ROLE_MENU)
public class RoleMenuController {

    private final RoleMenuService roleMenuService;

    @Operation(summary = "更新角色权限")
    @PostMapping("/role/menu/updateRoleMenu")
    @SaCheckPermission("system:role:menu:update")
    public ResponseDTO<String> updateRoleMenu(@Valid @RequestBody RoleMenuUpdateForm updateDTO) {
        return roleMenuService.updateRoleMenu(updateDTO);
    }

    @Operation(summary = "获取角色关联菜单权限")
    @GetMapping("/role/menu/getRoleSelectedMenu/{roleId}")
    public ResponseDTO<RoleMenuTreeVO> getRoleSelectedMenu(@PathVariable Long roleId) {
        return roleMenuService.getRoleSelectedMenu(roleId);
    }
}
