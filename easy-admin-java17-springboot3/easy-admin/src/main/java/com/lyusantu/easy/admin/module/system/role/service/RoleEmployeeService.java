package com.lyusantu.easy.admin.module.system.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lyusantu.easy.admin.module.system.role.manager.RoleEmployeeManager;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.module.system.department.mapper.DepartmentMapper;
import com.lyusantu.easy.admin.module.system.department.domain.entity.DepartmentEntity;
import com.lyusantu.easy.admin.module.system.employee.domain.vo.EmployeeVO;
import com.lyusantu.easy.admin.module.system.role.mapper.RoleMapper;
import com.lyusantu.easy.admin.module.system.role.mapper.RoleEmployeeMapper;
import com.lyusantu.easy.admin.module.system.role.domain.entity.RoleEmployeeEntity;
import com.lyusantu.easy.admin.module.system.role.domain.entity.RoleEntity;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleEmployeeQueryForm;
import com.lyusantu.easy.admin.module.system.role.domain.form.RoleEmployeeUpdateForm;
import com.lyusantu.easy.admin.module.system.role.domain.vo.RoleSelectedVO;
import com.lyusantu.easy.admin.module.system.role.domain.vo.RoleVO;
import com.lyusantu.easy.base.common.constant.StringConst;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色-员工
 */
@RequiredArgsConstructor
@Service
public class RoleEmployeeService {

    private final RoleMapper roleMapper;

    private final DepartmentMapper departmentMapper;

    private final RoleEmployeeMapper roleEmployeeMapper;

    private final RoleEmployeeManager roleEmployeeManager;


    /**
     * 批量插入
     */
    public void batchInsert(List<RoleEmployeeEntity> roleEmployeeList) {
        roleEmployeeManager.saveBatch(roleEmployeeList);
    }

    /**
     * 通过角色id，分页获取成员员工列表
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(RoleEmployeeQueryForm roleEmployeeQueryForm) {
        Page page = PageUtil.convert2PageQuery(roleEmployeeQueryForm);
        List<EmployeeVO> employeeList = roleEmployeeMapper.selectRoleEmployeeByName(page, roleEmployeeQueryForm)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Long> departmentIdList = employeeList.stream().filter(e -> e != null && e.getDepartmentId() != null).map(EmployeeVO::getDepartmentId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(departmentIdList)) {
            List<DepartmentEntity> departmentEntities = departmentMapper.selectBatchIds(departmentIdList);
            Map<Long, String> departmentIdNameMap = departmentEntities.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));
            employeeList.forEach(e -> {
                e.setDepartmentName(departmentIdNameMap.getOrDefault(e.getDepartmentId(), StringConst.EMPTY));
            });
        }
        PageResult<EmployeeVO> pageResult = PageUtil.convert2PageResult(page, employeeList, EmployeeVO.class);
        return ResponseDTO.ok(pageResult);
    }

    public List<EmployeeVO> getAllEmployeeByRoleId(Long roleId) {
        return roleEmployeeMapper.selectEmployeeByRoleId(roleId);
    }

    /**
     * 移除员工角色
     */
    public ResponseDTO<String> removeRoleEmployee(Long employeeId, Long roleId) {
        if (null == employeeId || null == roleId) {
            return ResponseDTO.userErrorParam();
        }
        roleEmployeeMapper.deleteByEmployeeIdRoleId(employeeId, roleId);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除角色的成员员工
     */
    public ResponseDTO<String> batchRemoveRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        roleEmployeeMapper.batchDeleteEmployeeRole(roleEmployeeUpdateForm.getRoleId(), roleEmployeeUpdateForm.getEmployeeIdList());
        return ResponseDTO.ok();
    }

    /**
     * 批量添加角色的成员员工
     */
    public ResponseDTO<String> batchAddRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        Long roleId = roleEmployeeUpdateForm.getRoleId();

        // 已选择的员工id列表
        Set<Long> selectedEmployeeIdList = roleEmployeeUpdateForm.getEmployeeIdList();
        // 数据库里已有的员工id列表
        Set<Long> dbEmployeeIdList = roleEmployeeMapper.selectEmployeeIdByRoleIdList(Lists.newArrayList(roleId));
        // 从已选择的员工id列表里 过滤数据库里不存在的 即需要添加的员工 id
        Set<Long> addEmployeeIdList = selectedEmployeeIdList.stream().filter(id -> !dbEmployeeIdList.contains(id)).collect(Collectors.toSet());

        // 添加角色员工
        if (CollectionUtils.isNotEmpty(addEmployeeIdList)) {
            List<RoleEmployeeEntity> roleEmployeeList = addEmployeeIdList.stream()
                    .map(employeeId -> new RoleEmployeeEntity(roleId, employeeId))
                    .collect(Collectors.toList());
            roleEmployeeManager.saveBatch(roleEmployeeList);
        }
        return ResponseDTO.ok();
    }

    /**
     * 通过员工id获取员工角色
     */
    public List<RoleSelectedVO> getRoleInfoListByEmployeeId(Long employeeId) {
        List<Long> roleIds = roleEmployeeMapper.selectRoleIdByEmployeeId(employeeId);
        List<RoleEntity> roleList = roleMapper.selectList(null);
        List<RoleSelectedVO> result = BeanUtil.copyList(roleList, RoleSelectedVO.class);
        result.stream().forEach(item -> item.setSelected(roleIds.contains(item.getRoleId())));
        return result;
    }

    /**
     * 根据员工id 查询角色id集合
     */
    public List<RoleVO> getRoleIdList(Long employeeId) {
        return roleEmployeeMapper.selectRoleByEmployeeId(employeeId);
    }


}
