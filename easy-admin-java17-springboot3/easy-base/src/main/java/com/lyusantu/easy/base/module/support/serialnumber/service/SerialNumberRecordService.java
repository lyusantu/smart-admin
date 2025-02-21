package com.lyusantu.easy.base.module.support.serialnumber.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.serialnumber.domain.SerialNumberRecordEntity;
import com.lyusantu.easy.base.module.support.serialnumber.mapper.SerialNumberRecordMapper;
import jakarta.annotation.Resource;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.module.support.serialnumber.domain.SerialNumberRecordQueryForm;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单据序列号 记录
 */
@Service
public class SerialNumberRecordService {

    @Resource
    private SerialNumberRecordMapper serialNumberRecordDao;

    public PageResult<SerialNumberRecordEntity> query(SerialNumberRecordQueryForm queryForm) {
        Page page = PageUtil.convert2PageQuery(queryForm);
        List<SerialNumberRecordEntity> recordList = serialNumberRecordDao.query(page, queryForm);
        return PageUtil.convert2PageResult(page, recordList);
    }
}
