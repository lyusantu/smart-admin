package com.lyusantu.easy.base.module.support.config;

import com.lyusantu.easy.base.module.support.config.domain.ConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置
 */
@Tag(name = SwaggerTagConst.Support.CONFIG)
@RequiredArgsConstructor
@RestController
public class ConfigController extends SupportBaseController {

    private final ConfigService configService;

    @Operation(summary = "查询配置详情")
    @GetMapping("/config/queryByKey")
    public ResponseDTO<ConfigVO> queryByKey(@RequestParam String configKey) {
        return ResponseDTO.ok(configService.getConfig(configKey));
    }

}
