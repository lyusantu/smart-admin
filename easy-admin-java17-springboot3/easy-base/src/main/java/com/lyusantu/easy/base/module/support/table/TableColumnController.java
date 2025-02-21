package com.lyusantu.easy.base.module.support.table;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.repeatsubmit.annoation.RepeatSubmit;
import com.lyusantu.easy.base.module.support.table.domain.TableColumnUpdateForm;
import org.springframework.web.bind.annotation.*;

/**
 * 表格自定义列（前端用户自定义表格列，并保存到数据库里）
 */
@RequiredArgsConstructor
@RestController
@Tag(name = SwaggerTagConst.Support.TABLE_COLUMN)
public class TableColumnController extends SupportBaseController {

    private final TableColumnService tableColumnService;

    @Operation(summary = "修改表格列")
    @PostMapping("/tableColumn/update")
    @RepeatSubmit
    public ResponseDTO<String> updateTableColumn(@RequestBody @Valid TableColumnUpdateForm updateForm) {
        return tableColumnService.updateTableColumns(RequestUtil.getRequestUser(), updateForm);
    }

    @Operation(summary = "恢复默认（删除）")
    @GetMapping("/tableColumn/delete/{tableId}")
    @RepeatSubmit
    public ResponseDTO<String> deleteTableColumn(@PathVariable Integer tableId) {
        return tableColumnService.deleteTableColumn(RequestUtil.getRequestUser(), tableId);
    }

    @Operation(summary = "查询表格列")
    @GetMapping("/tableColumn/getColumns/{tableId}")
    public ResponseDTO<String> getColumns(@PathVariable Integer tableId) {
        return ResponseDTO.ok(tableColumnService.getTableColumns(RequestUtil.getRequestUser(), tableId));
    }
}
