package net.lab1024.sa.admin.module.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEntity;

/**
 * 角色 Mapper
 */
@Mapper
@Component
public interface RoleMapper extends BaseMapper<RoleEntity> {

    /**
     * 根据角色名称查询
     */
    RoleEntity getByRoleName(@Param("roleName") String roleName);

    /**
     * 根据角色编码
     */
    RoleEntity getByRoleCode(@Param("roleCode") String roleCode);
}
