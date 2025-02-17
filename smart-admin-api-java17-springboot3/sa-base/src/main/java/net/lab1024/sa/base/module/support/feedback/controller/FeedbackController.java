package net.lab1024.sa.base.module.support.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.RequestUtil;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.feedback.domain.FeedbackAddForm;
import net.lab1024.sa.base.module.support.feedback.domain.FeedbackQueryForm;
import net.lab1024.sa.base.module.support.feedback.domain.FeedbackVO;
import net.lab1024.sa.base.module.support.feedback.service.FeedbackService;
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
