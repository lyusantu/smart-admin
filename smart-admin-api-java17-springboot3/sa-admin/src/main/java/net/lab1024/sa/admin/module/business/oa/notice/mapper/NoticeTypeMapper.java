package net.lab1024.sa.admin.module.business.oa.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.oa.notice.domain.entity.NoticeTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 通知公告类型
 */
@Mapper
@Component
public interface NoticeTypeMapper extends BaseMapper<NoticeTypeEntity> {

}
