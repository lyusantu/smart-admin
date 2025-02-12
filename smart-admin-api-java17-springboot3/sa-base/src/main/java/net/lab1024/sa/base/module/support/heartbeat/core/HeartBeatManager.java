package net.lab1024.sa.base.module.support.heartbeat.core;

import net.lab1024.sa.base.module.support.heartbeat.HeartBeatService;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 心跳核心调度管理器
 */
public class HeartBeatManager {

    private static final String THREAD_NAME_PREFIX = "smart-heart-beat";
    private static final int THREAD_COUNT = 1;
    private static final long INITIAL_DELAY = 60 * 1000L;

    private final ScheduledThreadPoolExecutor threadPoolExecutor;

    /**
     * 服务状态持久化处理类
     */
    private final HeartBeatService heartBeatService;

    /**
     * 调度配置信息
     */
    private final long intervalMilliseconds;

    /**
     * @param intervalMilliseconds 间隔执行时间(毫秒)
     */
    public HeartBeatManager(Long intervalMilliseconds, HeartBeatService heartBeatService) {
        this.intervalMilliseconds = intervalMilliseconds;
        this.heartBeatService = heartBeatService;
        //使用守护线程去处理
        this.threadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_COUNT, r -> {
            Thread t = new Thread(r, THREAD_NAME_PREFIX);
            if (!t.isDaemon()) {
                t.setDaemon(true);
            }
            return t;
        });
        // 开始心跳
        this.beginHeartBeat();
    }

    /**
     * 开启心跳
     */
    private void beginHeartBeat() {
        HeartBeatRunnable heartBeatRunnable = new HeartBeatRunnable(heartBeatService);
        threadPoolExecutor.scheduleWithFixedDelay(heartBeatRunnable, INITIAL_DELAY, intervalMilliseconds, TimeUnit.MILLISECONDS);
    }

}
