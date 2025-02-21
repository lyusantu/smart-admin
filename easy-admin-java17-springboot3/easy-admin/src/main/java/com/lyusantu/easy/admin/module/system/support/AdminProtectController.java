package com.lyusantu.easy.admin.module.system.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.domain.validate.ValidateList;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.config.ConfigKeyEnum;
import com.lyusantu.easy.base.module.support.config.ConfigService;
import com.lyusantu.easy.base.module.support.securityprotect.domain.Level3ProtectConfigForm;
import com.lyusantu.easy.base.module.support.securityprotect.domain.LoginFailQueryForm;
import com.lyusantu.easy.base.module.support.securityprotect.domain.LoginFailVO;
import com.lyusantu.easy.base.module.support.securityprotect.service.Level3ProtectConfigService;
import com.lyusantu.easy.base.module.support.securityprotect.service.SecurityLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网络安全
 */
@RequiredArgsConstructor
@RestController
@Tag(name = SwaggerTagConst.Support.PROTECT)
public class AdminProtectController extends SupportBaseController {

    private final ConfigService configService;

    private final SecurityLoginService securityLoginService;

    private final Level3ProtectConfigService level3ProtectConfigService;

    @Operation(summary = "分页查询")
    @PostMapping("/protect/loginFail/queryPage")
    public ResponseDTO<PageResult<LoginFailVO>> queryPage(@RequestBody @Valid LoginFailQueryForm queryForm) {
        return ResponseDTO.ok(securityLoginService.queryPage(queryForm));
    }


    @Operation(summary = "批量删除")
    @PostMapping("/protect/loginFail/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return securityLoginService.batchDelete(idList);
    }

    @Operation(summary = "更新三级等保配置")
    @PostMapping("/protect/level3protect/updateConfig")
    public ResponseDTO<String> updateConfig(@RequestBody @Valid Level3ProtectConfigForm configForm) {
        return level3ProtectConfigService.updateLevel3Config(configForm);
    }

    @Operation(summary = "查询 三级等保配置")
    @GetMapping("/protect/level3protect/getConfig")
    public ResponseDTO<String> getConfig() {
        return ResponseDTO.ok(configService.getConfigValue(ConfigKeyEnum.LEVEL3_PROTECT_CONFIG));
    }
}
