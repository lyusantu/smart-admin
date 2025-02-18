package com.lyusantu.easy.base.module.support.helpdoc.service;

import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.module.support.helpdoc.mapper.HelpDocCatalogMapper;
import com.lyusantu.easy.base.module.support.helpdoc.mapper.HelpDocMapper;
import com.lyusantu.easy.base.module.support.helpdoc.domain.entity.HelpDocCatalogEntity;
import com.lyusantu.easy.base.module.support.helpdoc.domain.form.HelpDocCatalogAddForm;
import com.lyusantu.easy.base.module.support.helpdoc.domain.form.HelpDocCatalogUpdateForm;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocCatalogVO;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 帮助文档 目录
 */
@RequiredArgsConstructor
@Service
public class HelpDocCatalogService {

    private final HelpDocMapper helpDocMapper;

    private final HelpDocCatalogMapper helpDocCatalogMapper;

    /**
     * 查询全部目录
     *
     * @return
     */
    public List<HelpDocCatalogVO> getAll() {
        return BeanUtil.copyList(helpDocCatalogMapper.selectList(null), HelpDocCatalogVO.class);
    }

    /**
     * 添加目录
     *
     * @param helpDocCatalogAddForm
     * @return
     */
    public synchronized ResponseDTO<String> add(HelpDocCatalogAddForm helpDocCatalogAddForm) {
        List<HelpDocCatalogVO> helpDocCatalogList = getAll();
        Optional<HelpDocCatalogVO> exist = helpDocCatalogList.stream().filter(e -> helpDocCatalogAddForm.getName().equals(e.getName())).findFirst();
        if (exist.isPresent()) {
            return ResponseDTO.userErrorParam("存在相同名称的目录了");
        }

        helpDocCatalogMapper.insert(BeanUtil.copy(helpDocCatalogAddForm, HelpDocCatalogEntity.class));
        return ResponseDTO.ok();
    }

    /**
     * 更新目录
     *
     * @param updateForm
     * @return
     */
    public synchronized ResponseDTO<String> update(HelpDocCatalogUpdateForm updateForm) {
        HelpDocCatalogEntity helpDocCatalogEntity = helpDocCatalogMapper.selectById(updateForm.getHelpDocCatalogId());
        if (helpDocCatalogEntity == null) {
            return ResponseDTO.userErrorParam("目录不存在");
        }

        List<HelpDocCatalogVO> helpDocCatalogList = getAll();
        Optional<HelpDocCatalogVO> exist = helpDocCatalogList.stream().filter(e -> updateForm.getName().equals(e.getName())).findFirst();
        if (exist.isPresent() && !exist.get().getHelpDocCatalogId().equals(updateForm.getHelpDocCatalogId())) {
            return ResponseDTO.userErrorParam("存在相同名称的目录了");
        }
        helpDocCatalogMapper.updateById(BeanUtil.copy(updateForm, HelpDocCatalogEntity.class));
        return ResponseDTO.ok();
    }

    /**
     * 删除目录（如果有子目录、或者有帮助文档，则不能删除）
     *
     * @param helpDocCatalogId
     * @return
     */
    public synchronized ResponseDTO<String> delete(Long helpDocCatalogId) {
        if (helpDocCatalogId == null) {
            return ResponseDTO.ok();
        }

        HelpDocCatalogEntity helpDocCatalogEntity = helpDocCatalogMapper.selectById(helpDocCatalogId);
        if (helpDocCatalogEntity == null) {
            return ResponseDTO.userErrorParam("目录不存在");
        }

        //如果有子目录，则不能删除
        Optional<HelpDocCatalogVO> existOptional = getAll().stream().filter(e -> helpDocCatalogId.equals(e.getParentId())).findFirst();
        if (existOptional.isPresent()) {
            return ResponseDTO.userErrorParam("存在子目录：" + existOptional.get().getName());
        }

        //查询是否有帮助文档
        List<HelpDocVO> helpDocVOList = helpDocMapper.queryHelpDocByCatalogId(helpDocCatalogId);
        if (CollectionUtils.isNotEmpty(helpDocVOList)) {
            return ResponseDTO.userErrorParam("目录下存在文档，不能删除");
        }
        helpDocCatalogMapper.deleteById(helpDocCatalogId);
        return ResponseDTO.ok();
    }

}
