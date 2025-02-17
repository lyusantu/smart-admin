package net.lab1024.sa.admin.module.business.oa.invoice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.oa.enterprise.EnterpriseService;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.sa.admin.module.business.oa.invoice.domain.*;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.BeanUtil;
import net.lab1024.sa.base.common.util.PageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerConst;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * OA发票信息
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class InvoiceService {

    private final InvoiceMapper invoiceMapper;

    private final EnterpriseService enterpriseService;

    private final DataTracerService dataTracerService;

    /**
     * 分页查询发票信息
     */
    public ResponseDTO<PageResult<InvoiceVO>> queryByPage(InvoiceQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<InvoiceVO> invoiceList = invoiceMapper.queryPage(page, queryForm);
        PageResult<InvoiceVO> pageResult = PageUtil.convert2PageResult(page, invoiceList);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<List<InvoiceVO>> queryList(Long enterpriseId) {
        InvoiceQueryForm queryForm = new InvoiceQueryForm();
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setDisabledFlag(Boolean.FALSE);
        queryForm.setEnterpriseId(enterpriseId);
        List<InvoiceVO> invoiceList = invoiceMapper.queryPage(null, queryForm);
        return ResponseDTO.ok(invoiceList);
    }

    /**
     * 查询发票信息详情
     */
    public ResponseDTO<InvoiceVO> getDetail(Long invoiceId) {
        // 校验发票信息是否存在
        InvoiceVO invoiceVO = invoiceMapper.getDetail(invoiceId, Boolean.FALSE);
        if (Objects.isNull(invoiceVO)) {
            return ResponseDTO.userErrorParam("发票信息不存在");
        }
        return ResponseDTO.ok(invoiceVO);
    }

    /**
     * 新建发票信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createInvoice(InvoiceAddForm createVO) {
        Long enterpriseId = createVO.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseVO enterpriseVO = enterpriseService.getDetail(enterpriseId);
        if (Objects.isNull(enterpriseVO)) {
            return ResponseDTO.userErrorParam("企业不存在");
        }
        // 验证发票信息账号是否重复
        InvoiceEntity validateInvoice = invoiceMapper.queryByAccountNumber(enterpriseId, createVO.getAccountNumber(), null, Boolean.FALSE);
        if (Objects.nonNull(validateInvoice)) {
            return ResponseDTO.userErrorParam("发票信息账号重复");
        }
        // 数据插入
        InvoiceEntity insertInvoice = BeanUtil.copy(createVO, InvoiceEntity.class);
        invoiceMapper.insert(insertInvoice);
        dataTracerService.addTrace(enterpriseId, DataTracerTypeEnum.OA_ENTERPRISE, "新增发票：" + DataTracerConst.HTML_BR + dataTracerService.getChangeContent(insertInvoice));
        return ResponseDTO.ok();
    }

    /**
     * 编辑发票信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateInvoice(InvoiceUpdateForm updateVO) {
        Long enterpriseId = updateVO.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseVO enterpriseVO = enterpriseService.getDetail(enterpriseId);
        if (Objects.isNull(enterpriseVO)) {
            return ResponseDTO.userErrorParam("企业不存在");
        }
        Long invoiceId = updateVO.getInvoiceId();
        // 校验发票信息是否存在
        InvoiceEntity invoiceDetail = invoiceMapper.selectById(invoiceId);
        if (Objects.isNull(invoiceDetail) || invoiceDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("发票信息不存在");
        }
        // 验证发票信息账号是否重复
        InvoiceEntity validateInvoice = invoiceMapper.queryByAccountNumber(updateVO.getEnterpriseId(), updateVO.getAccountNumber(), invoiceId, Boolean.FALSE);
        if (Objects.nonNull(validateInvoice)) {
            return ResponseDTO.userErrorParam("发票信息账号重复");
        }
        // 数据编辑
        InvoiceEntity updateInvoice = BeanUtil.copy(updateVO, InvoiceEntity.class);
        invoiceMapper.updateById(updateInvoice);
        dataTracerService.addTrace(enterpriseId, DataTracerTypeEnum.OA_ENTERPRISE, "更新发票：" + DataTracerConst.HTML_BR + dataTracerService.getChangeContent(invoiceDetail, updateInvoice));
        return ResponseDTO.ok();
    }


    /**
     * 删除发票信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteInvoice(Long invoiceId) {
        // 校验发票信息是否存在
        InvoiceEntity invoiceDetail = invoiceMapper.selectById(invoiceId);
        if (Objects.isNull(invoiceDetail) || invoiceDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("发票信息不存在");
        }
        invoiceMapper.deleteInvoice(invoiceId, Boolean.TRUE);
        dataTracerService.addTrace(invoiceDetail.getEnterpriseId(), DataTracerTypeEnum.OA_ENTERPRISE, "删除发票：" + DataTracerConst.HTML_BR + dataTracerService.getChangeContent(invoiceDetail));
        return ResponseDTO.ok();
    }
}
