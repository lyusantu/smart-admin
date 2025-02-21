package com.lyusantu.easy.base.module.support.reload;

import com.lyusantu.easy.base.module.support.reload.domain.ReloadForm;
import com.lyusantu.easy.base.module.support.reload.domain.ReloadItemEntity;
import com.lyusantu.easy.base.module.support.reload.domain.ReloadItemVO;
import com.lyusantu.easy.base.module.support.reload.domain.ReloadResultVO;
import com.lyusantu.easy.base.module.support.reload.mapper.ReloadItemMapper;
import com.lyusantu.easy.base.module.support.reload.mapper.ReloadResultMapper;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * reload (内存热加载、钩子等)
 */
@RequiredArgsConstructor
@Service
public class ReloadService {

    private final ReloadItemMapper reloadItemMapper;

    private final ReloadResultMapper reloadResultMapper;

    /**
     * 查询
     *
     * @return
     */
    public ResponseDTO<List<ReloadItemVO>> query() {
        List<ReloadItemVO> list = reloadItemMapper.query();
        return ResponseDTO.ok(list);
    }

    public ResponseDTO<List<ReloadResultVO>> queryReloadItemResult(String tag) {
        List<ReloadResultVO> reloadResultList = reloadResultMapper.query(tag);
        return ResponseDTO.ok(reloadResultList);
    }


    /**
     * 通过标签更新标识符
     *
     * @param reloadForm
     * @return
     */
    public ResponseDTO<String> updateByTag(ReloadForm reloadForm) {
        ReloadItemEntity reloadItemEntity = reloadItemMapper.selectById(reloadForm.getTag());
        if (null == reloadItemEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        reloadItemEntity.setIdentification(reloadForm.getIdentification());
        reloadItemEntity.setUpdateTime(LocalDateTime.now());
        reloadItemEntity.setArgs(reloadForm.getArgs());
        reloadItemMapper.updateById(reloadItemEntity);
        return ResponseDTO.ok();
    }
}
