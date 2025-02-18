package net.lab1024.sa.base.module.support.job.repository;

import net.lab1024.sa.base.module.support.job.repository.domain.SmartJobEntity;
import net.lab1024.sa.base.module.support.job.repository.domain.SmartJobLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * job 持久化业务
 */
@Service
public class SmartJobRepository {

    @Autowired
    private SmartJobMapper jobMapper;

    @Autowired
    private SmartJobLogMapper jobLogMapper;

    public SmartJobMapper getJobMapper() {
        return jobMapper;
    }

    public SmartJobLogMapper getJobLogMapper() {
        return jobLogMapper;
    }

    /**
     * 保存执行记录
     *
     * @param logEntity
     * @param jobEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveLog(SmartJobLogEntity logEntity, SmartJobEntity jobEntity) {
        jobLogMapper.insert(logEntity);

        jobEntity.setLastExecuteLogId(logEntity.getLogId());
        jobMapper.updateById(jobEntity);
    }
}
