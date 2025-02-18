package com.lyusantu.easy.base.module.support.heartbeat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.heartbeat.core.HeartBeatRecord;
import com.lyusantu.easy.base.module.support.heartbeat.domain.HeartBeatRecordEntity;
import com.lyusantu.easy.base.module.support.heartbeat.domain.HeartBeatRecordQueryForm;
import com.lyusantu.easy.base.module.support.heartbeat.domain.HeartBeatRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 心跳记录
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class HeartBeatService {

    private final HeartBeatRecordMapper heartBeatRecordMapper;

    public ResponseDTO<PageResult<HeartBeatRecordVO>> pageQuery(HeartBeatRecordQueryForm pageParam) {
        Page pageQueryInfo = PageUtil.convert2PageQuery(pageParam);
        List<HeartBeatRecordVO> recordVOList = heartBeatRecordMapper.pageQuery(pageQueryInfo, pageParam);
        PageResult<HeartBeatRecordVO> pageResult = PageUtil.convert2PageResult(pageQueryInfo, recordVOList);
        return ResponseDTO.ok(pageResult);
    }

    public void saveOrUpdate(HeartBeatRecord heartBeatRecord) {
        HeartBeatRecordEntity heartBeatRecordEntity = BeanUtil.copy(heartBeatRecord, HeartBeatRecordEntity.class);
        HeartBeatRecordEntity heartBeatRecordOld = heartBeatRecordMapper.query(heartBeatRecordEntity);
        if (heartBeatRecordOld == null) {
            heartBeatRecordMapper.insert(heartBeatRecordEntity);
        } else {
            heartBeatRecordMapper.updateHeartBeatTimeById(heartBeatRecordOld.getHeartBeatRecordId(), heartBeatRecordEntity.getHeartBeatTime());
        }
    }

}
