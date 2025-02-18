package com.lyusantu.easy.admin.module.system.login.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.lyusantu.easy.admin.module.system.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.lyusantu.easy.admin.constant.AdminSwaggerTagConst;
import com.lyusantu.easy.admin.module.system.login.domain.LoginForm;
import com.lyusantu.easy.admin.module.system.login.domain.LoginResultVO;
import com.lyusantu.easy.admin.util.AdminRequestUtil;
import com.lyusantu.easy.base.common.annoation.NoNeedLogin;
import com.lyusantu.easy.base.common.constant.RequestHeaderConst;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.module.support.captcha.domain.CaptchaVO;
import com.lyusantu.easy.base.module.support.securityprotect.service.Level3ProtectConfigService;
import org.springframework.web.bind.annotation.*;

/**
 * 员工登录
 */
@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_LOGIN)
public class LoginController {

    @Resource
    private LoginService loginService;

    @Resource
    private Level3ProtectConfigService level3ProtectConfigService;

    @NoNeedLogin
    @PostMapping("/login")
    @Operation(summary = "登录")
    public ResponseDTO<LoginResultVO> login(@Valid @RequestBody LoginForm loginForm, HttpServletRequest request) {
        String ip = JakartaServletUtil.getClientIP(request);
        String userAgent = JakartaServletUtil.getHeaderIgnoreCase(request, RequestHeaderConst.USER_AGENT);
        return loginService.login(loginForm, ip, userAgent);
    }

    @GetMapping("/login/getLoginInfo")
    @Operation(summary = "获取登录结果信息")
    public ResponseDTO<LoginResultVO> getLoginInfo() {
        String tokenValue = StpUtil.getTokenValue();
        LoginResultVO loginResult = loginService.getLoginResult(AdminRequestUtil.getRequestUser(), tokenValue);
        loginResult.setToken(tokenValue);
        return ResponseDTO.ok(loginResult);
    }

    @Operation(summary = "退出登陆  @author 卓大")
    @GetMapping("/login/logout")
    public ResponseDTO<String> logout(@RequestHeader(value = RequestHeaderConst.TOKEN, required = false) String token) {
        return loginService.logout(token, RequestUtil.getRequestUser());
    }

    @Operation(summary = "获取验证码")
    @GetMapping("/login/getCaptcha")
    @NoNeedLogin
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return loginService.getCaptcha();
    }

    @NoNeedLogin
    @GetMapping("/login/sendEmailCode/{loginName}")
    @Operation(summary = "获取邮箱登录验证码")
    public ResponseDTO<String> sendEmailCode(@PathVariable String loginName) {
        return loginService.sendEmailCode(loginName);
    }


    @NoNeedLogin
    @GetMapping("/login/getTwoFactorLoginFlag")
    @Operation(summary = "获取双因子登录标识")
    public ResponseDTO<Boolean> getTwoFactorLoginFlag() {
        // 双因子登录
        boolean twoFactorLoginEnabled = level3ProtectConfigService.isTwoFactorLoginEnabled();
        return ResponseDTO.ok(twoFactorLoginEnabled);
    }
}
