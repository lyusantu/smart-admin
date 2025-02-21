package com.lyusantu.easy.base.module.support.feedback.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.enumeration.UserTypeEnum;
import com.lyusantu.easy.base.common.json.deserializer.FileKeyVoDeserializer;
import com.lyusantu.easy.base.common.json.serializer.FileKeyVoSerializer;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;

import java.time.LocalDateTime;

/**
 * 意见反馈 返回对象
 */
@Data
public class FeedbackVO {

    @Schema(description = "主键")
    private Long feedbackId;

    @Schema(description = "反馈内容")
    private String feedbackContent;

    @Schema(description = "反馈图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String feedbackAttachment;

    @Schema(description = "创建人id")
    private Long userId;

    @Schema(description = "创建人姓名")
    private String userName;

    @SchemaEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer userType;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
