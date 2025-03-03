package com.lyusantu.easy.admin.module.business.oa.notice.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 通知公告 员工查看
 */
@Data
public class NoticeEmployeeVO extends NoticeVO {

    @Schema(description = "是否查看")
    private Boolean viewFlag;

    @Schema(description = "发布日期")
    private LocalDate publishDate;

}
