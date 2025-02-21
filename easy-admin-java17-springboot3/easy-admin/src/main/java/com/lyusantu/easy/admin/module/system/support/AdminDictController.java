package com.lyusantu.easy.admin.module.system.support;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lyusantu.easy.base.module.support.dict.domain.form.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.dict.domain.vo.DictKeyVO;
import com.lyusantu.easy.base.module.support.dict.service.DictCacheService;
import com.lyusantu.easy.base.module.support.dict.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典
 */
@RequiredArgsConstructor
@Tag(name = SwaggerTagConst.Support.DICT)
@RestController
public class AdminDictController extends SupportBaseController {

    private final DictService dictService;

    private final DictCacheService dictCacheService;

    @Operation(summary = "分页查询数据字典KEY")
    @PostMapping("/dict/key/query")
    public ResponseDTO<PageResult<DictKeyVO>> keyQuery(@Valid @RequestBody DictKeyQueryForm queryForm) {
        return dictService.keyQuery(queryForm);
    }


    @Operation(summary = "数据字典KEY-添加")
    @PostMapping("/dict/key/add")
    @SaCheckPermission("support:dict:add")
    public ResponseDTO<String> keyAdd(@Valid @RequestBody DictKeyAddForm keyAddForm) {
        return dictService.keyAdd(keyAddForm);
    }

    @Operation(summary = "数据字典缓存-刷新")
    @GetMapping("/dict/cache/refresh")
    @SaCheckPermission("support:dict:refresh")
    public ResponseDTO<String> cacheRefresh() {
        return dictCacheService.cacheRefresh();
    }

    @Operation(summary = "数据字典Value-添加")
    @PostMapping("/dict/value/add")
    public ResponseDTO<String> valueAdd(@Valid @RequestBody DictValueAddForm valueAddForm) {
        return dictService.valueAdd(valueAddForm);
    }

    @Operation(summary = "数据字典KEY-更新")
    @PostMapping("/dict/key/edit")
    @SaCheckPermission("support:dict:edit")
    public ResponseDTO<String> keyEdit(@Valid @RequestBody DictKeyUpdateForm keyUpdateForm) {
        return dictService.keyEdit(keyUpdateForm);
    }

    @Operation(summary = "数据字典Value-更新")
    @PostMapping("/dict/value/edit")
    public ResponseDTO<String> valueEdit(@Valid @RequestBody DictValueUpdateForm valueUpdateForm) {
        return dictService.valueEdit(valueUpdateForm);
    }

    @Operation(summary = "数据字典KEY-删除")
    @PostMapping("/dict/key/delete")
    @SaCheckPermission("support:dict:delete")
    public ResponseDTO<String> keyDelete(@RequestBody List<Long> keyIdList) {
        return dictService.keyDelete(keyIdList);
    }

    @Operation(summary = "数据字典Value-删除")
    @PostMapping("/dict/value/delete")
    public ResponseDTO<String> valueDelete(@RequestBody List<Long> valueIdList) {
        return dictService.valueDelete(valueIdList);
    }

}
