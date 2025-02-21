package com.lyusantu.easy.base.common.code;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 错误码注册
 * 为了能统一、清晰、浏览当前定义的所有状态码，方便后续维护，此处手动注册
 */
public class ErrorCodeRegister {

    /**
     * 统计数量
     */
    static int errorCounter;

    /**
     * 所有的错误码均大于10000
     */
    static final int MIN_START_CODE;

    static final Map<Class<? extends ErrorCode>, ImmutablePair<Integer, Integer>> CODE_RANGE_MAP;

    static {
        // 初始化参数
        errorCounter = 0;
        MIN_START_CODE = 10000;
        CODE_RANGE_MAP = new ConcurrentHashMap<>();

        // 系统错误码
        register(SystemErrorCode.class, 10001, 20000);
        // 意外错误码
        register(UnexpectedErrorCode.class, 20001, 30000);
        // 用户通用错误码
        register(UserErrorCode.class, 30001, 40000);
    }


    public static int initialize() {
        return errorCounter;
    }

    /**
     * 注册状态码
     * 校验是否重复、是否越界
     */
    static void register(Class<? extends ErrorCode> clazz, int start, int end) {
        String simpleName = getSimpleName(clazz, start, end);
        // 校验 开始结束值 是否越界
        CODE_RANGE_MAP.forEach((k, v) -> {
            if (isExistOtherRange(start, end, v)) {
                throw new IllegalArgumentException(String.format("<<ErrorCodeRangeValidator>> error: %s[%d,%d] has intersection with class:%s[%d,%d]", simpleName, start, end,
                        k.getSimpleName(), v.getLeft(), v.getRight()));
            }
        });

        // 循环校验code并存储
        List<Integer> codeList = Stream.of(clazz.getEnumConstants()).map(codeEnum -> {
            int code = codeEnum.getCode();
            if (code < start || code > end) {
                throw new IllegalArgumentException(String.format("<<ErrorCodeRangeValidator>> error: %s[%d,%d] code %d out of range", simpleName, start, end, code));
            }
            return code;
        }).collect(Collectors.toList());

        // 校验code是否重复
        List<Integer> distinctCodeList = codeList.stream().distinct().collect(Collectors.toList());
        Collection<Integer> subtract = CollectionUtils.subtract(codeList, distinctCodeList);
        if (CollectionUtils.isNotEmpty(subtract)) {
            throw new IllegalArgumentException(String.format("<<ErrorCodeRangeValidator>> error: %s code %s is repeat!", simpleName, subtract));
        }

        CODE_RANGE_MAP.put(clazz, ImmutablePair.of(start, end));
        // 统计
        errorCounter = errorCounter + distinctCodeList.size();
    }

    private static String getSimpleName(Class<? extends ErrorCode> clazz, int start, int end) {
        String simpleName = clazz.getSimpleName();
        if (!clazz.isEnum()) {
            throw new ExceptionInInitializerError(String.format("<<ErrorCodeRangeValidator>> error: %s not Enum class !", simpleName));
        }
        if (start > end) {
            throw new ExceptionInInitializerError(String.format("<<ErrorCodeRangeValidator>> error: %s start must be less than the end !", simpleName));
        }

        if (start <= MIN_START_CODE) {
            throw new ExceptionInInitializerError(String.format("<<ErrorCodeRangeValidator>> error: %s start must be more than %s !", simpleName, MIN_START_CODE));
        }
        // 校验是否重复注册
        boolean containsKey = CODE_RANGE_MAP.containsKey(clazz);
        if (containsKey) {
            throw new ExceptionInInitializerError(String.format("<<ErrorCodeRangeValidator>> error: Enum %s already exist !", simpleName));
        }
        return simpleName;
    }

    /**
     * 是否存在于其他范围
     */
    private static boolean isExistOtherRange(int start, int end, ImmutablePair<Integer, Integer> range) {
        if (start >= range.getLeft() && start <= range.getRight()) {
            return true;
        }
        return end >= range.getLeft() && end <= range.getRight();
    }

}
