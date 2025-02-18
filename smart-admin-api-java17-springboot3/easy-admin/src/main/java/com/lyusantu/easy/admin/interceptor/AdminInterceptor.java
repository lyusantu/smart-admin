package com.lyusantu.easy.admin.interceptor;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.admin.module.system.login.domain.RequestEmployee;
import com.lyusantu.easy.admin.module.system.login.service.LoginService;
import com.lyusantu.easy.base.common.annoation.NoNeedLogin;
import com.lyusantu.easy.base.common.code.SystemErrorCode;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.constant.StringConst;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.domain.SystemEnvironment;
import com.lyusantu.easy.base.common.enumeration.SystemEnvironmentEnum;
import com.lyusantu.easy.base.common.enumeration.UserTypeEnum;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.common.util.ResponseUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * Admin拦截器
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    private final SystemEnvironment systemEnvironment;

    /**
     * 请求前拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Options请求直接放行
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }

        // 如果请求是访问静态资源而非Controller直接跳过拦截
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        try {
            // 获取 Token 并解析用户
            String tokenValue = StpUtil.getTokenValue();
            boolean debugNumberTokenFlag = isDevDebugNumberToken(tokenValue);

            String loginId = null;
            if (debugNumberTokenFlag) {
                // 开发、测试环境，且为数字的话，则表明为调试临时用户，即需要调用 sa-token switch
                loginId = UserTypeEnum.ADMIN_EMPLOYEE.getValue() + StringConst.COLON + tokenValue;
                StpUtil.switchTo(loginId);
            } else {
                loginId = (String) StpUtil.getLoginIdByToken(tokenValue);
            }

            RequestEmployee requestEmployee = loginService.getLoginEmployee(loginId, request);

            // 放行所有带@NoNeedLogin的接口
            Method method = ((HandlerMethod) handler).getMethod();
            if (((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class) != null) {
                checkActiveTimeout(requestEmployee, debugNumberTokenFlag);
                return true;
            }

            // 校验用户是否登录
            if (requestEmployee == null) {
                ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID));
                return false;
            }

            // 检测token活跃度
            checkActiveTimeout(requestEmployee, debugNumberTokenFlag);

            // 放行所有带@SaIgnore的接口
            RequestUtil.setRequestUser(requestEmployee);
            if (SaStrategy.instance.isAnnotationPresent.apply(method, SaIgnore.class)) {
                return true;
            }

            // 放行超级管理员
            if (requestEmployee.getAdministratorFlag()) {
                return true;
            }

            // 权限效验
            SaStrategy.instance.checkMethodAnnotation.accept(method);
        } catch (SaTokenException e) {
            // Sa-Token异常状态码 https://sa-token.cc/doc.html#/fun/exception-code
            switch (e.getCode()) {
                case 11041, 11051 -> ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.NO_PERMISSION));
                case 11016 -> ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_ACTIVE_TIMEOUT));
                case 11011 - 11015 ->
                        ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID));
                default -> ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.PARAM_ERROR));
            }
            log.warn("SaToken效验失败，Code: {}, Message: {}", e.getCode(), e.getMessage());
            return false;
        } catch (Throwable e) {
            ResponseUtil.write(response, ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR));
            log.error("AdminInterceptor 发生异常: {}", e.getMessage(), e);
            return false;
        }
        // 通过验证
        return true;
    }


    /**
     * 检测：token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结
     */
    private void checkActiveTimeout(RequestEmployee requestEmployee, boolean debugNumberTokenFlag) {
        // 不检测开发&测试环境的数字DebugToken
        if (debugNumberTokenFlag) {
            return;
        }
        // 不检测不在线的用户
        if (requestEmployee == null) {
            return;
        }
        StpUtil.checkActiveTimeout();
        StpUtil.updateLastActiveToNow();
    }


    /**
     * 是否为开发使用的 debug token
     */
    private boolean isDevDebugNumberToken(String token) {
        if (!StrUtil.isNumeric(token)) {
            return false;
        }
        return systemEnvironment.getCurrentEnvironment() == SystemEnvironmentEnum.DEV || systemEnvironment.getCurrentEnvironment() == SystemEnvironmentEnum.TEST;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除上下文
        RequestUtil.remove();
        // 开发环境，关闭 sa token 的临时切换用户
        if (systemEnvironment.getCurrentEnvironment() == SystemEnvironmentEnum.DEV) {
            StpUtil.endSwitch();
        }
    }


}
