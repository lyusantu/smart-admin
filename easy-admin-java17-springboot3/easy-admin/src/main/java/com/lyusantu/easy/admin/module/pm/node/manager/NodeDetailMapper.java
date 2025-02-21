package com.lyusantu.easy.admin.module.pm.node.manager;

import com.lyusantu.easy.admin.module.pm.node.domain.entity.NodeDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 界面模板详情表 Mapper
 *
 * @Author lyusantu
 * @Date 2025-02-19 17:09:17
 * @Copyright lyusantu
 */

@Mapper
@Component
public interface NodeDetailMapper extends BaseMapper<NodeDetailEntity> {

}
