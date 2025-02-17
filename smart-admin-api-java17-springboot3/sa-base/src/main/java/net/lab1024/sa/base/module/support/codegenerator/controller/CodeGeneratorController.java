package net.lab1024.sa.base.module.support.codegenerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.ResponseUtil;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorPreviewForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.TableQueryForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableColumnVO;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableConfigVO;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableVO;
import net.lab1024.sa.base.module.support.codegenerator.service.CodeGeneratorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 代码生成
 */
@RequiredArgsConstructor
@Tag(name = SwaggerTagConst.Support.CODE_GENERATOR)
@Controller
public class CodeGeneratorController extends SupportBaseController {

    private final CodeGeneratorService codeGeneratorService;

    /*              代码查询                */

    @Operation(summary = "获取表列")
    @GetMapping("/codeGenerator/table/getTableColumns/{table}")
    @ResponseBody
    public ResponseDTO<List<TableColumnVO>> getTableColumns(@PathVariable String table) {
        return ResponseDTO.ok(codeGeneratorService.getTableColumns(table));
    }

    @Operation(summary = "查询数据库的表")
    @PostMapping("/codeGenerator/table/queryTableList")
    @ResponseBody
    public ResponseDTO<PageResult<TableVO>> queryTableList(@RequestBody @Valid TableQueryForm tableQueryForm) {
        return ResponseDTO.ok(codeGeneratorService.queryTableList(tableQueryForm));
    }

    /*               代码配置               */

    @Operation(summary = "获取表配置")
    @GetMapping("/codeGenerator/table/getConfig/{table}")
    @ResponseBody
    public ResponseDTO<TableConfigVO> getTableConfig(@PathVariable String table) {
        return ResponseDTO.ok(codeGeneratorService.getTableConfig(table));
    }

    @Operation(summary = "更新表配置")
    @PostMapping("/codeGenerator/table/updateConfig")
    @ResponseBody
    public ResponseDTO<String> updateConfig(@RequestBody @Valid CodeGeneratorConfigForm form) {
        return codeGeneratorService.updateConfig(form);
    }

    /*              代码生成                */

    @Operation(summary = "代码预览")
    @PostMapping("/codeGenerator/code/preview")
    @ResponseBody
    public ResponseDTO<String> preview(@RequestBody @Valid CodeGeneratorPreviewForm form) {
        return codeGeneratorService.preview(form);
    }

    @Operation(summary = "代码下载 @author 卓大")
    @GetMapping(value = "/codeGenerator/code/download/{tableName}", produces = "application/octet-stream")
    public void download(@PathVariable String tableName, HttpServletResponse response) throws IOException {

        ResponseDTO<byte[]> download = codeGeneratorService.download(tableName);

        if (download.getOk()) {
            ResponseUtil.setDownloadFileHeader(response, tableName + "_code.zip", (long) download.getData().length);
            response.getOutputStream().write(download.getData());
        } else {
            ResponseUtil.write(response, download);
        }
    }

}
