package com.lyusantu.easy.base.module.support.feedback.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.lyusantu.easy.base.common.json.deserializer.FileKeyVoDeserializer;
import com.lyusantu.easy.base.common.json.serializer.FileKeyVoSerializer;

/**
 * 意见反馈 添加表单
 */
@Data
public class FeedbackAddForm {

    @Schema(description = "反馈内容")
    @NotBlank(message = "反馈内容不能为空")
    private String feedbackContent;

    @Schema(description = "反馈图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String feedbackAttachment;

}
