package com.lyusantu.easy.base.module.support.feedback.controller;

import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackQueryForm;
import com.lyusantu.easy.base.module.support.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackAddForm;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 意见反馈
 */
@Slf4j
@Tag(name = SwaggerTagConst.Support.FEEDBACK)
@RequiredArgsConstructor
@RestController
public class FeedbackController extends SupportBaseController {

    private final FeedbackService feedbackService;

    @Operation(summary = "意见反馈-分页查询")
    @PostMapping("/feedback/query")
    public ResponseDTO<PageResult<FeedbackVO>> query(@RequestBody @Valid FeedbackQueryForm queryForm) {
        return feedbackService.query(queryForm);
    }

    @Operation(summary = "意见反馈-新增")
    @PostMapping("/feedback/add")
    public ResponseDTO<String> add(@RequestBody @Valid FeedbackAddForm addForm) {
        RequestUser employee = RequestUtil.getRequestUser();
        return feedbackService.add(addForm, employee);
    }
}
