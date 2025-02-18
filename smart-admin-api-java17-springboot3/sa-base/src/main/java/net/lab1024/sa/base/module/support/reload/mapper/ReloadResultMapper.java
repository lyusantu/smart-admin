package net.lab1024.sa.base.module.support.reload.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.base.module.support.reload.domain.ReloadResultEntity;
import net.lab1024.sa.base.module.support.reload.domain.ReloadResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * t_reload_result Mapper
 */
@Component
@Mapper
public interface ReloadResultMapper extends BaseMapper<ReloadResultEntity> {

    List<ReloadResultVO> query(@Param("tag") String tag);
}
