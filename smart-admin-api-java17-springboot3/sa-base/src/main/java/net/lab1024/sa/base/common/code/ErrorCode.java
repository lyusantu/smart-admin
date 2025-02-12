package net.lab1024.sa.base.common.code;

/**
 * 错误码
 * 共三种：系统错误、用户错误、未预期错误
 */
public interface ErrorCode {

    /**
     * 系统等级
     */
    String LEVEL_SYSTEM = "system";

    /**
     * 用户等级
     */
    String LEVEL_USER = "user";

    /**
     * 未预期到的等级
     */
    String LEVEL_UNEXPECTED = "unexpected";

    /**
     * 错误码
     */
    int getCode();

    /**
     * 错误消息
     */
    String getMsg();

    /**
     * 错误等级
     */
    String getLevel();


}
