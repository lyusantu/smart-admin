package com.lyusantu.easy.base.module.support.reload.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * Reload 处理程序的实现方法，用于包装以注解 EasyReload 实现的处理类
 */
@Data
@AllArgsConstructor
public class EasyReloadObject {

    /**
     * 方法对应的实例化对象
     */
    private Object reloadObject;

    /**
     * 重新加载执行的方法
     */
    private Method method;


}
