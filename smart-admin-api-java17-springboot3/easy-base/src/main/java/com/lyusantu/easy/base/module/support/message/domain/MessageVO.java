package com.lyusantu.easy.base.module.support.message.domain;

import com.lyusantu.easy.base.module.support.message.constant.MessageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.enumeration.UserTypeEnum;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;

import java.time.LocalDateTime;

/**
 * 消息
 */
@Data
public class MessageVO {

    private Long messageId;

    @SchemaEnum(value = MessageTypeEnum.class)
    private Integer messageType;

    @SchemaEnum(value = UserTypeEnum.class)
    private Integer receiverUserType;

    @Schema(description = "接收者id")
    private Long receiverUserId;

    @Schema(description = "相关业务id")
    private String dataId;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "是否已读")
    private Boolean readFlag;

    @Schema(description = "已读时间")
    private LocalDateTime readTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
