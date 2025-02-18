package com.lyusantu.easy.base.module.support.datatracer.domain.form;

import com.lyusantu.easy.base.module.support.datatracer.constant.DataTracerTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据变动表单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataTracerForm {

    /**
     * 业务id
     */
    private Long dataId;

    /**
     * 业务类型
     */
    private DataTracerTypeEnum type;

    /**
     * 操作内容
     */
    private String content;

    /**
     * diff 差异：旧的数据
     */
    private String diffOld;

    /**
     * 差异：新的数据
     */
    private String diffNew;

    /**
     * 扩展字段
     */
    private String extraData;

}
