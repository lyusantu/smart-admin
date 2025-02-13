package net.lab1024.sa.admin.module.system.menu.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 权限类型
 */
@AllArgsConstructor
@Getter
public enum MenuPermsTypeEnum implements BaseEnum {

    SA_TOKEN(1, "Sa-Token模式");

    private final Integer value;

    private final String desc;

}
