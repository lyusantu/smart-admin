package com.lyusantu.easy.admin.module.system.role.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.module.system.role.domain.entity.RoleDataScopeEntity;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleDataScopeUpdateForm;
import com.lyusantu.easy.admin.module.system.role.domain.vo.RoleDataScopeVO;
import com.lyusantu.easy.admin.module.system.role.manager.RoleDataScopeManager;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 角色-数据范围
 */
@RequiredArgsConstructor
@Service
public class RoleDataScopeService {

    private final RoleDataScopeManager roleDataScopeManager;


    /**
     * 获取某个角色的数据范围设置信息
     *
     */
    public ResponseDTO<List<RoleDataScopeVO>> getRoleDataScopeList(Long roleId) {
        List<RoleDataScopeEntity> roleDataScopeEntityList = roleDataScopeManager.getBaseMapper().listByRoleId(roleId);
        if (CollectionUtils.isEmpty(roleDataScopeEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<RoleDataScopeVO> roleDataScopeList = BeanUtil.copyList(roleDataScopeEntityList, RoleDataScopeVO.class);
        return ResponseDTO.ok(roleDataScopeList);
    }

    /**
     * 批量设置某个角色的数据范围设置信息
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRoleDataScopeList(RoleDataScopeUpdateForm roleDataScopeUpdateForm) {
        List<RoleDataScopeUpdateForm.RoleUpdateDataScopeListFormItem> batchSetList = roleDataScopeUpdateForm.getDataScopeItemList();
        if (CollectionUtils.isEmpty(batchSetList)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "缺少配置信息");
        }
        List<RoleDataScopeEntity> roleDataScopeEntityList = BeanUtil.copyList(batchSetList, RoleDataScopeEntity.class);
        roleDataScopeEntityList.forEach(e -> e.setRoleId(roleDataScopeUpdateForm.getRoleId()));
        roleDataScopeManager.getBaseMapper().deleteByRoleId(roleDataScopeUpdateForm.getRoleId());
        roleDataScopeManager.saveBatch(roleDataScopeEntityList);
        return ResponseDTO.ok();
    }
}
