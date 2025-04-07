package com.lyusantu.easy.admin.module.business.goods.service;

import cn.idev.excel.FastExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.admin.module.business.goods.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.admin.module.business.category.constant.CategoryTypeEnum;
import com.lyusantu.easy.admin.module.business.category.domain.entity.CategoryEntity;
import com.lyusantu.easy.admin.module.business.category.service.CategoryQueryService;
import com.lyusantu.easy.admin.module.business.goods.constant.GoodsStatusEnum;
import com.lyusantu.easy.admin.module.business.goods.domain.entity.GoodsEntity;
import com.lyusantu.easy.admin.module.business.goods.domain.form.GoodsAddForm;
import com.lyusantu.easy.admin.module.business.goods.domain.form.GoodsImportForm;
import com.lyusantu.easy.admin.module.business.goods.domain.form.GoodsQueryForm;
import com.lyusantu.easy.admin.module.business.goods.domain.form.GoodsUpdateForm;
import com.lyusantu.easy.admin.module.business.goods.domain.vo.GoodsExcelVO;
import com.lyusantu.easy.admin.module.business.goods.domain.vo.GoodsVO;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.exception.BusinessException;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.EnumUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.module.support.datatracer.constant.DataTracerTypeEnum;
import com.lyusantu.easy.base.module.support.datatracer.service.DataTracerService;
import com.lyusantu.easy.base.module.support.dict.service.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsService {

    private final GoodsMapper goodsMapper;

    private final DictCacheService dictCacheService;

    private final DataTracerService dataTracerService;

    private final CategoryQueryService categoryQueryService;

    /**
     * 添加商品
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(GoodsAddForm addForm) {
        // 商品校验
        ResponseDTO<String> res = this.checkGoods(addForm);
        if (!res.getOk()) {
            return res;
        }
        GoodsEntity goodsEntity = BeanUtil.copy(addForm, GoodsEntity.class);
        goodsEntity.setDeletedFlag(Boolean.FALSE);
        goodsMapper.insert(goodsEntity);
        dataTracerService.insert(goodsEntity.getGoodsId(), DataTracerTypeEnum.GOODS);
        return ResponseDTO.ok();
    }

    /**
     * 更新商品
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(GoodsUpdateForm updateForm) {
        // 商品校验
        ResponseDTO<String> res = this.checkGoods(updateForm);
        if (!res.getOk()) {
            return res;
        }
        GoodsEntity originEntity = goodsMapper.selectById(updateForm.getGoodsId());
        GoodsEntity goodsEntity = BeanUtil.copy(updateForm, GoodsEntity.class);
        goodsMapper.updateById(goodsEntity);
        dataTracerService.update(updateForm.getGoodsId(), DataTracerTypeEnum.GOODS, originEntity, goodsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 添加/更新 商品校验
     */
    private ResponseDTO<String> checkGoods(GoodsAddForm addForm) {
        // 校验类目id
        Long categoryId = addForm.getCategoryId();
        Optional<CategoryEntity> optional = categoryQueryService.queryCategory(categoryId);
        if (optional.isEmpty() || !CategoryTypeEnum.GOODS.equalsValue(optional.get().getCategoryType())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "商品类目不存在~");
        }

        return ResponseDTO.ok();
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long goodsId) {
        GoodsEntity goodsEntity = goodsMapper.selectById(goodsId);
        if (goodsEntity == null) {
            return ResponseDTO.userErrorParam("商品不存在");
        }

        if (!goodsEntity.getGoodsStatus().equals(GoodsStatusEnum.SELL_OUT.getValue())) {
            return ResponseDTO.userErrorParam("只有售罄的商品才可以删除");
        }

        batchDelete(Collections.singletonList(goodsId));
        dataTracerService.batchDelete(Collections.singletonList(goodsId), DataTracerTypeEnum.GOODS);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> goodsIdList) {
        if (CollectionUtils.isEmpty(goodsIdList)) {
            return ResponseDTO.ok();
        }

        goodsMapper.batchUpdateDeleted(goodsIdList, Boolean.TRUE);
        return ResponseDTO.ok();
    }


    /**
     * 分页查询
     */
    public ResponseDTO<PageResult<GoodsVO>> query(GoodsQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<GoodsVO> list = goodsMapper.query(page, queryForm);
        PageResult<GoodsVO> pageResult = PageUtil.convert2PageResult(page, list);
        if (pageResult.getEmptyFlag()) {
            return ResponseDTO.ok(pageResult);
        }
        // 查询分类名称
        List<Long> categoryIdList = list.stream().map(GoodsVO::getCategoryId).distinct().collect(Collectors.toList());
        Map<Long, CategoryEntity> categoryMap = categoryQueryService.queryCategoryList(categoryIdList);
        list.forEach(e -> {
            CategoryEntity categoryEntity = categoryMap.get(e.getCategoryId());
            if (categoryEntity != null) {
                e.setCategoryName(categoryEntity.getCategoryName());
            }
        });
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 商品导入
     *
     * @param file 上传文件
     * @return 结果
     */
    public ResponseDTO<String> importGoods(MultipartFile file) {
        List<GoodsImportForm> dataList;
        try {
            dataList = FastExcel.read(file.getInputStream()).head(GoodsImportForm.class)
                    .sheet()
                    .doReadSync();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("数据格式存在问题，无法读取");
        }

        if (CollectionUtils.isEmpty(dataList)) {
            return ResponseDTO.userErrorParam("数据为空");
        }

        return ResponseDTO.okMsg("成功导入" + dataList.size() + "条，具体数据为：" + JSON.toJSONString(dataList));
    }

    /**
     * 商品导出
     */
    public List<GoodsExcelVO> getAllGoods() {
        List<GoodsEntity> goodsEntityList = goodsMapper.selectList(null);
        String keyCode="GODOS_PLACE";
        return goodsEntityList.stream()
                .map(e ->
                        GoodsExcelVO.builder()
                                .goodsStatus(EnumUtil.getEnumDescByValue(e.getGoodsStatus(), GoodsStatusEnum.class))
                                .categoryName(categoryQueryService.queryCategoryName(e.getCategoryId()))
                                .place(Arrays.stream(e.getPlace().split(",")).map(code -> dictCacheService.selectValueNameByValueCode(keyCode,code)).collect(Collectors.joining(",")))
                                .price(e.getPrice())
                                .goodsName(e.getGoodsName())
                                .remark(e.getRemark())
                                .build()
                )
                .collect(Collectors.toList());

    }
}
