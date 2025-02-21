package com.lyusantu.easy.admin.module.system.position.mapper;

import java.util.List;

import com.lyusantu.easy.admin.module.system.position.domain.entity.PositionEntity;
import com.lyusantu.easy.admin.module.system.position.domain.form.PositionQueryForm;
import com.lyusantu.easy.admin.module.system.position.domain.vo.PositionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 职务表 Mapper
 */

@Mapper
@Component
public interface PositionMapper extends BaseMapper<PositionEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<PositionVO> queryPage(Page page, @Param("queryForm") PositionQueryForm queryForm);


    /**
     * 查询
     * @param deletedFlag
     * @return
     */
    List<PositionVO> queryList(@Param("deletedFlag") Boolean deletedFlag);
}
