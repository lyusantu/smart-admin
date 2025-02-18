package com.lyusantu.easy.base.module.support.codegenerator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;

/**
 * 前端组件类型
 */
@Getter
@AllArgsConstructor
public enum CodeFrontComponentEnum implements BaseEnum {

    INPUT("Input", "输入框"),
    INPUT_NUMBER("InputNumber", "数字输入框"),
    TEXTAREA("Textarea", " 文本"),
    BOOLEAN_SELECT("BooleanSelect", "布尔下拉框"),
    ENUM_SELECT("SmartEnumSelect", "枚举下拉"),
    DICT_SELECT("DictSelect", "字典下拉"),
    DATE("Date", "日期选择"),
    DATE_TIME("DateTime", "时间选择"),
    FILE_UPLOAD("FileUpload", "文件上传");

    private final String value;

    private final String desc;

}
