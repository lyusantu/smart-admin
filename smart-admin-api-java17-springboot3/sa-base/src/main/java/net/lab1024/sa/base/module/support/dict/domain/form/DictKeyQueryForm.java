package net.lab1024.sa.base.module.support.dict.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.page.PageParam;

/**
 * 字典
 */
@Data
public class DictKeyQueryForm extends PageParam {

    @Schema(description = "搜索词")
    private String searchWord;

    @Schema(description = "删除标识",hidden = true)
    private Boolean deletedFlag;
}
