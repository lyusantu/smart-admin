package net.lab1024.sa.base.module.support.changelog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.BeanUtil;
import net.lab1024.sa.base.common.util.PageUtil;
import net.lab1024.sa.base.module.support.changelog.mapper.ChangeLogMapper;
import net.lab1024.sa.base.module.support.changelog.domain.entity.ChangeLogEntity;
import net.lab1024.sa.base.module.support.changelog.domain.form.ChangeLogAddForm;
import net.lab1024.sa.base.module.support.changelog.domain.form.ChangeLogQueryForm;
import net.lab1024.sa.base.module.support.changelog.domain.form.ChangeLogUpdateForm;
import net.lab1024.sa.base.module.support.changelog.domain.vo.ChangeLogVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统更新日志 Service
 */
@RequiredArgsConstructor
@Service
public class ChangeLogService {

    private final ChangeLogMapper changeLogMapper;

    /**
     * 分页查询
     */
    public PageResult<ChangeLogVO> queryPage(ChangeLogQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<ChangeLogVO> list = changeLogMapper.queryPage(page, queryForm);
        return PageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public synchronized ResponseDTO<String> add(ChangeLogAddForm addForm) {
        ChangeLogEntity existVersion = changeLogMapper.selectByVersion(addForm.getVersion());
        if (existVersion != null) {
            return ResponseDTO.userErrorParam("此版本已经存在");
        }

        ChangeLogEntity changeLogEntity = BeanUtil.copy(addForm, ChangeLogEntity.class);
        changeLogMapper.insert(changeLogEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     */
    public synchronized ResponseDTO<String> update(ChangeLogUpdateForm updateForm) {
        ChangeLogEntity existVersion = changeLogMapper.selectByVersion(updateForm.getVersion());
        if (existVersion != null && !updateForm.getChangeLogId().equals(existVersion.getChangeLogId())) {
            return ResponseDTO.userErrorParam("此版本已经存在");
        }
        ChangeLogEntity changeLogEntity = BeanUtil.copy(updateForm, ChangeLogEntity.class);
        changeLogMapper.updateById(changeLogEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public synchronized ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }

        changeLogMapper.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public synchronized ResponseDTO<String> delete(Long changeLogId) {
        if (null == changeLogId) {
            return ResponseDTO.ok();
        }

        changeLogMapper.deleteById(changeLogId);
        return ResponseDTO.ok();
    }

    public ChangeLogVO getById(Long changeLogId) {
        return BeanUtil.copy(changeLogMapper.selectById(changeLogId), ChangeLogVO.class);
    }
}
