package com.lyusantu.easy.admin.module.system.role.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.domain.page.PageParam;

/**
 * 角色的员工查询
 */
@Data
public class RoleEmployeeQueryForm extends PageParam {

    @Schema(description = "关键字")
    private String keywords;

    @Schema(description = "角色id")
    private String roleId;
}
