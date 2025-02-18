package net.lab1024.sa.base.module.support.reload;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import net.lab1024.sa.base.common.util.BeanUtil;
import net.lab1024.sa.base.module.support.reload.core.AbstractSmartReloadCommand;
import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadItem;
import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadResult;
import net.lab1024.sa.base.module.support.reload.mapper.ReloadItemMapper;
import net.lab1024.sa.base.module.support.reload.mapper.ReloadResultMapper;
import net.lab1024.sa.base.module.support.reload.domain.ReloadItemEntity;
import net.lab1024.sa.base.module.support.reload.domain.ReloadResultEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * reload 操作
 */
@RequiredArgsConstructor
@Component
public class ReloadCommand extends AbstractSmartReloadCommand {

    private final ReloadItemMapper reloadItemMapper;

    private final ReloadResultMapper reloadResultMapper;

    /**
     * 读取数据库中SmartReload项
     *
     * @return List<ReloadItem>
     */
    @Override
    public List<SmartReloadItem> readReloadItem() {
        List<ReloadItemEntity> reloadItemEntityList = reloadItemMapper.selectList(null);
        return BeanUtil.copyList(reloadItemEntityList, SmartReloadItem.class);
    }


    /**
     * 保存reload结果
     *
     * @param smartReloadResult
     */
    @Override
    public void handleReloadResult(SmartReloadResult smartReloadResult) {
        ReloadResultEntity reloadResultEntity = BeanUtil.copy(smartReloadResult, ReloadResultEntity.class);
        reloadResultMapper.insert(reloadResultEntity);
    }
}
