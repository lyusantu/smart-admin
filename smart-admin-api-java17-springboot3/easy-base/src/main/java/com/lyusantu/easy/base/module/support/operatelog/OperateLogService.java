package com.lyusantu.easy.base.module.support.operatelog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.operatelog.domain.OperateLogEntity;
import com.lyusantu.easy.base.module.support.operatelog.domain.OperateLogQueryForm;
import com.lyusantu.easy.base.module.support.operatelog.domain.OperateLogVO;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志
 */
@RequiredArgsConstructor
@Service
public class OperateLogService {

    private final OperateLogMapper operateLogMapper;

    /**
     * @author 罗伊
     * @description 分页查询
     */
    public ResponseDTO<PageResult<OperateLogVO>> queryByPage(OperateLogQueryForm queryForm) {
        Page page = PageUtil.convert2PageQuery(queryForm);
        List<OperateLogEntity> logEntityList = operateLogMapper.queryByPage(page, queryForm);
        PageResult<OperateLogVO> pageResult = PageUtil.convert2PageResult(page, logEntityList, OperateLogVO.class);
        return ResponseDTO.ok(pageResult);
    }


    /**
     * 查询详情
     *
     * @param operateLogId
     * @return
     */
    public ResponseDTO<OperateLogVO> detail(Long operateLogId) {
        OperateLogEntity operateLogEntity = operateLogMapper.selectById(operateLogId);
        if (operateLogEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        OperateLogVO operateLogVO = BeanUtil.copy(operateLogEntity, OperateLogVO.class);
        return ResponseDTO.ok(operateLogVO);
    }
}
