package com.lyusantu.easy.admin.module.pm.node.mapper;

import com.lyusantu.easy.admin.module.pm.node.domain.entity.NodeEntity;
import com.lyusantu.easy.admin.module.pm.node.domain.form.NodeQueryForm;
import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeTemplateListVO;
import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeVO;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 节点模板表 Mapper
 *
 * @Author lyusantu
 * @Date 2025-02-19 14:33:24
 * @Copyright lyusantu
 */

@Mapper
@Component
public interface NodeMapper extends BaseMapper<NodeEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<NodeVO> queryPage(Page page, @Param("queryForm") NodeQueryForm queryForm);

    /**
     * 更新删除状态
     */
    long updateDeleted(@Param("nodeId") Long nodeId, @Param("deletedFlag") boolean deletedFlag);

    /**
     * 批量更新删除状态
     */
    void batchUpdateDeleted(@Param("idList") List<Long> idList, @Param("deletedFlag") boolean deletedFlag);

    /**
     * 批量删除
     */
    void batchDeleted(@Param("idList") List<Long> idList);

    /**
     * 节点模板列表
     */
    List<NodeTemplateListVO> listNodeTemplate();

}
