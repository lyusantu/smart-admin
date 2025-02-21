package com.lyusantu.easy.base.module.support.job.sample;

import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.module.support.config.ConfigMapper;
import com.lyusantu.easy.base.module.support.config.domain.ConfigEntity;
import com.lyusantu.easy.base.module.support.job.core.EasyJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务 示例2
 */
@Slf4j
@Service
public class EasyJobSample2 implements EasyJob {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 定时任务示例
     * 需要事务时 添加 @Transactional 注解
     *
     * @param param 可选参数 任务不需要时不用管
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public String run(String param) {
        // 随便更新点什么东西
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setConfigId(1L);
        configEntity.setRemark(param);
        configMapper.updateById(configEntity);

        configEntity = new ConfigEntity();
        configEntity.setConfigId(2L);
        configEntity.setRemark("Easy Sample2 update");
        configMapper.updateById(configEntity);

        return "执行成功,本次处理数据1条";
    }

}
