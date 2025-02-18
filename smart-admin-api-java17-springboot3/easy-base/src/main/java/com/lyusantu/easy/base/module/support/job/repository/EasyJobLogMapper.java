package com.lyusantu.easy.base.module.support.job.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobLogEntity;
import com.lyusantu.easy.base.module.support.job.api.domain.EasyJobLogQueryForm;
import com.lyusantu.easy.base.module.support.job.api.domain.EasyJobLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务-执行记录 Mapper
 */
@Mapper
@Component
public interface EasyJobLogMapper extends BaseMapper<EasyJobLogEntity> {

    /**
     * 定时任务-执行记录-分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<EasyJobLogVO> query(Page<?> page, @Param("query") EasyJobLogQueryForm queryForm);
}
