package com.lyusantu.easy.base.module.support.job.core;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.lyusantu.easy.base.module.support.job.constant.EasyJobConst;
import com.lyusantu.easy.base.module.support.job.constant.EasyJobUtil;
import com.lyusantu.easy.base.module.support.job.repository.EasyJobRepository;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobEntity;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobLogEntity;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.common.util.IpUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务 执行器
 */
@Slf4j
public class EasyJobExecutor implements Runnable {

    private final EasyJobEntity jobEntity;

    private final EasyJobRepository jobRepository;

    private final EasyJob jobInterface;

    private final RedissonClient redissonClient;

    private static final String EXECUTE_LOCK = "easy-job-lock-execute-";

    public EasyJobExecutor(EasyJobEntity jobEntity,
                           EasyJobRepository jobRepository,
                           EasyJob jobInterface,
                           RedissonClient redissonClient) {
        this.jobEntity = jobEntity;
        this.jobRepository = jobRepository;
        this.jobInterface = jobInterface;
        this.redissonClient = redissonClient;
    }

    /**
     * 系统线程执行
     */
    @Override
    public void run() {
        // 获取当前任务执行锁 最多持有30s自动释放
        Integer jobId = jobEntity.getJobId();
        RLock rLock = redissonClient.getLock(EXECUTE_LOCK + jobId);
        try {
            boolean lock = rLock.tryLock(0, 30, TimeUnit.SECONDS);
            if (!lock) {
                return;
            }
            // 查询上次执行时间 校验执行间隔
            EasyJobEntity dbJobEntity = jobRepository.getJobMapper().selectById(jobId);
            if (null == dbJobEntity) {
                return;
            }
            LocalDateTime lastExecuteTime = dbJobEntity.getLastExecuteTime();
            if (null != lastExecuteTime) {
                LocalDateTime nextTime = EasyJobUtil.queryNextTimeFromLast(jobEntity.getTriggerType(), jobEntity.getTriggerValue(), lastExecuteTime, 1).get(0);
                if (LocalDateTime.now().isBefore(nextTime)) {
                    return;
                }
            }
            // 执行任务
            EasyJobLogEntity logEntity = this.execute(EasyJobConst.SYSTEM_NAME);
            log.info("==== EasyJob ==== execute job->{},time-millis->{}ms", jobEntity.getJobName(), logEntity.getExecuteTimeMillis());
        } catch (Throwable t) {
            log.error("==== EasyJob ==== execute err:", t);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    /**
     * 执行任务
     *
     * @param executorName
     */
    public EasyJobLogEntity execute(String executorName) {
        // 保存执行记录
        LocalDateTime startTime = LocalDateTime.now();
        Long logId = this.saveLogBeforeExecute(jobEntity, executorName, startTime);

        // 执行计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 执行任务
        boolean successFlag = true;
        String executeResult;
        try {
            executeResult = jobInterface.run(jobEntity.getParam());
            stopWatch.stop();
        } catch (Throwable t) {
            stopWatch.stop();
            successFlag = false;
            // ps:异常信息不大于数据库字段长度限制
            executeResult = ExceptionUtil.stacktraceToString(t, 1800);
            log.error("==== EasyJob ==== execute err:", t);
        }

        // 更新执行记录
        EasyJobLogEntity logEntity = new EasyJobLogEntity();
        logEntity.setLogId(logId);
        logEntity.setSuccessFlag(successFlag);
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        logEntity.setExecuteTimeMillis(totalTimeMillis);
        logEntity.setExecuteEndTime(startTime.plus(totalTimeMillis, ChronoUnit.MILLIS));
        logEntity.setExecuteResult(executeResult);
        jobRepository.getJobLogMapper().updateById(logEntity);
        return logEntity;
    }

    /**
     * 执行前 保存执行记录
     *
     * @param jobEntity
     * @param executorName
     * @param executeTime
     * @return 返回执行记录id
     */
    private Long saveLogBeforeExecute(EasyJobEntity jobEntity,
                                      String executorName,
                                      LocalDateTime executeTime) {
        Integer jobId = jobEntity.getJobId();
        // 保存执行记录
        EasyJobLogEntity logEntity = new EasyJobLogEntity();
        logEntity.setJobId(jobId);
        logEntity.setJobName(jobEntity.getJobName());
        logEntity.setParam(jobEntity.getParam());
        logEntity.setSuccessFlag(true);
        // 执行开始时间
        logEntity.setExecuteStartTime(executeTime);
        logEntity.setExecuteEndTime(executeTime);
        logEntity.setExecuteTimeMillis(0L);
        logEntity.setCreateName(executorName);
        logEntity.setIp(IpUtil.getLocalFirstIp());
        logEntity.setProcessId(EasyJobUtil.getProcessId());
        logEntity.setProgramPath(EasyJobUtil.getProgramPath());

        // 更新最后执行时间
        EasyJobEntity updateJobEntity = new EasyJobEntity();
        updateJobEntity.setJobId(jobId);
        updateJobEntity.setLastExecuteTime(executeTime);
        jobRepository.saveLog(logEntity, updateJobEntity);
        return logEntity.getLogId();
    }

    /**
     * 查询 当前任务信息
     *
     * @return
     */
    public EasyJobEntity getJob() {
        return jobEntity;
    }
}
