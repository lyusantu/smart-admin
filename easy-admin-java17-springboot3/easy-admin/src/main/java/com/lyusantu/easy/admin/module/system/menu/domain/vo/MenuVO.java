package com.lyusantu.easy.admin.module.system.menu.domain.vo;

import com.lyusantu.easy.admin.module.system.menu.domain.form.MenuBaseForm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单
 */
@Data
public class MenuVO extends MenuBaseForm {

    @Schema(description = "菜单ID")
    private Long menuId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createUserId;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateUserId;
}
