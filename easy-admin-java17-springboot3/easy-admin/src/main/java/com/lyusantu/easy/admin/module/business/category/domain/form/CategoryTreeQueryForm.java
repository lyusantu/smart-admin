package com.lyusantu.easy.admin.module.business.category.domain.form;

import com.lyusantu.easy.admin.module.business.category.constant.CategoryTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;

/**
 * 类目 层级树查询
 */
@Data
public class CategoryTreeQueryForm {

    @SchemaEnum(desc = "分类类型|可选", value = CategoryTypeEnum.class)
    private Integer categoryType;

    @Schema(description = "父级类目id|可选")
    private Long parentId;
}
