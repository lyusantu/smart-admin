package com.lyusantu.easy.base.module.support.reload;

import com.lyusantu.easy.base.module.support.reload.core.AbstractEasyReloadCommand;
import com.lyusantu.easy.base.module.support.reload.core.domain.EasyReloadItem;
import com.lyusantu.easy.base.module.support.reload.core.domain.EasyReloadResult;
import com.lyusantu.easy.base.module.support.reload.domain.ReloadItemEntity;
import com.lyusantu.easy.base.module.support.reload.domain.ReloadResultEntity;
import com.lyusantu.easy.base.module.support.reload.mapper.ReloadItemMapper;
import com.lyusantu.easy.base.module.support.reload.mapper.ReloadResultMapper;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.util.BeanUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * reload 操作
 */
@RequiredArgsConstructor
@Component
public class ReloadCommand extends AbstractEasyReloadCommand {

    private final ReloadItemMapper reloadItemMapper;

    private final ReloadResultMapper reloadResultMapper;

    /**
     * 读取数据库中EasyReload项
     */
    @Override
    public List<EasyReloadItem> readReloadItem() {
        List<ReloadItemEntity> reloadItemEntityList = reloadItemMapper.selectList(null);
        return BeanUtil.copyList(reloadItemEntityList, EasyReloadItem.class);
    }


    /**
     * 保存reload结果
     */
    @Override
    public void handleReloadResult(EasyReloadResult easyReloadResult) {
        ReloadResultEntity reloadResultEntity = BeanUtil.copy(easyReloadResult, ReloadResultEntity.class);
        reloadResultMapper.insert(reloadResultEntity);
    }
}
