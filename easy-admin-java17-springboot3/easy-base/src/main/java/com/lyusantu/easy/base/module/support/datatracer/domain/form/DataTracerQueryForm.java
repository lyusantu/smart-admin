package com.lyusantu.easy.base.module.support.datatracer.domain.form;

import com.lyusantu.easy.base.module.support.datatracer.constant.DataTracerTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.lyusantu.easy.base.common.domain.page.PageParam;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;

/**
 * 查询表单
 */
@Data
public class DataTracerQueryForm extends PageParam {

    @SchemaEnum(DataTracerTypeEnum.class)
    private Integer type;

    @Schema(description = "业务id")
    @NotNull(message = "业务id不能为空")
    private Long dataId;

    @Schema(description = "关键字")
    private String keywords;
}
