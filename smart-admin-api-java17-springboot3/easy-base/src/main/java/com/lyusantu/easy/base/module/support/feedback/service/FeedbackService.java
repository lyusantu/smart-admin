package com.lyusantu.easy.base.module.support.feedback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackQueryForm;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.module.support.feedback.mapper.FeedbackMapper;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackAddForm;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackEntity;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackVO;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 意见反馈
 */
@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;

    /**
     * 分页查询
     *
     */
    public ResponseDTO<PageResult<FeedbackVO>> query(FeedbackQueryForm queryForm) {
        Page page = PageUtil.convert2PageQuery(queryForm);
        List<FeedbackVO> list = feedbackMapper.queryPage(page, queryForm);
        PageResult<FeedbackVO> pageResultDTO = PageUtil.convert2PageResult(page, list);
        if (pageResultDTO.getEmptyFlag()) {
            return ResponseDTO.ok(pageResultDTO);
        }
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 新建
     */
    public ResponseDTO<String> add(FeedbackAddForm addForm, RequestUser requestUser) {
        FeedbackEntity feedback = BeanUtil.copy(addForm, FeedbackEntity.class);
        feedback.setUserType(requestUser.getUserType().getValue());
        feedback.setUserId(requestUser.getUserId());
        feedback.setUserName(requestUser.getUserName());
        feedbackMapper.insert(feedback);
        return ResponseDTO.ok();
    }
}
