package com.lyusantu.easy.base.module.support.job.api;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.lyusantu.easy.base.module.support.job.api.domain.EasyJobMsg;
import com.lyusantu.easy.base.module.support.job.config.EasyJobAutoConfiguration;
import com.lyusantu.easy.base.module.support.job.core.EasyJob;
import com.lyusantu.easy.base.module.support.job.core.EasyJobExecutor;
import com.lyusantu.easy.base.module.support.job.core.EasyJobLauncher;
import com.lyusantu.easy.base.module.support.job.repository.EasyJobRepository;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobEntity;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * EasyJob 执行端管理
 * 分布式系统之间 用发布/订阅消息的形式 来管理多个job
 */
@ConditionalOnBean(EasyJobAutoConfiguration.class)
@Slf4j
@Service
public class EasyJobClientManager {

    private final EasyJobLauncher jobLauncher;

    private final EasyJobRepository jobRepository;

    private final List<EasyJob> jobInterfaceList;

    private static final String EXECUTE_LOCK = "easy-job-lock-msg-execute-";

    private static final String TOPIC = "easy-job-instance";

    private final RedissonClient redissonClient;

    private final RTopic topic;

    private final EasyJobMsgListener jobMsgListener;

    public EasyJobClientManager(EasyJobLauncher jobLauncher,
                                EasyJobRepository jobRepository,
                                List<EasyJob> jobInterfaceList,
                                RedissonClient redissonClient) {
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
        this.jobInterfaceList = jobInterfaceList;
        this.redissonClient = redissonClient;

        // 添加监听器
        this.topic = redissonClient.getTopic(TOPIC);
        this.jobMsgListener = new EasyJobMsgListener();
        topic.addListener(EasyJobMsg.class, jobMsgListener);
        log.info("==== EasyJob ==== client-manager init");
    }

    /**
     * 发布消息
     */
    public void publishToClient(EasyJobMsg msgDTO) {
        msgDTO.setMsgId(IdUtil.fastSimpleUUID());
        topic.publish(msgDTO);
    }

    /**
     * 处理消息
     */
    private class EasyJobMsgListener implements MessageListener<EasyJobMsg> {

        @Override
        public void onMessage(CharSequence channel, EasyJobMsg msg) {
            log.info("==== EasyJob ==== on-message :{}", msg);
            // 判断消息类型 业务简单就直接判断 复杂的话可以策略模式
            EasyJobMsg.MsgTypeEnum msgType = msg.getMsgType();
            // 更新任务
            if (EasyJobMsg.MsgTypeEnum.UPDATE_JOB == msgType) {
                updateJob(msg.getJobId());
            }
            // 执行任务
            if (EasyJobMsg.MsgTypeEnum.EXECUTE_JOB == msgType) {
                executeJob(msg);
            }
        }
    }

    /**
     * 获取任务执行类
     *
     * @param jobClass
     * @return
     */
    private Optional<EasyJob> queryJobImpl(String jobClass) {
        return jobInterfaceList.stream().filter(e -> Objects.equals(e.getClassName(), jobClass)).findFirst();
    }

    /**
     * 更新任务
     *
     * @param jobId
     */
    private void updateJob(Integer jobId) {
        EasyJobEntity jobEntity = jobRepository.getJobMapper().selectById(jobId);
        if (null == jobEntity) {
            return;
        }
        jobLauncher.startOrRefreshJob(Lists.newArrayList(jobEntity));
    }

    /**
     * 立即执行任务
     *
     * @param msg
     */
    private void executeJob(EasyJobMsg msg) {
        Integer jobId = msg.getJobId();
        EasyJobEntity jobEntity = jobRepository.getJobMapper().selectById(jobId);
        if (null == jobEntity) {
            return;
        }
        // 获取定时任务实现类
        Optional<EasyJob> optional = this.queryJobImpl(jobEntity.getJobClass());
        if (!optional.isPresent()) {
            return;
        }

        // 获取执行锁 无需主动释放
        RLock rLock = redissonClient.getLock(EXECUTE_LOCK + msg.getMsgId());
        try {
            boolean getLock = rLock.tryLock(0, 20, TimeUnit.SECONDS);
            if (!getLock) {
                return;
            }
        } catch (InterruptedException e) {
            log.error("==== EasyJob ==== msg execute err:", e);
            return;
        }

        // 通过执行器 执行任务
        jobEntity.setParam(msg.getParam());
        EasyJobExecutor jobExecutor = new EasyJobExecutor(jobEntity, jobRepository, optional.get(), redissonClient);
        jobExecutor.execute(msg.getUpdateName());
    }


    @PreDestroy
    public void destroy() {
        topic.removeListener(jobMsgListener);
    }


}
