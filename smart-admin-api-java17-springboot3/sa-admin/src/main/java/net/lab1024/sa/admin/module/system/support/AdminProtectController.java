package net.lab1024.sa.admin.module.system.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.validate.ValidateList;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.config.ConfigKeyEnum;
import net.lab1024.sa.base.module.support.config.ConfigService;
import net.lab1024.sa.base.module.support.securityprotect.domain.Level3ProtectConfigForm;
import net.lab1024.sa.base.module.support.securityprotect.domain.LoginFailQueryForm;
import net.lab1024.sa.base.module.support.securityprotect.domain.LoginFailVO;
import net.lab1024.sa.base.module.support.securityprotect.service.Level3ProtectConfigService;
import net.lab1024.sa.base.module.support.securityprotect.service.SecurityLoginService;
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
