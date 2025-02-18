package com.lyusantu.easy.base.module.support.job.repository;

import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobEntity;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * job 持久化业务
 */
@Service
public class EasyJobRepository {

    @Autowired
    private EasyJobMapper jobMapper;

    @Autowired
    private EasyJobLogMapper jobLogMapper;

    public EasyJobMapper getJobMapper() {
        return jobMapper;
    }

    public EasyJobLogMapper getJobLogMapper() {
        return jobLogMapper;
    }

    /**
     * 保存执行记录
     *
     * @param logEntity
     * @param jobEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveLog(EasyJobLogEntity logEntity, EasyJobEntity jobEntity) {
        jobLogMapper.insert(logEntity);

        jobEntity.setLastExecuteLogId(logEntity.getLogId());
        jobMapper.updateById(jobEntity);
    }
}
