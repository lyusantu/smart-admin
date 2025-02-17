package net.lab1024.sa.admin.module.system.role.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.system.role.mapper.RoleEmployeeMapper;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEmployeeEntity;
import org.springframework.stereotype.Service;

/**
 * 角色员工 manager
 */
@Service
public class RoleEmployeeManager extends ServiceImpl<RoleEmployeeMapper, RoleEmployeeEntity> {

}
