package com.lyusantu.easy.base.module.support.datatracer.controller;

import com.lyusantu.easy.base.module.support.datatracer.domain.form.DataTracerQueryForm;
import com.lyusantu.easy.base.module.support.datatracer.domain.vo.DataTracerVO;
import com.lyusantu.easy.base.module.support.datatracer.service.DataTracerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据变动记录
 */
@Tag(name = SwaggerTagConst.Support.DATA_TRACER)
@RequiredArgsConstructor
@RestController
public class DataTracerController extends SupportBaseController {

    private final DataTracerService dataTracerService;

    @Operation(summary = "分页查询业务操作日志")
    @PostMapping("/dataTracer/query")
    public ResponseDTO<PageResult<DataTracerVO>> query(@Valid @RequestBody DataTracerQueryForm queryForm) {
        return dataTracerService.query(queryForm);
    }
}
