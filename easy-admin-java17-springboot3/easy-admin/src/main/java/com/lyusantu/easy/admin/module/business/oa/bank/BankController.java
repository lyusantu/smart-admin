package com.lyusantu.easy.admin.module.business.oa.bank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.constant.AdminSwaggerTagConst;
import com.lyusantu.easy.admin.module.business.oa.bank.domain.BankCreateForm;
import com.lyusantu.easy.admin.module.business.oa.bank.domain.BankQueryForm;
import com.lyusantu.easy.admin.module.business.oa.bank.domain.BankUpdateForm;
import com.lyusantu.easy.admin.module.business.oa.bank.domain.BankVO;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.RequestUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OA办公-OA银行信息
 */
@RequiredArgsConstructor
@RestController
@Tag(name = AdminSwaggerTagConst.Business.OA_BANK)
public class BankController {

    private final BankService bankService;

    @Operation(summary = "分页查询银行信息")
    @PostMapping("/oa/bank/page/query")
    public ResponseDTO<PageResult<BankVO>> queryByPage(@RequestBody @Valid BankQueryForm queryForm) {
        return bankService.queryByPage(queryForm);
    }

    @Operation(summary = "根据企业ID查询银行信息列表")
    @GetMapping("/oa/bank/query/list/{enterpriseId}")
    public ResponseDTO<List<BankVO>> queryList(@PathVariable Long enterpriseId) {
        return bankService.queryList(enterpriseId);
    }

    @Operation(summary = "查询银行信息详情")
    @GetMapping("/oa/bank/get/{bankId}")
    public ResponseDTO<BankVO> getDetail(@PathVariable Long bankId) {
        return bankService.getDetail(bankId);
    }

    @Operation(summary = "新建银行信息")
    @PostMapping("/oa/bank/create")
    public ResponseDTO<String> createBank(@RequestBody @Valid BankCreateForm createVO) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return bankService.createBank(createVO);
    }

    @Operation(summary = "编辑银行信息")
    @PostMapping("/oa/bank/update")
    public ResponseDTO<String> updateBank(@RequestBody @Valid BankUpdateForm updateVO) {
        return bankService.updateBank(updateVO);
    }

    @Operation(summary = "删除银行信息")
    @GetMapping("/oa/bank/delete/{bankId}")
    public ResponseDTO<String> deleteBank(@PathVariable Long bankId) {
        return bankService.deleteBank(bankId);
    }
}
