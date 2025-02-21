package com.lyusantu.easy.base.module.support.codegenerator.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.domain.page.PageParam;


/**
 * 查询表数据
 */
@Data
public class TableQueryForm extends PageParam {

    @Schema(description = "表名关键字")
    private String tableNameKeywords;

}
