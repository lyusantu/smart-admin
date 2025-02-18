package com.lyusantu.easy.admin.module.system.datascope.strategy;

import com.lyusantu.easy.admin.module.system.datascope.domain.DataScopeSqlConfig;
import com.lyusantu.easy.admin.module.system.datascope.constant.DataScopeViewTypeEnum;

import java.util.Map;

/**
 * 数据范围策略 ,使用DataScopeWhereInTypeEnum.CUSTOM_STRATEGY类型，DataScope注解的joinSql属性无用
 */
public abstract class AbstractDataScopeStrategy {

    /**
     * 获取joinsql 字符串
     */
    public abstract String getCondition(DataScopeViewTypeEnum viewTypeEnum, Map<String, Object> paramMap, DataScopeSqlConfig sqlConfigDTO);
}
