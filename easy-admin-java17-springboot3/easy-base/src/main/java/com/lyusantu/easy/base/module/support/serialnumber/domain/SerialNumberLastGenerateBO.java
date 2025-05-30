package com.lyusantu.easy.base.module.support.serialnumber.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 上次生成信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerialNumberLastGenerateBO {

    /**
     * 序号id
     */
    private Integer serialNumberId;

    /**
     * 上次生成的数字
     */
    private Long lastNumber;

    /**
     * 上次生成的时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastTime;


}
