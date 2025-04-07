package com.lyusantu.easy.admin.module.system.datascope.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.module.system.datascope.constant.DataScopeTypeEnum;
import com.lyusantu.easy.admin.module.system.datascope.constant.DataScopeViewTypeEnum;
import com.lyusantu.easy.admin.module.system.department.service.DepartmentService;
import com.lyusantu.easy.admin.module.system.employee.mapper.EmployeeMapper;
import com.lyusantu.easy.admin.module.system.employee.domain.entity.EmployeeEntity;
import com.lyusantu.easy.admin.module.system.role.mapper.RoleDataScopeMapper;
import com.lyusantu.easy.admin.module.system.role.mapper.RoleEmployeeMapper;
import com.lyusantu.easy.admin.module.system.role.domain.entity.RoleDataScopeEntity;
import com.lyusantu.easy.base.common.util.EnumUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据范围
 */
@RequiredArgsConstructor
@Service
public class DataScopeViewService {

    private final EmployeeMapper employeeMapper;

    private final RoleEmployeeMapper roleEmployeeMapper;

    private final RoleDataScopeMapper roleDataScopeMapper;

    private final DepartmentService departmentService;

    /**
     * 获取某人可以查看的所有人员信息
     */
    public List<Long> getCanViewEmployeeId(DataScopeViewTypeEnum viewType, Long employeeId) {
        if (DataScopeViewTypeEnum.ME == viewType) {
            return this.getMeEmployeeIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT == viewType) {
            return this.getDepartmentEmployeeIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT_AND_SUB == viewType) {
            return this.getDepartmentAndSubEmployeeIdList(employeeId);
        }
        return Lists.newArrayList();
    }

    /**
     * 获取某人可以查看的所有部门信息
     */
    public List<Long> getCanViewDepartmentId(DataScopeViewTypeEnum viewType, Long employeeId) {
        if (DataScopeViewTypeEnum.ME == viewType) {
            // 数据可见范围类型为本人时 不可以查看任何部门数据
            return Lists.newArrayList(0L);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT == viewType) {
            return this.getMeDepartmentIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT_AND_SUB == viewType) {
            return this.getDepartmentAndSubIdList(employeeId);
        }
        // 可以查看所有部门数据
        return Lists.newArrayList();
    }

    public List<Long> getMeDepartmentIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeMapper.selectById(employeeId);
        return Lists.newArrayList(employeeEntity.getDepartmentId());
    }

    public List<Long> getDepartmentAndSubIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeMapper.selectById(employeeId);
        return departmentService.selfAndChildrenIdList(employeeEntity.getDepartmentId());
    }

    /**
     * 根据员工id 获取各数据范围最大的可见范围 map<dataScopeType,viewType></>
     */
    public DataScopeViewTypeEnum getEmployeeDataScopeViewType(DataScopeTypeEnum dataScopeTypeEnum, Long employeeId) {
        EmployeeEntity employeeEntity = employeeMapper.selectById(employeeId);
        if (employeeEntity == null || employeeEntity.getEmployeeId() == null) {
            return DataScopeViewTypeEnum.ME;
        }

        // 如果是超级管理员 则可查看全部
        if (employeeEntity.getAdministratorFlag()) {
            return DataScopeViewTypeEnum.ALL;
        }

        List<Long> roleIdList = roleEmployeeMapper.selectRoleIdByEmployeeId(employeeId);
        //未设置角色 默认本人
        if (CollectionUtils.isEmpty(roleIdList)) {
            return DataScopeViewTypeEnum.ME;
        }
        //未设置角色数据范围 默认本人
        List<RoleDataScopeEntity> dataScopeRoleList = roleDataScopeMapper.listByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(dataScopeRoleList)) {
            return DataScopeViewTypeEnum.ME;
        }
        Map<Integer, List<RoleDataScopeEntity>> listMap = dataScopeRoleList.stream().collect(Collectors.groupingBy(RoleDataScopeEntity::getDataScopeType));
        List<RoleDataScopeEntity> viewLevelList = listMap.getOrDefault(dataScopeTypeEnum.getValue(), Lists.newArrayList());
        if (CollectionUtils.isEmpty(viewLevelList)) {
            return DataScopeViewTypeEnum.ME;
        }
        RoleDataScopeEntity maxLevel = viewLevelList.stream().max(Comparator.comparing(e -> EnumUtil.getEnumByValue(e.getViewType(), DataScopeViewTypeEnum.class).getLevel())).get();
        return EnumUtil.getEnumByValue(maxLevel.getViewType(), DataScopeViewTypeEnum.class);
    }

    /**
     * 获取本人相关 可查看员工id
     */
    private List<Long> getMeEmployeeIdList(Long employeeId) {
        return Lists.newArrayList(employeeId);
    }

    /**
     * 获取本部门相关 可查看员工id
     */
    private List<Long> getDepartmentEmployeeIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeMapper.selectById(employeeId);
        return employeeMapper.getEmployeeIdByDepartmentId(employeeEntity.getDepartmentId(), false);
    }

    /**
     * 获取本部门及下属子部门相关 可查看员工id
     */
    private List<Long> getDepartmentAndSubEmployeeIdList(Long employeeId) {
        List<Long> allDepartmentIds = getDepartmentAndSubIdList(employeeId);
        return employeeMapper.getEmployeeIdByDepartmentIdList(allDepartmentIds, false);
    }
}
