package com.lyusantu.easy.admin.module.system.support;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.reload.ReloadService;
import com.lyusantu.easy.base.module.support.reload.domain.ReloadForm;
import com.lyusantu.easy.base.module.support.reload.domain.ReloadItemVO;
import com.lyusantu.easy.base.module.support.reload.domain.ReloadResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * reload (内存热加载、钩子等)
 */
@RequiredArgsConstructor
@RestController
@Tag(name = SwaggerTagConst.Support.RELOAD)
public class AdminReloadController extends SupportBaseController {

    private final ReloadService reloadService;

    @Operation(summary = "查询reload列表")
    @GetMapping("/reload/query")
    public ResponseDTO<List<ReloadItemVO>> query() {
        return reloadService.query();
    }

    @Operation(summary = "获取reload result")
    @GetMapping("/reload/result/{tag}")
    @SaCheckPermission("support:reload:result")
    public ResponseDTO<List<ReloadResultVO>> queryReloadResult(@PathVariable("tag") String tag) {
        return reloadService.queryReloadItemResult(tag);
    }

    @Operation(summary = "通过tag更新标识")
    @PostMapping("/reload/update")
    @SaCheckPermission("support:reload:update")
    public ResponseDTO<String> updateByTag(@RequestBody @Valid ReloadForm reloadForm) {
        return reloadService.updateByTag(reloadForm);
    }
}
