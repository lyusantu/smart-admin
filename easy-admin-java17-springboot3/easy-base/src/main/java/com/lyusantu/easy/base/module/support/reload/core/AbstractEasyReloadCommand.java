package com.lyusantu.easy.base.module.support.reload.core;


import com.lyusantu.easy.base.module.support.reload.core.domain.EasyReloadItem;
import com.lyusantu.easy.base.module.support.reload.core.domain.EasyReloadObject;
import com.lyusantu.easy.base.module.support.reload.core.domain.EasyReloadResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 检测是否 Reload 的类
 */
public abstract class AbstractEasyReloadCommand {

    /**
     * 当前ReloadItem的存储器
     */
    private ConcurrentHashMap<String, String> tagIdentifierMap = new ConcurrentHashMap<>();

    private EasyReloadManager easyReloadManager;

    /**
     * @return
     */
    public void setReloadManager(EasyReloadManager easyReloadManager) {
        this.easyReloadManager = easyReloadManager;
    }

    public void init() {
        List<EasyReloadItem> easyReloadItems = this.readReloadItem();
        if (easyReloadItems != null) {
            for (EasyReloadItem easyReloadItem : easyReloadItems) {
                tagIdentifierMap.put(easyReloadItem.getTag(), easyReloadItem.getIdentification());
            }
        }
    }


    /**
     * 该方法返回一个List<ReloadItem></>:<br>
     * ReloadItem对象的tagIdentify为：该tag的 状态（状态其实就是个字符串，如果该字符串跟上次有变化则进行reload操作）<br>
     * ReloadItem对象的args为： reload操作需要的参数<br><br>
     *
     * @return List<ReloadItem>
     */
    public abstract List<EasyReloadItem> readReloadItem();

    /**
     * 处理Reload结果
     */
    public abstract void handleReloadResult(EasyReloadResult easyReloadResult);


    /**
     * 获取本地缓存tag标识
     */
    public ConcurrentHashMap<String, String> getTagIdentifierMap() {
        return tagIdentifierMap;
    }

    /**
     * 设置新的缓存标识
     */
    public void putIdentifierMap(String tag, String identification) {
        tagIdentifierMap.put(tag, identification);
    }


    /**
     * 获取重载对象
     */
    public EasyReloadObject reloadObject(String tag) {
        if (this.easyReloadManager == null) {
            return null;
        }
        Map<String, EasyReloadObject> reloadObjectMap = easyReloadManager.reloadObjectMap();
        return reloadObjectMap.get(tag);
    }
}
