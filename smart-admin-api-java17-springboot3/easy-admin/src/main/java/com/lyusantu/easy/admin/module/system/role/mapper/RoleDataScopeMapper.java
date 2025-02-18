package com.lyusantu.easy.admin.module.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyusantu.easy.admin.module.system.role.domain.entity.RoleDataScopeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 角色 数据权限 Mapper
 */
@Mapper
@Component
public interface RoleDataScopeMapper extends BaseMapper<RoleDataScopeEntity> {

    /**
     * 获取某个角色的设置信息
     */
    List<RoleDataScopeEntity> listByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取某批角色的所有数据范围配置信息
     */
    List<RoleDataScopeEntity> listByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    /**
     * 删除某个角色的设置信息
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

}
