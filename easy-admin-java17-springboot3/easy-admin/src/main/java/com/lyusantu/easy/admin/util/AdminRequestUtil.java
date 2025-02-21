package com.lyusantu.easy.admin.util;

import com.lyusantu.easy.admin.module.system.login.domain.RequestEmployee;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.util.RequestUtil;

/**
 * 请求工具类
 */
public final class AdminRequestUtil {


    public static RequestEmployee getRequestUser() {
        return (RequestEmployee) RequestUtil.getRequestUser();
    }

    public static Long getRequestUserId() {
        RequestUser requestUser = getRequestUser();
        return null == requestUser ? null : requestUser.getUserId();
    }

}
