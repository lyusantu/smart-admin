package net.lab1024.sa.base.module.support.feedback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.BeanUtil;
import net.lab1024.sa.base.common.util.PageUtil;
import net.lab1024.sa.base.module.support.feedback.mapper.FeedbackMapper;
import net.lab1024.sa.base.module.support.feedback.domain.FeedbackAddForm;
import net.lab1024.sa.base.module.support.feedback.domain.FeedbackEntity;
import net.lab1024.sa.base.module.support.feedback.domain.FeedbackQueryForm;
import net.lab1024.sa.base.module.support.feedback.domain.FeedbackVO;
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
