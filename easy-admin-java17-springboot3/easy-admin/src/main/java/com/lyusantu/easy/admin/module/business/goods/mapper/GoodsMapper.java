package com.lyusantu.easy.admin.module.business.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.admin.module.business.goods.domain.entity.GoodsEntity;
import com.lyusantu.easy.admin.module.business.goods.domain.form.GoodsQueryForm;
import com.lyusantu.easy.admin.module.business.goods.domain.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品
 */
@Mapper
@Component
public interface GoodsMapper extends BaseMapper<GoodsEntity> {

    /**
     * 分页 查询商品
     */
    List<GoodsVO> query(Page page, @Param("query") GoodsQueryForm query);

    /**
     * 批量更新删除状态
     */

    void batchUpdateDeleted(@Param("goodsIdList") List<Long> goodsIdList, @Param("deletedFlag") Boolean deletedFlag);
}
