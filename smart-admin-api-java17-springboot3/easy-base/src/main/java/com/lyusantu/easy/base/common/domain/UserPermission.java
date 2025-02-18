package com.lyusantu.easy.base.common.domain;

import lombok.Data;

import java.util.List;

/**
 * sa-token 所需的权限信息
 */

@Data
public class UserPermission {

    /**
     * 角色列表
     */
    private List<String> roleList;

    /**
     * 权限列表
     */
    private List<String> permissionList;


}
