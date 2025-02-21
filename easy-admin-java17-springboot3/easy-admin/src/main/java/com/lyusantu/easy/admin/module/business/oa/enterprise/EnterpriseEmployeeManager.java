package com.lyusantu.easy.admin.module.business.oa.enterprise;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyusantu.easy.admin.module.business.oa.enterprise.mapper.EnterpriseEmployeeMapper;
import com.lyusantu.easy.admin.module.business.oa.enterprise.domain.entity.EnterpriseEmployeeEntity;
import org.springframework.stereotype.Service;

/**
 * 企业员工关系 manager
 */
@Service
public class EnterpriseEmployeeManager extends ServiceImpl<EnterpriseEmployeeMapper, EnterpriseEmployeeEntity> {
}
