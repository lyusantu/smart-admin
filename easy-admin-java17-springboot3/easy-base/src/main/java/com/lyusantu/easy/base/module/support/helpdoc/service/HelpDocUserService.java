package com.lyusantu.easy.base.module.support.helpdoc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.module.support.helpdoc.mapper.HelpDocMapper;
import com.lyusantu.easy.base.module.support.helpdoc.domain.entity.HelpDocEntity;
import com.lyusantu.easy.base.module.support.helpdoc.domain.form.HelpDocViewRecordQueryForm;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocDetailVO;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocVO;
import com.lyusantu.easy.base.module.support.helpdoc.domain.vo.HelpDocViewRecordVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户查看  帮助文档
 */
@RequiredArgsConstructor
@Service
public class HelpDocUserService {

    private final HelpDocMapper helpDocMapper;


    /**
     * 查询全部 帮助文档
     *
     * @return
     */
    public ResponseDTO<List<HelpDocVO>> queryAllHelpDocList() {
        return ResponseDTO.ok(helpDocMapper.queryAllHelpDocList());
    }


    /**
     * 查询我的 待查看的 帮助文档清单
     *
     * @return
     */
    public ResponseDTO<HelpDocDetailVO> view(RequestUser requestUser, Long helpDocId) {
        HelpDocEntity helpDocEntity = helpDocMapper.selectById(helpDocId);
        if (helpDocEntity == null) {
            return ResponseDTO.userErrorParam("帮助文档不存在");
        }

        HelpDocDetailVO helpDocDetailVO = BeanUtil.copy(helpDocEntity, HelpDocDetailVO.class);
        long viewCount = helpDocMapper.viewRecordCount(helpDocId, requestUser.getUserId());
        if (viewCount == 0) {
            helpDocMapper.insertViewRecord(helpDocId, requestUser.getUserId(), requestUser.getUserName(), requestUser.getIp(), requestUser.getUserAgent(), 1);
            helpDocMapper.updateViewCount(helpDocId, 1, 1);
            helpDocDetailVO.setPageViewCount(helpDocDetailVO.getPageViewCount() + 1);
            helpDocDetailVO.setUserViewCount(helpDocDetailVO.getUserViewCount() + 1);
        } else {
            helpDocMapper.updateViewRecord(helpDocId, requestUser.getUserId(), requestUser.getIp(), requestUser.getUserAgent());
            helpDocMapper.updateViewCount(helpDocId, 0, 1);
            helpDocDetailVO.setPageViewCount(helpDocDetailVO.getPageViewCount() + 1);
        }

        return ResponseDTO.ok(helpDocDetailVO);
    }


    /**
     * 分页查询  查看记录
     *
     * @param helpDocViewRecordQueryForm
     * @return
     */
    public PageResult<HelpDocViewRecordVO> queryViewRecord(HelpDocViewRecordQueryForm helpDocViewRecordQueryForm) {
        Page<?> page = PageUtil.convert2PageQuery(helpDocViewRecordQueryForm);
        List<HelpDocViewRecordVO> noticeViewRecordVOS = helpDocMapper.queryViewRecordList(page, helpDocViewRecordQueryForm);
        return PageUtil.convert2PageResult(page, noticeViewRecordVOS);
    }
}
