package com.lyusantu.easy.admin.module.system.support;

import com.lyusantu.easy.base.module.support.job.api.domain.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.job.api.EasyJobService;
import com.lyusantu.easy.base.module.support.job.config.EasyJobAutoConfiguration;
import com.lyusantu.easy.base.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务 管理接口
 */
@Tag(name = SwaggerTagConst.Support.JOB)
@RequiredArgsConstructor
@RestController
@ConditionalOnBean(EasyJobAutoConfiguration.class)
public class AdminEasyJobController extends SupportBaseController {

    private final EasyJobService jobService;

    @Operation(summary = "定时任务-立即执行")
    @PostMapping("/job/execute")
    @RepeatSubmit
    public ResponseDTO<String> execute(@RequestBody @Valid EasyJobExecuteForm executeForm) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        executeForm.setUpdateName(requestUser.getUserName());
        return jobService.execute(executeForm);
    }

    @Operation(summary = "定时任务-查询详情")
    @GetMapping("/job/{jobId}")
    public ResponseDTO<EasyJobVO> queryJobInfo(@PathVariable Integer jobId) {
        return jobService.queryJobInfo(jobId);
    }

    @Operation(summary = "定时任务-分页查询")
    @PostMapping("/job/query")
    public ResponseDTO<PageResult<EasyJobVO>> queryJob(@RequestBody @Valid EasyJobQueryForm queryForm) {
        return jobService.queryJob(queryForm);
    }

    @Operation(summary = "定时任务-添加任务")
    @PostMapping("/job/add")
    @RepeatSubmit
    public ResponseDTO<String> addJob(@RequestBody @Valid EasyJobAddForm addForm) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        addForm.setUpdateName(requestUser.getUserName());
        return jobService.addJob(addForm);
    }

    @Operation(summary = "定时任务-更新-任务信息")
    @PostMapping("/job/update")
    @RepeatSubmit
    public ResponseDTO<String> updateJob(@RequestBody @Valid EasyJobUpdateForm updateForm) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        updateForm.setUpdateName(requestUser.getUserName());
        return jobService.updateJob(updateForm);
    }

    @Operation(summary = "定时任务-更新-开启状态")
    @PostMapping("/job/update/enabled")
    @RepeatSubmit
    public ResponseDTO<String> updateJobEnabled(@RequestBody @Valid EasyJobEnabledUpdateForm updateForm) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        updateForm.setUpdateName(requestUser.getUserName());
        return jobService.updateJobEnabled(updateForm);
    }

    @Operation(summary = "定时任务-删除")
    @GetMapping("/job/delete")
    @RepeatSubmit
    public ResponseDTO<String> deleteJob(@RequestParam Integer jobId) {
        return jobService.deleteJob(jobId, RequestUtil.getRequestUser());
    }

    @Operation(summary = "定时任务-执行记录-分页查询")
    @PostMapping("/job/log/query")
    public ResponseDTO<PageResult<EasyJobLogVO>> queryJobLog(@RequestBody @Valid EasyJobLogQueryForm queryForm) {
        return jobService.queryJobLog(queryForm);
    }
}
