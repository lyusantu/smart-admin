package com.lyusantu.easy.base.module.support.dict.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.dict.domain.entity.DictKeyEntity;
import com.lyusantu.easy.base.module.support.dict.domain.entity.DictValueEntity;
import com.lyusantu.easy.base.module.support.dict.domain.form.*;
import com.lyusantu.easy.base.module.support.dict.mapper.DictValueMapper;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.module.support.dict.mapper.DictKeyMapper;
import com.lyusantu.easy.base.module.support.dict.domain.form.*;
import com.lyusantu.easy.base.module.support.dict.domain.vo.DictKeyVO;
import com.lyusantu.easy.base.module.support.dict.domain.vo.DictValueVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 服务
 */
@RequiredArgsConstructor
@Service
public class DictService {

    private final DictKeyMapper dictKeyMapper;

    private final DictValueMapper dictValueMapper;

    private final DictCacheService dictCacheService;


    /**
     * key添加
     *
     * @param keyAddForm
     * @return
     */
    public synchronized ResponseDTO<String> keyAdd(DictKeyAddForm keyAddForm) {
        DictKeyEntity dictKeyEntity = dictKeyMapper.selectByCode(keyAddForm.getKeyCode(), false);
        if (dictKeyEntity != null) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        dictKeyEntity = BeanUtil.copy(keyAddForm, DictKeyEntity.class);
        dictKeyMapper.insert(dictKeyEntity);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * 值添加
     *
     * @param valueAddForm
     * @return
     */
    public synchronized ResponseDTO<String> valueAdd(DictValueAddForm valueAddForm) {
        DictValueEntity dictValueEntity = dictValueMapper.selectByCode(valueAddForm.getValueCode(), false);
        if (dictValueEntity != null) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        dictValueEntity = BeanUtil.copy(valueAddForm, DictValueEntity.class);
        dictValueMapper.insert(dictValueEntity);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * key 编辑
     *
     * @param keyUpdateForm
     * @return
     */
    public synchronized ResponseDTO<String> keyEdit(DictKeyUpdateForm keyUpdateForm) {
        DictKeyEntity dictKeyEntity = dictKeyMapper.selectByCode(keyUpdateForm.getKeyCode(), false);
        if (dictKeyEntity != null && !dictKeyEntity.getDictKeyId().equals(keyUpdateForm.getDictKeyId())) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        DictKeyEntity dictKeyUpdateEntity = BeanUtil.copy(keyUpdateForm, DictKeyEntity.class);
        dictKeyMapper.updateById(dictKeyUpdateEntity);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * 值编辑
     *
     * @param valueUpdateForm
     * @return
     */
    public synchronized ResponseDTO<String> valueEdit(DictValueUpdateForm valueUpdateForm) {
        DictKeyEntity dictKeyEntity = dictKeyMapper.selectById(valueUpdateForm.getDictKeyId());
        if (dictKeyEntity == null || dictKeyEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("key不能存在");
        }
        DictValueEntity dictValueEntity = dictValueMapper.selectByCode(valueUpdateForm.getValueCode(), false);
        if (dictValueEntity != null && !dictValueEntity.getDictValueId().equals(valueUpdateForm.getDictValueId())) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        DictValueEntity dictValueUpdateEntity = BeanUtil.copy(valueUpdateForm, DictValueEntity.class);
        dictValueMapper.updateById(dictValueUpdateEntity);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * key删除
     *
     * @param keyIdList
     * @return
     */
    public synchronized ResponseDTO<String> keyDelete(List<Long> keyIdList) {
        if (CollectionUtils.isEmpty(keyIdList)) {
            return ResponseDTO.ok();
        }
        dictKeyMapper.updateDeletedFlagByIdList(keyIdList, true);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * 值删除
     *
     * @param valueIdList
     * @return
     */
    public synchronized ResponseDTO<String> valueDelete(List<Long> valueIdList) {
        if (CollectionUtils.isEmpty(valueIdList)) {
            return ResponseDTO.ok();
        }
        dictValueMapper.updateDeletedFlagByIdList(valueIdList, true);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * 分页查询key
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DictKeyVO>> keyQuery(DictKeyQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<DictKeyVO> list = dictKeyMapper.query(page, queryForm);
        PageResult<DictKeyVO> pageResult = PageUtil.convert2PageResult(page, list);
        if (pageResult.getEmptyFlag()) {
            return ResponseDTO.ok(pageResult);
        }
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 所有key
     *
     * @return
     */
    public List<DictKeyVO> queryAllKey() {
        return BeanUtil.copyList(dictKeyMapper.selectByDeletedFlag(false), DictKeyVO.class);
    }

    /**
     * 分页查询值
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DictValueVO>> valueQuery(DictValueQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<DictValueVO> list = dictValueMapper.query(page, queryForm);
        PageResult<DictValueVO> pageResult = PageUtil.convert2PageResult(page, list);
        if (pageResult.getEmptyFlag()) {
            return ResponseDTO.ok(pageResult);
        }
        return ResponseDTO.ok(pageResult);
    }


}
