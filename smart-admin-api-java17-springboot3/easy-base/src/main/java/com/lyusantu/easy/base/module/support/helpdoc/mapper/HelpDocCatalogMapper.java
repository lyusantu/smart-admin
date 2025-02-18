package com.lyusantu.easy.base.module.support.helpdoc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyusantu.easy.base.module.support.helpdoc.domain.entity.HelpDocCatalogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 帮助文档目录
 */
@Mapper
@Component
public interface HelpDocCatalogMapper extends BaseMapper<HelpDocCatalogEntity> {

}
