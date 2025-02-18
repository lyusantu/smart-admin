package com.lyusantu.easy.base.module.support.job.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lyusantu.easy.base.module.support.job.api.domain.*;
import com.lyusantu.easy.base.module.support.job.config.EasyJobAutoConfiguration;
import com.lyusantu.easy.base.module.support.job.constant.EasyJobTriggerTypeEnum;
import com.lyusantu.easy.base.module.support.job.constant.EasyJobUtil;
import com.lyusantu.easy.base.module.support.job.repository.EasyJobLogMapper;
import com.lyusantu.easy.base.module.support.job.repository.EasyJobMapper;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobEntity;
import com.lyusantu.easy.base.module.support.job.repository.domain.EasyJobLogEntity;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定时任务 接口业务管理
 * 如果不需要通过接口管理定时任务 可以删除此类
 */
@ConditionalOnBean(EasyJobAutoConfiguration.class)
@RequiredArgsConstructor
@Service
public class EasyJobService {

    private final EasyJobMapper jobMapper;

    private final EasyJobLogMapper jobLogMapper;

    private final EasyJobClientManager jobClientManager;

    /**
     * 查询 定时任务详情
     *
     * @param jobId
     * @return
     */
    public ResponseDTO<EasyJobVO> queryJobInfo(Integer jobId) {
        EasyJobEntity jobEntity = jobMapper.selectById(jobId);
        if (null == jobEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EasyJobVO jobVO = BeanUtil.copy(jobEntity, EasyJobVO.class);
        // 处理设置job详情
        this.handleJobInfo(Lists.newArrayList(jobVO));
        return ResponseDTO.ok(jobVO);
    }

    /**
     * 分页查询 定时任务
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<EasyJobVO>> queryJob(EasyJobQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<EasyJobVO> jobList = jobMapper.query(page, queryForm);
        PageResult<EasyJobVO> pageResult = PageUtil.convert2PageResult(page, jobList);
        // 处理设置job详情
        this.handleJobInfo(jobList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 处理设置 任务信息
     *
     * @param jobList
     */
    private void handleJobInfo(List<EasyJobVO> jobList) {
        if (CollectionUtils.isEmpty(jobList)) {
            return;
        }
        // 查询最后一次执行记录
        List<Long> logIdList = jobList.stream().map(EasyJobVO::getLastExecuteLogId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, EasyJobLogVO> lastLogMap = Collections.emptyMap();
        if (CollectionUtils.isNotEmpty(logIdList)) {
            lastLogMap = jobLogMapper.selectBatchIds(logIdList)
                    .stream()
                    .collect(Collectors.toMap(EasyJobLogEntity::getLogId, e -> BeanUtil.copy(e, EasyJobLogVO.class)));
        }

        // 循环处理任务信息
        for (EasyJobVO jobVO : jobList) {
            // 设置最后一次执行记录
            Long lastExecuteLogId = jobVO.getLastExecuteLogId();
            if (null != lastExecuteLogId) {
                jobVO.setLastJobLog(lastLogMap.get(lastExecuteLogId));
            }
            // 计算未来5次执行时间
            if (jobVO.getEnabledFlag()) {
                List<LocalDateTime> nextTimeList = EasyJobUtil.queryNextTimeFromNow(jobVO.getTriggerType(), jobVO.getTriggerValue(), jobVO.getLastExecuteTime(), 5);
                jobVO.setNextJobExecuteTimeList(nextTimeList);
            }
        }
    }

    /**
     * 分页查询 定时任务-执行记录
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<EasyJobLogVO>> queryJobLog(EasyJobLogQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<EasyJobLogVO> jobList = jobLogMapper.query(page, queryForm);
        PageResult<EasyJobLogVO> pageResult = PageUtil.convert2PageResult(page, jobList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 添加定时任务
     *
     * @param addForm
     * @return
     */
    public synchronized ResponseDTO<String> addJob(EasyJobAddForm addForm) {
        // 校验参数
        ResponseDTO<String> checkRes = this.checkParam(addForm);
        if (!checkRes.getOk()) {
            return checkRes;
        }

        // 校验重复的执行类
        EasyJobEntity existJobClass = jobMapper.selectByJobClass(addForm.getJobClass());
        if (null != existJobClass && !existJobClass.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("已经存在相同的执行类");
        }

        // 添加数据
        EasyJobEntity jobEntity = BeanUtil.copy(addForm, EasyJobEntity.class);
        jobMapper.insert(jobEntity);

        // 更新执行端
        EasyJobMsg jobMsg = new EasyJobMsg();
        jobMsg.setJobId(jobEntity.getJobId());
        jobMsg.setMsgType(EasyJobMsg.MsgTypeEnum.UPDATE_JOB);
        jobMsg.setUpdateName(addForm.getUpdateName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

    /**
     * 更新定时任务
     *
     * @param updateForm
     * @return
     */
    public synchronized ResponseDTO<String> updateJob(EasyJobUpdateForm updateForm) {
        // 校验参数
        Integer jobId = updateForm.getJobId();
        EasyJobEntity jobEntity = jobMapper.selectById(jobId);
        if (null == jobEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        ResponseDTO<String> checkRes = this.checkParam(updateForm);
        if (!checkRes.getOk()) {
            return checkRes;
        }

        // 校验重复的执行类
        EasyJobEntity existJobClass = jobMapper.selectByJobClass(updateForm.getJobClass());
        if (null != existJobClass && !existJobClass.getDeletedFlag() && !existJobClass.getJobId().equals(jobId)) {
            return ResponseDTO.userErrorParam("已经存在相同的执行类");
        }

        // 更新数据
        jobEntity = BeanUtil.copy(updateForm, EasyJobEntity.class);
        jobMapper.updateById(jobEntity);

        // 更新执行端
        EasyJobMsg jobMsg = new EasyJobMsg();
        jobMsg.setJobId(jobId);
        jobMsg.setMsgType(EasyJobMsg.MsgTypeEnum.UPDATE_JOB);
        jobMsg.setUpdateName(updateForm.getUpdateName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

    /**
     * 校验参数
     * 如需其他校验，请自行添加校验逻辑
     *
     * @param addForm
     * @return
     */
    private ResponseDTO<String> checkParam(EasyJobAddForm addForm) {
        // 校验触发时间配置
        String triggerType = addForm.getTriggerType();
        String triggerValue = addForm.getTriggerValue();
        if (EasyJobTriggerTypeEnum.CRON.equalsValue(triggerType) && !EasyJobUtil.checkCron(triggerValue)) {
            return ResponseDTO.userErrorParam("cron表达式错误");
        }
        if (EasyJobTriggerTypeEnum.FIXED_DELAY.equalsValue(triggerType) && !EasyJobUtil.checkFixedDelay(triggerValue)) {
            return ResponseDTO.userErrorParam("固定间隔配置错误：必须是大于0的整数");
        }
        // 校验job class
        return EasyJobUtil.checkJobClass(addForm.getJobClass());
    }

    /**
     * 更新定时任务-是否开启
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> updateJobEnabled(EasyJobEnabledUpdateForm updateForm) {
        Integer jobId = updateForm.getJobId();
        EasyJobEntity jobEntity = jobMapper.selectById(jobId);
        if (null == jobEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        Boolean enabledFlag = updateForm.getEnabledFlag();
        if (Objects.equals(enabledFlag, jobEntity.getEnabledFlag())) {
            return ResponseDTO.ok();
        }
        // 更新数据
        jobEntity = new EasyJobEntity();
        jobEntity.setJobId(jobId);
        jobEntity.setEnabledFlag(enabledFlag);
        jobEntity.setUpdateName(updateForm.getUpdateName());
        jobMapper.updateById(jobEntity);

        // 更新执行端
        EasyJobMsg jobMsg = new EasyJobMsg();
        jobMsg.setJobId(jobId);
        jobMsg.setMsgType(EasyJobMsg.MsgTypeEnum.UPDATE_JOB);
        jobMsg.setUpdateName(updateForm.getUpdateName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

    /**
     * 执行定时任务
     * 忽略任务的开启状态,立即执行一次
     *
     * @param executeForm
     * @return
     */
    public ResponseDTO<String> execute(EasyJobExecuteForm executeForm) {
        Integer jobId = executeForm.getJobId();
        EasyJobEntity jobEntity = jobMapper.selectById(jobId);
        if (null == jobEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 更新执行端
        EasyJobMsg jobMsg = new EasyJobMsg();
        jobMsg.setJobId(jobId);
        jobMsg.setParam(executeForm.getParam());
        jobMsg.setMsgType(EasyJobMsg.MsgTypeEnum.EXECUTE_JOB);
        jobMsg.setUpdateName(executeForm.getUpdateName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

    /**
     * 移除定时任务
     * 物理删除
     *
     * @return
     * @author huke
     */
    public synchronized ResponseDTO<String> deleteJob(Integer jobId, RequestUser requestUser) {
        // 删除任务
        jobMapper.updateDeletedFlag(jobId, Boolean.TRUE);

        // 更新执行端
        EasyJobMsg jobMsg = new EasyJobMsg();
        jobMsg.setJobId(jobId);
        jobMsg.setMsgType(EasyJobMsg.MsgTypeEnum.UPDATE_JOB);
        jobMsg.setUpdateName(requestUser.getUserName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

}
