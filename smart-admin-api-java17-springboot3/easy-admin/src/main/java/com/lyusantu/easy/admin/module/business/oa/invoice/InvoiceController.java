package com.lyusantu.easy.admin.module.business.oa.invoice;

import com.lyusantu.easy.admin.module.business.oa.invoice.domain.InvoiceAddForm;
import com.lyusantu.easy.admin.module.business.oa.invoice.domain.InvoiceQueryForm;
import com.lyusantu.easy.admin.module.business.oa.invoice.domain.InvoiceUpdateForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.admin.constant.AdminSwaggerTagConst;
import com.lyusantu.easy.admin.module.business.oa.invoice.domain.InvoiceVO;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OA发票信息
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = AdminSwaggerTagConst.Business.OA_INVOICE)
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Operation(summary = "分页查询发票信息")
    @PostMapping("/oa/invoice/page/query")
    public ResponseDTO<PageResult<InvoiceVO>> queryByPage(@RequestBody @Valid InvoiceQueryForm queryForm) {
        return invoiceService.queryByPage(queryForm);
    }

    @Operation(summary = "查询发票信息详情")
    @GetMapping("/oa/invoice/get/{invoiceId}")
    public ResponseDTO<InvoiceVO> getDetail(@PathVariable Long invoiceId) {
        return invoiceService.getDetail(invoiceId);
    }

    @Operation(summary = "新建发票信息")
    @PostMapping("/oa/invoice/create")
    public ResponseDTO<String> createInvoice(@RequestBody @Valid InvoiceAddForm createVO) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return invoiceService.createInvoice(createVO);
    }

    @Operation(summary = "编辑发票信息")
    @PostMapping("/oa/invoice/update")
    @OperateLog
    public ResponseDTO<String> updateInvoice(@RequestBody @Valid InvoiceUpdateForm updateVO) {
        return invoiceService.updateInvoice(updateVO);
    }

    @Operation(summary = "删除发票信息")
    @GetMapping("/invoice/delete/{invoiceId}")
    public ResponseDTO<String> deleteInvoice(@PathVariable Long invoiceId) {
        return invoiceService.deleteInvoice(invoiceId);
    }

    @Operation(summary = "查询列表")
    @GetMapping("/oa/invoice/query/list/{enterpriseId}")
    public ResponseDTO<List<InvoiceVO>> queryList(@PathVariable Long enterpriseId) {
        return invoiceService.queryList(enterpriseId);
    }


}
