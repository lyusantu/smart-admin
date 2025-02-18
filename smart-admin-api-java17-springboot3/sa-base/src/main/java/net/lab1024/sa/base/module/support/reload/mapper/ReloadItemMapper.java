package net.lab1024.sa.base.module.support.reload.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.base.module.support.reload.domain.ReloadItemEntity;
import net.lab1024.sa.base.module.support.reload.domain.ReloadItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * t_reload_item Mapper
 */
@Component
@Mapper
public interface ReloadItemMapper extends BaseMapper<ReloadItemEntity> {

    List<ReloadItemVO> query();
}
