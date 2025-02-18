package com.lyusantu.easy.admin.module.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色 菜单
 */
@Data
@TableName("t_role_menu")
public class RoleMenuEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long roleMenuId;

    /**
     * 角色 id
     */
    private Long roleId;

    /**
     * 菜单 id
     */
    private Long menuId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
