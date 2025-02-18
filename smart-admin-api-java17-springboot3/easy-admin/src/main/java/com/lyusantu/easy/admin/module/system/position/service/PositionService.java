package com.lyusantu.easy.admin.module.system.position.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.admin.module.system.position.domain.entity.PositionEntity;
import com.lyusantu.easy.admin.module.system.position.domain.form.PositionAddForm;
import com.lyusantu.easy.admin.module.system.position.domain.form.PositionQueryForm;
import com.lyusantu.easy.admin.module.system.position.domain.form.PositionUpdateForm;
import com.lyusantu.easy.admin.module.system.position.domain.vo.PositionVO;
import com.lyusantu.easy.admin.module.system.position.mapper.PositionMapper;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 职务表 Service
 */
@RequiredArgsConstructor
@Service
public class PositionService {

    private final PositionMapper positionMapper;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<PositionVO> queryPage(PositionQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<PositionVO> list = positionMapper.queryPage(page, queryForm);
        PageResult<PositionVO> pageResult = PageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(PositionAddForm addForm) {
        PositionEntity positionEntity = BeanUtil.copy(addForm, PositionEntity.class);
        positionMapper.insert(positionEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(PositionUpdateForm updateForm) {
        PositionEntity positionEntity = BeanUtil.copy(updateForm, PositionEntity.class);
        positionMapper.updateById(positionEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        positionMapper.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long positionId) {
        if (null == positionId){
            return ResponseDTO.ok();
        }

        positionMapper.deleteById(positionId);
        return ResponseDTO.ok();
    }

    /**
     * 分页查询
     *
     * @return
     */
    public List<PositionVO> queryList() {
        List<PositionVO> list = positionMapper.queryList(Boolean.FALSE);
        return list;
    }
}
