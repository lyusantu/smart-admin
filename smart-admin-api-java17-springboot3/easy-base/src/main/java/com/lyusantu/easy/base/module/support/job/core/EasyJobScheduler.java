package com.lyusantu.easy.base.module.support.job.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lyusantu.easy.base.module.support.job.config.EasyJobConfig;
import com.lyusantu.easy.base.module.support.job.constant.EasyJobTriggerTypeEnum;
import com.lyusantu.easy.base.module.support.job.constant.EasyJobUtil;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 定时任务 调度管理
 */
@Slf4j
public class EasyJobScheduler {

    /**
     * Spring线程池任务调度器
     */
    private static ThreadPoolTaskScheduler TASK_SCHEDULER;

    /**
     * 定时任务 map
     */
    private static Map<Integer, Pair<EasyJobEntity, ScheduledFuture<?>>> JOB_FUTURE_MAP;

    private EasyJobScheduler() {

    }

    /**
     * 初始化任务调度配置
     */
    public static void init(EasyJobConfig config) {
        TASK_SCHEDULER = new ThreadPoolTaskScheduler();
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("EasyJobExecutor-%d").build();
        TASK_SCHEDULER.setThreadFactory(threadFactory);
        TASK_SCHEDULER.setPoolSize(config.getCorePoolSize());
        // 线程池在关闭时会等待所有任务完成
        TASK_SCHEDULER.setWaitForTasksToCompleteOnShutdown(true);
        // 在调用shutdown方法后，等待任务完成的最长时间
        TASK_SCHEDULER.setAwaitTerminationSeconds(10);
        // 错误处理
        TASK_SCHEDULER.setErrorHandler((t) -> log.error("EasyJobExecute Err:", t));
        // 当一个任务在被调度执行前被取消时，是否应该从线程池的任务队列中移除
        TASK_SCHEDULER.setRemoveOnCancelPolicy(true);
        TASK_SCHEDULER.initialize();

        JOB_FUTURE_MAP = new ConcurrentHashMap<>();
    }

    /**
     * 获取任务执行对象
     *
     * @param jobId
     * @return
     */
    public static ScheduledFuture<?> getJobFuture(Integer jobId) {
        Pair<EasyJobEntity, ScheduledFuture<?>> pair = JOB_FUTURE_MAP.get(jobId);
        if (null == pair) {
            return null;
        }
        return pair.getRight();
    }

    /**
     * 获取当前所有执行任务
     *
     * @return
     */
    public static List<EasyJobEntity> getJobInfo() {
        return JOB_FUTURE_MAP.values().stream().map(Pair::getLeft).collect(Collectors.toList());
    }

    /**
     * 获取任务执行实体类
     *
     * @param jobId
     * @return
     */
    public static EasyJobEntity getJobInfo(Integer jobId) {
        Pair<EasyJobEntity, ScheduledFuture<?>> pair = JOB_FUTURE_MAP.get(jobId);
        if (null == pair) {
            return null;
        }
        return pair.getLeft();
    }

    /**
     * 添加任务
     *
     * @param jobExecute
     * @return
     */
    public static void addJob(EasyJobExecutor jobExecute) {
        // 任务是否存在
        EasyJobEntity jobEntity = jobExecute.getJob();
        Integer jobId = jobEntity.getJobId();
        if (JOB_FUTURE_MAP.containsKey(jobId)) {
            // 移除任务
            removeJob(jobId);
        }
        // 任务触发类型
        Trigger trigger = null;
        String triggerType = jobEntity.getTriggerType();
        String triggerValue = jobEntity.getTriggerValue();
        // 优先 cron 表达式
        if (EasyJobTriggerTypeEnum.CRON.equalsValue(triggerType)) {
            trigger = new CronTrigger(triggerValue);
        } else if (EasyJobTriggerTypeEnum.FIXED_DELAY.equalsValue(triggerType)) {
            trigger = new PeriodicTrigger(EasyJobUtil.getFixedDelayVal(triggerValue), TimeUnit.SECONDS);
        }
        String jobName = jobEntity.getJobName();
        if (null == trigger) {
            log.error("==== EasyJob ==== trigger-value not null {}", jobName);
            return;
        }
        // 执行任务
        ScheduledFuture<?> schedule = TASK_SCHEDULER.schedule(jobExecute, trigger);
        JOB_FUTURE_MAP.put(jobId, Pair.of(jobEntity, schedule));
        log.info("==== EasyJob ==== add job:{}", jobName);
    }

    /**
     * 移除任务
     * 等待任务执行完成后移除
     *
     * @param jobId
     */
    public static void removeJob(Integer jobId) {
        ScheduledFuture<?> jobFuture = getJobFuture(jobId);
        if (null == jobFuture) {
            return;
        }
        // 结束任务
        stopJob(jobFuture);
        JOB_FUTURE_MAP.remove(jobId);
        log.info("==== EasyJob ==== remove job:{}", jobId);
    }

    /**
     * 停止所有定时任务
     */
    public static void destroy() {
        // 启动一个有序的关闭过程,在这个过程中,不再接受新的任务提交,但已提交的任务（包括正在执行的和队列中等待的）会被允许执行完成。
        TASK_SCHEDULER.destroy();
        JOB_FUTURE_MAP.clear();
    }

    /**
     * 结束任务
     * 如果任务还没有开始执行，会直接被取消。
     * 如果任务已经开始执行，此时不会中断执行中的线程，任务会执行完成再被取消
     *
     * @param scheduledFuture
     */
    private static void stopJob(ScheduledFuture<?> scheduledFuture) {
        if (null == scheduledFuture || scheduledFuture.isCancelled()) {
            return;
        }
        scheduledFuture.cancel(false);
    }
}
