package com.lyusantu.easy.base.module.support.feedback.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackQueryForm;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackEntity;
import com.lyusantu.easy.base.module.support.feedback.domain.FeedbackVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 意见反馈 Mapper
 */
@Mapper
@Component
public interface FeedbackMapper extends BaseMapper<FeedbackEntity> {

    /**
     * 分页查询
     */
    List<FeedbackVO> queryPage(Page page, @Param("query") FeedbackQueryForm query);
}
