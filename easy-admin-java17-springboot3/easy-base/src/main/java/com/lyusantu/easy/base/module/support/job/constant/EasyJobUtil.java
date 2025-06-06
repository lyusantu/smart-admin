package com.lyusantu.easy.base.module.support.job.constant;

import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.module.support.job.core.EasyJob;
import org.springframework.scheduling.support.CronExpression;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EasyJob Util
 */
public class EasyJobUtil {

    private EasyJobUtil() {
    }

    /**
     * 校验cron表达式 是否合法
     *
     * @param cron
     * @return
     */
    public static boolean checkCron(String cron) {
        return CronExpression.isValidExpression(cron);
    }

    /**
     * 校验固定间隔 值是否合法
     *
     * @param val
     * @return
     */
    public static boolean checkFixedDelay(String val) {
        int intVal;
        try {
            intVal = Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return false;
        }
        return intVal > 0;
    }

    /**
     * 打印一些展示信息到控制台
     * 环保绿
     *
     * @param info
     */
    public static void printInfo(String info) {
        System.out.printf("\033[32;1m %s \033[0m", info);
    }

    /**
     * 查询未来N次执行时间 从最后一次时间时间 开始计算
     *
     * @param triggerType
     * @param triggerVal
     * @param lastExecuteTime
     * @param num
     * @return
     */
    public static List<LocalDateTime> queryNextTimeFromLast(String triggerType,
                                                            String triggerVal,
                                                            LocalDateTime lastExecuteTime,
                                                            int num) {
        List<LocalDateTime> nextTimeList = null;
        if (EasyJobTriggerTypeEnum.CRON.equalsValue(triggerType)) {
            nextTimeList = EasyJobUtil.queryNextTime(triggerVal, lastExecuteTime, num);
        } else if (EasyJobTriggerTypeEnum.FIXED_DELAY.equalsValue(triggerType)) {
            nextTimeList = EasyJobUtil.queryNextTime(getFixedDelayVal(triggerVal), lastExecuteTime, num);
        }
        return nextTimeList;
    }

    /**
     * 查询未来N次执行时间 从当前时间 开始计算
     *
     * @param triggerType
     * @param triggerVal
     * @param lastExecuteTime
     * @param num
     * @return
     */
    public static List<LocalDateTime> queryNextTimeFromNow(String triggerType,
                                                           String triggerVal,
                                                           LocalDateTime lastExecuteTime,
                                                           int num) {
        LocalDateTime nowTime = LocalDateTime.now();
        List<LocalDateTime> nextTimeList = null;
        if (EasyJobTriggerTypeEnum.CRON.equalsValue(triggerType)) {
            nextTimeList = EasyJobUtil.queryNextTime(triggerVal, nowTime, num);
        } else if (EasyJobTriggerTypeEnum.FIXED_DELAY.equalsValue(triggerType)) {
            Integer fixedDelay = getFixedDelayVal(triggerVal);
            LocalDateTime startTime = null == lastExecuteTime || lastExecuteTime.plusSeconds(fixedDelay).isBefore(nowTime)
                    ? nowTime : lastExecuteTime;
            nextTimeList = EasyJobUtil.queryNextTime(fixedDelay, startTime, num);
        }
        return nextTimeList;
    }

    /**
     * 根据cron表达式 计算N次执行时间
     *
     * @param cron
     * @param startTime
     * @param num
     * @return
     */
    public static List<LocalDateTime> queryNextTime(String cron, LocalDateTime startTime, int num) {
        if (null == startTime) {
            return Collections.emptyList();
        }
        CronExpression parse = CronExpression.parse(cron);
        List<LocalDateTime> timeList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            startTime = parse.next(startTime);
            timeList.add(startTime);
        }
        return timeList;
    }

    /**
     * 根据 固定间隔 计算N次执行时间
     *
     * @param fixDelaySecond
     * @param startTime
     * @param num
     * @return
     */
    public static List<LocalDateTime> queryNextTime(Integer fixDelaySecond, LocalDateTime startTime, int num) {
        if (null == startTime) {
            return Collections.emptyList();
        }
        List<LocalDateTime> timeList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            startTime = startTime.plusSeconds(fixDelaySecond);
            timeList.add(startTime);
        }
        return timeList;
    }

    /**
     * 获取固定间隔时间
     *
     * @param val
     * @return
     */
    public static Integer getFixedDelayVal(String val) {
        return Integer.parseInt(val);
    }

    /**
     * 获取当前 Java 应用程序的工作目录
     *
     * @return
     */
    public static String getProgramPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取当前 Java 应用程序的进程id
     *
     * @return
     */
    public static String getProcessId() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        return runtime.getName().split("@")[0];
    }


    /**
     * 根据className 判断job class
     */
    public static ResponseDTO<String> checkJobClass(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            // 判断是否实现了 EasyJob
            if (!EasyJob.class.isAssignableFrom(aClass)) {
                return ResponseDTO.userErrorParam(className + " 执行类没有实现 EastJob 接口");
            }
        } catch (ClassNotFoundException e) {
            return ResponseDTO.userErrorParam("没有在代码中发现执行类：" + className);
        }
        return ResponseDTO.ok();
    }


    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.now();
        List<LocalDateTime> timeList = EasyJobUtil.queryNextTime("5 * * * * *", startTime, 3);
        System.out.println(timeList);

        timeList = EasyJobUtil.queryNextTime(10, startTime, 3);
        System.out.println(timeList);

        System.out.println("project path ->" + getProgramPath());
        System.out.println("project process id ->" + getProcessId());
        ResponseDTO<String> res = checkJobClass("com.lyusantu.easy.base.module.support.job.sample.EasyJobSample1");
        System.out.println(res.getMsg());
    }
}
