package com.lyusantu.easy.admin.module.system.employee.domain.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import com.lyusantu.easy.base.common.enumeration.GenderEnum;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;
import com.lyusantu.easy.base.common.util.VerificationUtil;
import com.lyusantu.easy.base.common.validator.enumeration.CheckEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 更新员工个人中心信息
 */
@Data
public class EmployeeUpdateCenterForm {

    @Schema(hidden = true)
    private Long employeeId;

    @Schema(description = "姓名")
    @NotNull(message = "姓名不能为空")
    @Length(max = 30, message = "姓名最多30字符")
    private String actualName;

    @SchemaEnum(GenderEnum.class)
    @CheckEnum(value = GenderEnum.class, message = "性别错误")
    private Integer gender;

    @Schema(description = "手机号")
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = VerificationUtil.PHONE_REGEXP, message = "手机号格式不正确")
    private String phone;

    @Schema(description = "邮箱账号")
    @NotNull(message = "邮箱账号不能为空")
    @Pattern(regexp = VerificationUtil.EMAIL, message = "邮箱账号格式不正确")
    private String email;

    @Schema(description = "职务级别ID")
    private Long positionId;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "备注")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;
}
