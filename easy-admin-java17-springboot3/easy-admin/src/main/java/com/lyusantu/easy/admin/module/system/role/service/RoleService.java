package com.lyusantu.easy.admin.module.system.role.service;

import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.module.system.role.mapper.RoleMapper;
import com.lyusantu.easy.admin.module.system.role.mapper.RoleEmployeeMapper;
import com.lyusantu.easy.admin.module.system.role.mapper.RoleMenuMapper;
import com.lyusantu.easy.admin.module.system.role.domain.entity.RoleEntity;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleAddForm;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleUpdateForm;
import com.lyusantu.easy.admin.module.system.role.domain.vo.RoleVO;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色
 */
@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleMapper roleMapper;

    private final RoleMenuMapper roleMenuMapper;

    private final RoleEmployeeMapper roleEmployeeMapper;

    /**
     * 新增添加角色
     */
    public ResponseDTO<String> addRole(RoleAddForm roleAddForm) {
        RoleEntity existRoleEntity = roleMapper.getByRoleName(roleAddForm.getRoleName());
        if (null != existRoleEntity) {
            return ResponseDTO.userErrorParam("角色名称重复");
        }

        existRoleEntity = roleMapper.getByRoleCode(roleAddForm.getRoleCode());
        if (null != existRoleEntity) {
            return ResponseDTO.userErrorParam("角色编码重复，重复的角色为：" + existRoleEntity.getRoleName());
        }

        RoleEntity roleEntity = BeanUtil.copy(roleAddForm, RoleEntity.class);
        roleMapper.insert(roleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据角色id 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteRole(Long roleId) {
        RoleEntity roleEntity = roleMapper.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 当没有员工绑定这个角色时才可以删除
        Integer exists = roleEmployeeMapper.existsByRoleId(roleId);
        if (exists != null) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "该角色下存在员工，无法删除");
        }
        roleMapper.deleteById(roleId);
        roleMenuMapper.deleteByRoleId(roleId);
        roleEmployeeMapper.deleteByRoleId(roleId);
        return ResponseDTO.ok();
    }

    /**
     * 更新角色
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRole(RoleUpdateForm roleUpdateForm) {
        if (null == roleMapper.selectById(roleUpdateForm.getRoleId())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        RoleEntity existRoleEntity = roleMapper.getByRoleName(roleUpdateForm.getRoleName());
        if (null != existRoleEntity && !existRoleEntity.getRoleId().equals(roleUpdateForm.getRoleId())) {
            return ResponseDTO.userErrorParam("角色名称重复");
        }

        existRoleEntity = roleMapper.getByRoleCode(roleUpdateForm.getRoleCode());
        if (null != existRoleEntity && !existRoleEntity.getRoleId().equals(roleUpdateForm.getRoleId())) {
            return ResponseDTO.userErrorParam("角色编码重复，重复的角色为：" + existRoleEntity.getRoleName());
        }

        RoleEntity roleEntity = BeanUtil.copy(roleUpdateForm, RoleEntity.class);
        roleMapper.updateById(roleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据id获取角色数据
     */
    public ResponseDTO<RoleVO> getRoleById(Long roleId) {
        RoleEntity roleEntity = roleMapper.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        RoleVO role = BeanUtil.copy(roleEntity, RoleVO.class);
        return ResponseDTO.ok(role);
    }

    /**
     * 获取所有角色列表
     */
    public ResponseDTO<List<RoleVO>> getAllRole() {
        List<RoleEntity> roleEntityList = roleMapper.selectList(null);
        List<RoleVO> roleList = BeanUtil.copyList(roleEntityList, RoleVO.class);
        return ResponseDTO.ok(roleList);
    }
}
