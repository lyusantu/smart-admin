package com.lyusantu.easy.base.module.support.dict.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.dict.domain.form.DictValueQueryForm;
import com.lyusantu.easy.base.module.support.dict.domain.vo.DictKeyVO;
import com.lyusantu.easy.base.module.support.dict.domain.vo.DictValueVO;
import com.lyusantu.easy.base.module.support.dict.service.DictCacheService;
import com.lyusantu.easy.base.module.support.dict.service.DictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典
 */
@Tag(name = SwaggerTagConst.Support.DICT)
@RequiredArgsConstructor
@RestController
public class DictController extends SupportBaseController {

    private final DictService dictService;

    private final DictCacheService dictCacheService;


    @Operation(summary = "查询全部字典key")
    @GetMapping("/dict/key/queryAll")
    public ResponseDTO<List<DictKeyVO>> queryAll() {
        return ResponseDTO.ok(dictService.queryAllKey());
    }

    @Operation(summary = "分页查询数据字典value")
    @PostMapping("/dict/value/query")
    public ResponseDTO<PageResult<DictValueVO>> valueQuery(@Valid @RequestBody DictValueQueryForm queryForm) {
        return dictService.valueQuery(queryForm);
    }

    @Operation(summary = "数据字典-值列表")
    @GetMapping("/dict/value/list/{keyCode}")
    public ResponseDTO<List<DictValueVO>> valueList(@PathVariable String keyCode) {
        List<DictValueVO> dictValueVOList = dictCacheService.selectByKeyCode(keyCode);
        return ResponseDTO.ok(dictValueVOList);
    }
}
