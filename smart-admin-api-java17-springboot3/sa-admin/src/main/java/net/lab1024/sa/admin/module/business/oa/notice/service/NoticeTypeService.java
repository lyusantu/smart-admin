package net.lab1024.sa.admin.module.business.oa.notice.service;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.lab1024.sa.admin.module.business.oa.notice.mapper.NoticeTypeMapper;
import net.lab1024.sa.admin.module.business.oa.notice.domain.entity.NoticeTypeEntity;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeTypeVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.BeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 通知。公告 类型
 */
@RequiredArgsConstructor
@Service
public class NoticeTypeService {

    private final NoticeTypeMapper noticeTypeMapper;

    /**
     * 查询全部
     * @return
     */
    public List<NoticeTypeVO> getAll() {
        return BeanUtil.copyList(noticeTypeMapper.selectList(null), NoticeTypeVO.class);
    }

    public NoticeTypeVO getByNoticeTypeId(Long noticceTypeId) {
        return BeanUtil.copy(noticeTypeMapper.selectById(noticceTypeId), NoticeTypeVO.class);
    }

    public synchronized ResponseDTO<String> add(String name) {
        if (StrUtil.isBlank(name)) {
            return ResponseDTO.userErrorParam("类型名称不能为空");
        }

        List<NoticeTypeEntity> noticeTypeEntityList = noticeTypeMapper.selectList(null);
        if (!CollectionUtils.isEmpty(noticeTypeEntityList)) {
            boolean exist = noticeTypeEntityList.stream().map(NoticeTypeEntity::getNoticeTypeName).collect(Collectors.toSet()).contains(name);
            if (exist) {
                return ResponseDTO.userErrorParam("类型名称已经存在");
            }
        }
        noticeTypeMapper.insert(NoticeTypeEntity.builder().noticeTypeName(name).build());
        return ResponseDTO.ok();
    }

    public synchronized ResponseDTO<String> update(Long noticeTypeId, String name) {
        if (StrUtil.isBlank(name)) {
            return ResponseDTO.userErrorParam("类型名称不能为空");
        }

        NoticeTypeEntity noticeTypeEntity = noticeTypeMapper.selectById(noticeTypeId);
        if (noticeTypeEntity == null) {
            return ResponseDTO.userErrorParam("类型名称不存在");
        }

        List<NoticeTypeEntity> noticeTypeEntityList = noticeTypeMapper.selectList(null);
        if (!CollectionUtils.isEmpty(noticeTypeEntityList)) {
            Optional<NoticeTypeEntity> optionalNoticeTypeEntity = noticeTypeEntityList.stream().filter(e -> e.getNoticeTypeName().equals(name)).findFirst();
            if (optionalNoticeTypeEntity.isPresent() && !optionalNoticeTypeEntity.get().getNoticeTypeId().equals(noticeTypeId)) {
                return ResponseDTO.userErrorParam("类型名称已经存在");
            }
        }
        noticeTypeEntity.setNoticeTypeName(name);
        noticeTypeMapper.updateById(noticeTypeEntity);
        return ResponseDTO.ok();
    }

    public synchronized ResponseDTO<String> delete(Long noticeTypeId) {
        noticeTypeMapper.deleteById(noticeTypeId);
        return ResponseDTO.ok();
    }

}
