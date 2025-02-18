package com.lyusantu.easy.base.module.support.job.sample;

import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.module.support.job.core.EasyJob;
import org.springframework.stereotype.Service;

/**
 * 定时任务 示例1
 */
@Slf4j
@Service
public class EasyJobSample1 implements EasyJob {

    /**
     * 定时任务示例
     *
     * @param param 可选参数 任务不需要时不用管
     * @return
     */
    @Override
    public String run(String param) {
        // 写点什么业务逻辑
        return "执行完毕,随便说点什么吧";
    }

}
