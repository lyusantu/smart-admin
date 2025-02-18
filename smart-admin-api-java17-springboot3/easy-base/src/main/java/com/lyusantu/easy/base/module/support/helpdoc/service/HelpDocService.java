package com.lyusantu.easy.base.module.support.helpdoc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.module.support.helpdoc.mapper.HelpDocMapper;
import com.lyusantu.easy.base.module.support.helpdoc.domain.entity.HelpDocEntity;
import com.lyusantu.easy.base.module.support.helpdoc.domain.form.HelpDocAddForm;
import com.lyusantu.easy.base.module.support.helpdoc.domain.form.HelpDocQueryForm;
import com.lyusantu.easy.base.module.support.helpdoc.domain.form.HelpDocUpdateForm;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocDetailVO;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocVO;
import com.lyusantu.easy.base.module.support.helpdoc.manager.HelpDocManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台管理业务
 */
@RequiredArgsConstructor
@Service
public class HelpDocService {

    private final HelpDocMapper helpDocMapper;

    private final HelpDocManager helpDaoManager;


    /**
     * 查询 帮助文档
     *
     * @param queryForm
     * @return
     */
    public PageResult<HelpDocVO> query(HelpDocQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<HelpDocVO> list = helpDocMapper.query(page, queryForm);
        return PageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     *
     * @param addForm
     * @return
     */
    public ResponseDTO<String> add(HelpDocAddForm addForm) {
        HelpDocEntity helpDaoEntity = BeanUtil.copy(addForm, HelpDocEntity.class);
        helpDaoManager.save(helpDaoEntity, addForm.getRelationList());
        return ResponseDTO.ok();
    }


    /**
     * 更新
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(HelpDocUpdateForm updateForm) {
        // 更新
        HelpDocEntity helpDaoEntity = BeanUtil.copy(updateForm, HelpDocEntity.class);
        helpDaoManager.update(helpDaoEntity, updateForm.getRelationList());
        return ResponseDTO.ok();
    }


    /**
     * 删除
     *
     * @param helpDocId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long helpDocId) {
        HelpDocEntity helpDaoEntity = helpDocMapper.selectById(helpDocId);
        if (helpDaoEntity != null) {
            helpDocMapper.deleteById(helpDocId);
            helpDocMapper.deleteRelation(helpDocId);
        }
        return ResponseDTO.ok();
    }

    /**
     * 获取详情
     *
     * @param helpDocId
     * @return
     */
    public HelpDocDetailVO getDetail(Long helpDocId) {
        HelpDocEntity helpDaoEntity = helpDocMapper.selectById(helpDocId);
        HelpDocDetailVO detail = BeanUtil.copy(helpDaoEntity, HelpDocDetailVO.class);
        if (detail != null) {
            detail.setRelationList(helpDocMapper.queryRelationByHelpDoc(helpDocId));
        }
        return detail;
    }

    /**
     * 获取详情
     *
     * @param relationId
     * @return
     */
    public List<HelpDocVO> queryHelpDocByRelationId(Long relationId) {
        return helpDocMapper.queryHelpDocByRelationId(relationId);
    }
}
