package com.lyusantu.easy.admin.module.pm.project.mapper;

import com.lyusantu.easy.admin.module.pm.project.domain.entity.ProjectStageEntity;
import com.lyusantu.easy.admin.module.pm.project.domain.form.ProjectStageQueryForm;
import com.lyusantu.easy.admin.module.pm.project.domain.vo.ProjectStageVO;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 项目阶段表 Mapper
 *
 * @Author lyusantu
 * @Date 2025-02-21 11:10:50
 * @Copyright lyusantu
 */

@Mapper
@Component
public interface ProjectStageMapper extends BaseMapper<ProjectStageEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ProjectStageVO> queryPage(Page page, @Param("queryForm") ProjectStageQueryForm queryForm);

}
