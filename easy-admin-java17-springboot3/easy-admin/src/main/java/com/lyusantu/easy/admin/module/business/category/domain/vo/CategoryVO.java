package com.lyusantu.easy.admin.module.business.category.domain.vo;

import com.lyusantu.easy.admin.module.business.category.constant.CategoryTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;

import java.time.LocalDateTime;

/**
 * 类目
 */
@Data
public class CategoryVO {

    @Schema(description = "类目名称", required = true)
    private String categoryName;

    @SchemaEnum(desc = "分类类型", value = CategoryTypeEnum.class)
    private Integer categoryType;

    @Schema(description = "父级类目id|可选")
    private Long parentId;

    @Schema(description = "排序|可选")
    private Integer sort;

    @Schema(description = "备注|可选")
    private String remark;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "类目id")
    private Long categoryId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
