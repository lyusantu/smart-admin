package com.lyusantu.easy.base.module.support.reload.core.thread;

import com.lyusantu.easy.base.module.support.reload.core.domain.EasyReloadItem;
import com.lyusantu.easy.base.module.support.reload.core.domain.EasyReloadObject;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.module.support.reload.core.AbstractEasyReloadCommand;
import com.lyusantu.easy.base.module.support.reload.core.domain.EasyReloadResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * reload 线程
 */
@Slf4j
public class EasyReloadRunnable implements Runnable {

    private AbstractEasyReloadCommand abstractEasyReloadCommand;

    private boolean isInit = false;

    public EasyReloadRunnable(AbstractEasyReloadCommand abstractEasyReloadCommand) {
        this.abstractEasyReloadCommand = abstractEasyReloadCommand;
    }

    @Override
    public void run() {
        try {
            this.doTask();
        } catch (Throwable e) {
            log.error("", e);
        }
    }

    /**
     * 检测Identifier变化，执行reload
     */
    private void doTask() {
        if (!isInit) {
            this.abstractEasyReloadCommand.init();
            isInit = true;
            return;
        }

        List<EasyReloadItem> easyReloadItemList = this.abstractEasyReloadCommand.readReloadItem();
        ConcurrentHashMap<String, String> tagIdentifierMap = this.abstractEasyReloadCommand.getTagIdentifierMap();
        for (EasyReloadItem easyReloadItem : easyReloadItemList) {
            String tag = easyReloadItem.getTag();
            String tagIdentifier = easyReloadItem.getIdentification();
            String preTagChangeIdentifier = tagIdentifierMap.get(tag);
            // 数据不一致
            if (preTagChangeIdentifier == null || !preTagChangeIdentifier.equals(tagIdentifier)) {
                this.abstractEasyReloadCommand.putIdentifierMap(tag, tagIdentifier);
                // 执行重新加载此项的动作
                EasyReloadResult easyReloadResult = this.doReload(easyReloadItem);
                this.abstractEasyReloadCommand.handleReloadResult(easyReloadResult);
            }
        }
    }

    /**
     * 方法调用
     */
    private EasyReloadResult doReload(EasyReloadItem easyReloadItem) {
        EasyReloadResult result = new EasyReloadResult();
        EasyReloadObject easyReloadObject = this.abstractEasyReloadCommand.reloadObject(easyReloadItem.getTag());
        try {
            if (easyReloadObject == null) {
                result.setResult(false);
                result.setException("不能从系统中找到对应的tag：" + easyReloadItem.getTag());
                return result;
            }

            Method method = easyReloadObject.getMethod();
            if (method == null) {
                result.setResult(false);
                result.setException("reload方法不存在");
                return result;
            }

            result.setTag(easyReloadItem.getTag());
            result.setArgs(easyReloadItem.getArgs());
            result.setIdentification(easyReloadItem.getIdentification());
            result.setResult(true);
            int paramCount = method.getParameterCount();
            if (paramCount > 1) {
                result.setResult(false);
                result.setException("reload方法" + method.getName() + "参数太多");
                return result;
            }

            if (paramCount == 0) {
                method.invoke(easyReloadObject.getReloadObject());
            } else {
                method.invoke(easyReloadObject.getReloadObject(), easyReloadItem.getArgs());
            }
        } catch (Throwable throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);

            result.setResult(false);
            result.setException(throwable.toString());
        }
        return result;
    }

}
