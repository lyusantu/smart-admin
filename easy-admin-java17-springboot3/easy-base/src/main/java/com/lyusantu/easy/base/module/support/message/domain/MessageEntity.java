package com.lyusantu.easy.base.module.support.message.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lyusantu.easy.base.common.enumeration.UserTypeEnum;
import com.lyusantu.easy.base.module.support.message.constant.MessageTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息实体
 */
@Data
@TableName("t_message")
public class MessageEntity {

    @TableId(type = IdType.AUTO)
    private Long messageId;

    /**
     * 消息类型
     *
     * @see MessageTypeEnum
     */
    private Integer messageType;
    /**
     * 接收者类型
     *
     * @see UserTypeEnum
     */
    private Integer receiverUserType;

    /**
     * 接收者id
     */
    private Long receiverUserId;

    /**
     * 相关业务id
     */
    private String dataId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Boolean readFlag;

    /**
     * 已读时间
     */
    private LocalDateTime readTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
