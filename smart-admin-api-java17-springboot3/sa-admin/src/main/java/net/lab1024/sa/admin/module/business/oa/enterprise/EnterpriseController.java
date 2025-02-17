package net.lab1024.sa.admin.module.business.oa.enterprise;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.form.*;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseEmployeeVO;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseExcelVO;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseListVO;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.sa.admin.util.AdminRequestUtil;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.enumeration.DateFormatterEnum;
import net.lab1024.sa.base.common.util.*;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = AdminSwaggerTagConst.Business.OA_ENTERPRISE)
@OperateLog
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    @Operation(summary = "分页查询企业模块")
    @PostMapping("/oa/enterprise/page/query")
    @SaCheckPermission("oa:enterprise:query")
    public ResponseDTO<PageResult<EnterpriseVO>> queryByPage(@RequestBody @Valid EnterpriseQueryForm queryForm) {
        return enterpriseService.queryByPage(queryForm);
    }

    @Operation(summary = "导出企业信息")
    @PostMapping("/oa/enterprise/exportExcel")
    public void exportExcel(@RequestBody @Valid EnterpriseQueryForm queryForm, HttpServletResponse response) throws IOException {
        List<EnterpriseExcelVO> data = enterpriseService.getExcelExportData(queryForm);
        if (CollectionUtils.isEmpty(data)) {
            ResponseUtil.write(response, ResponseDTO.userErrorParam("暂无数据"));
            return;
        }

        String watermark = AdminRequestUtil.getRequestUser().getActualName();
        watermark += LocalDateUtil.format(LocalDateTime.now(), DateFormatterEnum.YMD_HMS);

        ExcelUtil.exportExcelWithWatermark(response,"企业基本信息.xlsx","企业信息",EnterpriseExcelVO.class,data,watermark);

    }

    @Operation(summary = "查询企业详情")
    @GetMapping("/oa/enterprise/get/{enterpriseId}")
    @SaCheckPermission("oa:enterprise:detail")
    public ResponseDTO<EnterpriseVO> getDetail(@PathVariable Long enterpriseId) {
        return ResponseDTO.ok(enterpriseService.getDetail(enterpriseId));
    }

    @Operation(summary = "新建企业")
    @PostMapping("/oa/enterprise/create")
    @SaCheckPermission("oa:enterprise:add")
    public ResponseDTO<String> createEnterprise(@RequestBody @Valid EnterpriseCreateForm createVO) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return enterpriseService.createEnterprise(createVO);
    }

    @Operation(summary = "编辑企业")
    @PostMapping("/oa/enterprise/update")
    @SaCheckPermission("oa:enterprise:update")
    public ResponseDTO<String> updateEnterprise(@RequestBody @Valid EnterpriseUpdateForm updateVO) {
        return enterpriseService.updateEnterprise(updateVO);
    }

    @Operation(summary = "删除企业")
    @GetMapping("/oa/enterprise/delete/{enterpriseId}")
    @SaCheckPermission("oa:enterprise:delete")
    public ResponseDTO<String> deleteEnterprise(@PathVariable Long enterpriseId) {
        return enterpriseService.deleteEnterprise(enterpriseId);
    }

    @Operation(summary = "按照类型查询企业")
    @GetMapping("/oa/enterprise/query/list")
    public ResponseDTO<List<EnterpriseListVO>> queryList(@RequestParam(value = "type", required = false) Integer type) {
        return enterpriseService.queryList(type);
    }


    @Operation(summary = "企业添加员工")
    @PostMapping("/oa/enterprise/employee/add")
    @SaCheckPermission("oa:enterprise:addEmployee")
    public ResponseDTO<String> addEmployee(@RequestBody @Valid EnterpriseEmployeeForm enterpriseEmployeeForm) {
        return enterpriseService.addEmployee(enterpriseEmployeeForm);
    }

    @Operation(summary = "查询企业全部员工")
    @PostMapping("/oa/enterprise/employee/list")
    public ResponseDTO<List<EnterpriseEmployeeVO>> employeeList(@RequestBody @Valid List<Long> enterpriseIdList) {
        return ResponseDTO.ok(enterpriseService.employeeList(enterpriseIdList));
    }

    @Operation(summary = "分页查询企业员工")
    @PostMapping("/oa/enterprise/employee/queryPage")
    public ResponseDTO<PageResult<EnterpriseEmployeeVO>> queryPageEmployeeList(@RequestBody @Valid EnterpriseEmployeeQueryForm queryForm) {
        return ResponseDTO.ok(enterpriseService.queryPageEmployeeList(queryForm));
    }


    @Operation(summary = "企业删除员工")
    @PostMapping("/oa/enterprise/employee/delete")
    @SaCheckPermission("oa:enterprise:deleteEmployee")
    public ResponseDTO<String> deleteEmployee(@RequestBody @Valid EnterpriseEmployeeForm enterpriseEmployeeForm) {
        return enterpriseService.deleteEmployee(enterpriseEmployeeForm);
    }
}
