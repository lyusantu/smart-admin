package com.lyusantu.easy.base.module.support.helpdoc.controller;

import com.lyusantu.easy.base.module.support.helpdoc.service.HelpDocCatalogService;
import com.lyusantu.easy.base.module.support.helpdoc.service.HelpDocUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.helpdoc.domain.form.HelpDocViewRecordQueryForm;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocCatalogVO;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocDetailVO;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocVO;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocViewRecordVO;
import com.lyusantu.easy.base.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 帮助文档
 */
@Tag(name = SwaggerTagConst.Support.HELP_DOC)
@RequiredArgsConstructor
@RestController
public class HelpDocController extends SupportBaseController {

    private final HelpDocCatalogService helpDocCatalogService;

    private final HelpDocUserService helpDocUserService;

    // --------------------- 帮助文档 【目录】 -------------------------

    @Operation(summary = "帮助文档目录-获取全部")
    @GetMapping("/helpDoc/helpDocCatalog/getAll")
    public ResponseDTO<List<HelpDocCatalogVO>> getAll() {
        return ResponseDTO.ok(helpDocCatalogService.getAll());
    }

    // --------------------- 帮助文档 【用户】-------------------------

    @Operation(summary = "【用户】帮助文档-查看详情")
    @GetMapping("/helpDoc/user/view/{helpDocId}")
    @RepeatSubmit
    public ResponseDTO<HelpDocDetailVO> view(@PathVariable Long helpDocId, HttpServletRequest request) {
        return helpDocUserService.view(
                RequestUtil.getRequestUser(),
                helpDocId);
    }

    @Operation(summary = "【用户】帮助文档-查询全部")
    @GetMapping("/helpDoc/user/queryAllHelpDocList")
    @RepeatSubmit
    public ResponseDTO<List<HelpDocVO>> queryAllHelpDocList() {
        return helpDocUserService.queryAllHelpDocList();
    }


    @Operation(summary = "【用户】帮助文档-查询 查看记录")
    @PostMapping("/helpDoc/user/queryViewRecord")
    @RepeatSubmit
    public ResponseDTO<PageResult<HelpDocViewRecordVO>> queryViewRecord(@RequestBody @Valid HelpDocViewRecordQueryForm helpDocViewRecordQueryForm) {
        return ResponseDTO.ok(helpDocUserService.queryViewRecord(helpDocViewRecordQueryForm));
    }
}
