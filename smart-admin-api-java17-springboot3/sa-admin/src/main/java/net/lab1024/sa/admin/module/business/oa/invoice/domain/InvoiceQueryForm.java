package net.lab1024.sa.admin.module.business.oa.invoice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.page.PageParam;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * OA发票信息查询
 */
@Data
public class InvoiceQueryForm extends PageParam {

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "关键字")
    @Length(max = 200, message = "关键字最多200字符")
    private String keywords;

    @Schema(description = "开始时间")
    private LocalDate startTime;

    @Schema(description = "结束时间")
    private LocalDate endTime;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;
}
