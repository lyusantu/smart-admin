package com.lyusantu.easy.admin.module.business.oa.notice.manager;

import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.module.business.oa.notice.mapper.NoticeMapper;
import com.lyusantu.easy.admin.module.business.oa.notice.domain.entity.NoticeEntity;
import com.lyusantu.easy.admin.module.business.oa.notice.domain.form.NoticeVisibleRangeForm;
import com.lyusantu.easy.base.module.support.datatracer.constant.DataTracerTypeEnum;
import com.lyusantu.easy.base.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知、公告 manager
 */
@RequiredArgsConstructor
@Service
public class NoticeManager {

    private final NoticeMapper noticeMapper;

    private final DataTracerService dataTracerService;

    /**
     * 保存
     */
    @Transactional(rollbackFor = Throwable.class)
    public void save(NoticeEntity noticeEntity, List<NoticeVisibleRangeForm> visibleRangeFormList) {
        noticeMapper.insert(noticeEntity);
        Long noticeId = noticeEntity.getNoticeId();
        // 保存可见范围
        if (CollectionUtils.isNotEmpty(visibleRangeFormList)) {
            noticeMapper.insertVisibleRange(noticeId, visibleRangeFormList);
        }
        dataTracerService.insert(noticeId, DataTracerTypeEnum.OA_NOTICE);
    }

    /**
     * 更新
     *
     */
    @Transactional(rollbackFor = Throwable.class)
    public void update(NoticeEntity old, NoticeEntity noticeEntity, List<NoticeVisibleRangeForm> visibleRangeList) {
        noticeMapper.updateById(noticeEntity);
        Long noticeId = noticeEntity.getNoticeId();
        // 保存可见范围
        if (CollectionUtils.isNotEmpty(visibleRangeList)) {
            noticeMapper.deleteVisibleRange(noticeId);
            noticeMapper.insertVisibleRange(noticeId, visibleRangeList);
        }
        dataTracerService.update(noticeId, DataTracerTypeEnum.OA_NOTICE, old, noticeEntity);
    }
}
