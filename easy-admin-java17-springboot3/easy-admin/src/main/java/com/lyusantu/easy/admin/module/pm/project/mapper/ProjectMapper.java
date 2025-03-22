package com.lyusantu.easy.admin.module.pm.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.admin.module.pm.project.domain.entity.ProjectEntity;
import com.lyusantu.easy.admin.module.pm.project.domain.form.ProjectQueryForm;
import com.lyusantu.easy.admin.module.pm.project.domain.vo.MyTaskVO;
import com.lyusantu.easy.admin.module.pm.project.domain.vo.ProjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目信息表 Mapper
 *
 * @Author lyusantu
 * @Date 2025-02-20 10:29:51
 * @Copyright lyusantu
 */

@Mapper
@Component
public interface ProjectMapper extends BaseMapper<ProjectEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ProjectVO> queryPage(Page page, @Param("queryForm") ProjectQueryForm queryForm);

    /**
     * 我的任务
     */
    List<MyTaskVO> myTask(@Param("userId") Long userId);

}
