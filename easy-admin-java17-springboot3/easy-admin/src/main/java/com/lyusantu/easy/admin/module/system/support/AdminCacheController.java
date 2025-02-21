package com.lyusantu.easy.admin.module.system.support;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.cache.CacheService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 缓存
 */
@RequiredArgsConstructor
@RestController
@Tag(name = SwaggerTagConst.Support.CACHE)
public class AdminCacheController extends SupportBaseController {

    private final CacheService cacheService;

    @Operation(summary = "获取所有缓存")
    @GetMapping("/cache/names")
    @SaCheckPermission("support:cache:keys")
    public ResponseDTO<List<String>> cacheNames() {
        return ResponseDTO.ok(cacheService.cacheNames());
    }

    @Operation(summary = "移除某个缓存")
    @GetMapping("/cache/remove/{cacheName}")
    @SaCheckPermission("support:cache:delete")
    public ResponseDTO<String> removeCache(@PathVariable String cacheName) {
        cacheService.removeCache(cacheName);
        return ResponseDTO.ok();
    }

    @Operation(summary = "获取某个缓存的所有key")
    @GetMapping("/cache/keys/{cacheName}")
    @SaCheckPermission("support:cache:keys")
    public ResponseDTO<List<String>> cacheKeys(@PathVariable String cacheName) {
        return ResponseDTO.ok(cacheService.cacheKey(cacheName));
    }

}
