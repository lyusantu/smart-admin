package net.lab1024.sa.base.module.support.datatracer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.datatracer.domain.form.DataTracerQueryForm;
import net.lab1024.sa.base.module.support.datatracer.domain.vo.DataTracerVO;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
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
