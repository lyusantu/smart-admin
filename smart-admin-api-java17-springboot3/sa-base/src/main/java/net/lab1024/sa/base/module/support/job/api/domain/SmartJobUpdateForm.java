package net.lab1024.sa.base.module.support.job.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.base.module.support.job.constant.SmartJobTriggerTypeEnum;
import org.hibernate.validator.constraints.Length;

/**
 * 定时任务 更新
 */
@Data
public class SmartJobUpdateForm extends SmartJobAddForm {

    @Schema(description = "任务id")
    @NotNull(message = "任务id不能为空")
    private Integer jobId;
}
