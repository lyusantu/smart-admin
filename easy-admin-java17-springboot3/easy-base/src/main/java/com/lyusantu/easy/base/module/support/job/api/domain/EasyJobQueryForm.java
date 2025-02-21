package com.lyusantu.easy.base.module.support.job.api.domain;

import com.lyusantu.easy.base.module.support.job.constant.EasyJobTriggerTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.domain.page.PageParam;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;
import com.lyusantu.easy.base.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

/**
 * 定时任务 分页查询
 */
@Data
public class EasyJobQueryForm extends PageParam {

    @Schema(description = "搜索词|可选")
    @Length(max = 50, message = "搜索词最多50字符")
    private String searchWord;

    @SchemaEnum(desc = "触发类型", value = EasyJobTriggerTypeEnum.class)
    @CheckEnum(value = EasyJobTriggerTypeEnum.class, message = "触发类型错误")
    private String triggerType;

    @Schema(description = "是否启用|可选")
    private Boolean enabledFlag;

    @Schema(description = "是否删除|可选")
    private Boolean deletedFlag;
}
