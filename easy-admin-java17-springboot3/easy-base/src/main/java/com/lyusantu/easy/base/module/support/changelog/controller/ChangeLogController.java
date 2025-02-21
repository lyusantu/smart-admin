package com.lyusantu.easy.base.module.support.changelog.controller;

import com.lyusantu.easy.base.module.support.changelog.service.ChangeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.changelog.domain.form.ChangeLogQueryForm;
import com.lyusantu.easy.base.module.support.changelog.domain.vo.ChangeLogVO;
import org.springframework.web.bind.annotation.*;

/**
 * 系统更新日志 Controller
 */
@RequiredArgsConstructor
@RestController
@Tag(name = SwaggerTagConst.Support.CHANGE_LOG)
public class ChangeLogController extends SupportBaseController {

    private final ChangeLogService changeLogService;

    @Operation(summary = "分页查询")
    @PostMapping("/changeLog/queryPage")
    public ResponseDTO<PageResult<ChangeLogVO>> queryPage(@RequestBody @Valid ChangeLogQueryForm queryForm) {
        return ResponseDTO.ok(changeLogService.queryPage(queryForm));
    }


    @Operation(summary = "变更内容详情")
    @GetMapping("/changeLog/getDetail/{changeLogId}")
    public ResponseDTO<ChangeLogVO> getDetail(@PathVariable Long changeLogId) {
        return ResponseDTO.ok(changeLogService.getById(changeLogId));
    }
}
