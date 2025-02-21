package com.lyusantu.easy.admin.module.pm.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyusantu.easy.admin.module.pm.project.domain.entity.ProjectNodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 项目节点表 Mapper
 *
 * @Author lyusantu
 * @Date 2025-02-20 14:58:16
 * @Copyright lyusantu
 */
@Mapper
@Component
public interface ProjectNodeMapper extends BaseMapper<ProjectNodeEntity> {

}
