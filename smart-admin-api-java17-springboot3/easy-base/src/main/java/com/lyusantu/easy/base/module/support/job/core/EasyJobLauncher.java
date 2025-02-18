package com.lyusantu.easy.base.module.support.job.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lyusantu.easy.base.module.support.job.constant.EasyJobConst;
import com.lyusantu.easy.base.module.support.job.constant.EasyJobUtil;
import com.lyusantu.easy.base.module.support.job.repository.EasyJobRepository;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobEntity;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.module.support.job.config.EasyJobConfig;
import org.redisson.api.RedissonClient;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 定时任务 作业启动类
 */
@Slf4j
public class EasyJobLauncher {

    private final EasyJobRepository jobRepository;

    private final List<EasyJob> jobInterfaceList;

    private final RedissonClient redissonClient;

    public EasyJobLauncher(EasyJobConfig jobConfig,
                           EasyJobRepository jobRepository,
                           List<EasyJob> jobInterfaceList,
                           RedissonClient redissonClient) {
        this.jobRepository = jobRepository;
        this.jobInterfaceList = jobInterfaceList;
        this.redissonClient = redissonClient;

        // init job scheduler
        EasyJobScheduler.init(jobConfig);

        // 任务自动检测配置 固定1个线程
        Integer initDelay = jobConfig.getInitDelay();
        Boolean refreshEnabled = jobConfig.getDbRefreshEnabled();
        Integer refreshInterval = jobConfig.getDbRefreshInterval();

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("EasyJobLauncher-%d").build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, factory);
        Runnable launcherRunnable = () -> {
            try {
                // 查询所有任务
                List<EasyJobEntity> jobList = this.queryJob();
                this.startOrRefreshJob(jobList);
            } catch (Throwable t) {
                log.error("EasyJob Error:", t);
            }
            // 只在启动时 执行一次
            if (!refreshEnabled) {
                executor.shutdown();
            }
        };
        executor.scheduleWithFixedDelay(launcherRunnable, initDelay, refreshInterval, TimeUnit.SECONDS);

        // 打印信息
        String refreshDesc = refreshEnabled ? "开启|检测间隔" + refreshInterval + "秒" : "关闭";
        String format = String.format(EasyJobConst.LOGO, jobConfig.getCorePoolSize(), initDelay, refreshDesc);
        EasyJobUtil.printInfo(format);
    }

    /**
     * 查询数据库
     * 启动/刷新任务
     */
    public void startOrRefreshJob(List<EasyJobEntity> JobList) {
        // 查询任务配置
        if (CollectionUtils.isEmpty(JobList) || CollectionUtils.isEmpty(jobInterfaceList)) {
            log.info("==== EasyJob ==== job list empty");
            return;
        }

        // 任务实现类
        Map<String, EasyJob> jobImplMap = jobInterfaceList.stream().collect(Collectors.toMap(EasyJob::getClassName, Function.identity()));
        for (EasyJobEntity jobEntity : JobList) {
            // 任务是否存在 判断是否需要更新
            Integer jobId = jobEntity.getJobId();
            EasyJobEntity oldJobEntity = EasyJobScheduler.getJobInfo(jobId);
            if (null != oldJobEntity) {
                // 不需要更新
                if (!isNeedUpdate(oldJobEntity, jobEntity)) {
                    continue;
                }
                // 需要更新 移除原任务
                EasyJobScheduler.removeJob(jobId);
            }
            // 任务未开启
            if (!jobEntity.getEnabledFlag()) {
                continue;
            }
            // 任务删除
            if (jobEntity.getDeletedFlag()) {
                continue;
            }
            // 查找任务实现类
            EasyJob jobImpl = jobImplMap.get(jobEntity.getJobClass());
            if (null == jobImpl) {
                continue;
            }
            // 添加任务
            EasyJobExecutor jobExecute = new EasyJobExecutor(jobEntity, jobRepository, jobImpl, redissonClient);
            EasyJobScheduler.addJob(jobExecute);
        }
        List<EasyJobEntity> runjJobList = EasyJobScheduler.getJobInfo();
        List<String> jobNameList = runjJobList.stream().map(EasyJobEntity::getJobName).collect(Collectors.toList());
        log.info("==== EasyJob ==== start/refresh job num:{}->{}", runjJobList.size(), jobNameList);
    }

    /**
     * 查询全部任务
     *
     * @return
     */
    private List<EasyJobEntity> queryJob() {
        return jobRepository.getJobMapper().selectList(null);
    }

    /**
     * 手动判断 任务配置 是否需要更新
     * 新增字段的话 在这个方法里增加判断
     *
     * @return
     */
    private static boolean isNeedUpdate(EasyJobEntity oldJob, EasyJobEntity newJob) {
        // cron为空时 fixedDelay 才有意义
        return !Objects.equals(oldJob.getEnabledFlag(), newJob.getEnabledFlag())
                || !Objects.equals(oldJob.getDeletedFlag(), newJob.getDeletedFlag())
                || !Objects.equals(oldJob.getTriggerType(), newJob.getTriggerType())
                || !Objects.equals(oldJob.getTriggerValue(), newJob.getTriggerValue())
                || !Objects.equals(oldJob.getJobClass(), newJob.getJobClass());
    }

    @PreDestroy
    public void destroy() {
        EasyJobScheduler.destroy();
        log.info("==== EasyJob ==== destroy job");
    }
}
