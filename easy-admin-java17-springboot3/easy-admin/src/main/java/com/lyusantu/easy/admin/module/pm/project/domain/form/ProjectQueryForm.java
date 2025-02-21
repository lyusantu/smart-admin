package com.lyusantu.easy.admin.module.pm.project.domain.form;

import com.lyusantu.easy.base.common.domain.page.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目信息表 分页查询表单
 *
 * @Author lyusantu
 * @Date 2025-02-20 10:29:51
 * @Copyright lyusantu
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectQueryForm extends PageParam {

    @Schema(description = "请输入项目名称")
    private String projectName;

    @Schema(description = "项目开始时间")
    private LocalDate startTime;

    @Schema(description = "项目开始时间")
    private LocalDate endTime;

    @Schema(description = "状态")
    private Integer state;

}
