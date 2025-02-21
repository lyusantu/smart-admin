package com.lyusantu.easy.base.module.support.job.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.job.api.domain.EasyJobQueryForm;
import com.lyusantu.easy.base.module.support.job.api.domain.EasyJobVO;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务 Mapper
 */
@Mapper
@Component
public interface EasyJobMapper extends BaseMapper<EasyJobEntity> {

    /**
     * 定时任务-分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<EasyJobVO> query(Page<?> page, @Param("query") EasyJobQueryForm queryForm);

    /**
     * 假删除
     *
     * @param jobId
     * @return
     */
    void updateDeletedFlag(@Param("jobId") Integer jobId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据 任务class 查找
     *
     * @param jobClass
     * @return
     */
    EasyJobEntity selectByJobClass(@Param("jobClass") String jobClass);
}
