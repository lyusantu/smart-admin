package com.lyusantu.easy.admin.module.system.employee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.lyusantu.easy.base.common.domain.page.PageParam;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 员工列表
 */
@Data
public class EmployeeQueryForm extends PageParam {

    @Schema(description = "搜索词")
    @Length(max = 20, message = "搜索词最多20字符")
    private String keyword;

    @Schema(description = "部门id")
    private Long departmentId;

    @Schema(description = "是否禁用")
    private Boolean disabledFlag;

    @Schema(description = "员工id集合")
    @Size(max = 99, message = "最多查询99个员工")
    private List<Long> employeeIdList;

    @Schema(description = "删除标识", hidden = true)
    private Boolean deletedFlag;

}
