package com.lyusantu.easy.base.module.support.loginlog.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.domain.page.PageParam;

/**
 * 登录查询日志
 */
@Data
public class LoginLogQueryForm extends PageParam {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "ip")
    private String ip;

}
