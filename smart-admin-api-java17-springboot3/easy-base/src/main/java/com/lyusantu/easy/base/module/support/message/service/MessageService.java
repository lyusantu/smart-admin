package com.lyusantu.easy.base.module.support.message.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lyusantu.easy.base.module.support.message.domain.*;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.enumeration.UserTypeEnum;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.module.support.message.constant.MessageTemplateEnum;
import com.lyusantu.easy.base.module.support.message.mapper.MessageMapper;
import com.lyusantu.easy.base.module.support.message.domain.*;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageMapper messageMapper;

    private final MessageManager messageManager;

    /**
     * 分页查询 消息
     */
    public PageResult<MessageVO> query(MessageQueryForm queryForm) {
        Page page = PageUtil.convert2PageQuery(queryForm);
        List<MessageVO> messageVOList = messageMapper.query(page, queryForm);
        return PageUtil.convert2PageResult(page, messageVOList);
    }

    /**
     * 查询未读消息数量
     */
    public Long getUnreadCount(UserTypeEnum userType, Long userId) {
        return messageMapper.getUnreadCount(userType.getValue(), userId);
    }

    /**
     * 更新已读状态
     */
    public void updateReadFlag(Long messageId, UserTypeEnum userType, Long receiverUserId) {
        messageMapper.updateReadFlag(messageId, userType.getValue(), receiverUserId, true);
    }


    /**
     * 发送【模板消息】
     */
    public void sendTemplateMessage(MessageTemplateSendForm... sendTemplateForms) {
        List<MessageSendForm> sendFormList = Lists.newArrayList();
        for (MessageTemplateSendForm sendTemplateForm : sendTemplateForms) {
            MessageTemplateEnum msgTemplateTypeEnum = sendTemplateForm.getMessageTemplateEnum();
            StringSubstitutor stringSubstitutor = new StringSubstitutor(sendTemplateForm.getContentParam());
            String content = stringSubstitutor.replace(msgTemplateTypeEnum.getContent());

            MessageSendForm messageSendForm = new MessageSendForm();
            messageSendForm.setMessageType(msgTemplateTypeEnum.getMessageTypeEnum().getValue());
            messageSendForm.setReceiverUserType(sendTemplateForm.getReceiverUserType().getValue());
            messageSendForm.setReceiverUserId(sendTemplateForm.getReceiverUserId());
            messageSendForm.setTitle(msgTemplateTypeEnum.getDesc());
            messageSendForm.setContent(content);
            messageSendForm.setDataId(sendTemplateForm.getDataId());
            sendFormList.add(messageSendForm);

        }
        this.sendMessage(sendFormList);
    }

    /**
     * 发送消息
     */
    public void sendMessage(MessageSendForm... sendForms) {
        this.sendMessage(Lists.newArrayList(sendForms));
    }

    /**
     * 批量发送通知消息
     */
    public void sendMessage(List<MessageSendForm> sendList) {
        for (MessageSendForm sendDTO : sendList) {
            String verify = BeanUtil.verify(sendDTO);
            if (null != verify) {
                throw new RuntimeException("send msg error: " + verify);
            }
        }
        List<MessageEntity> messageEntityList = sendList.stream().map(e -> {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setMessageType(e.getMessageType());
            messageEntity.setReceiverUserType(e.getReceiverUserType());
            messageEntity.setReceiverUserId(e.getReceiverUserId());
            messageEntity.setDataId(String.valueOf(e.getDataId()));
            messageEntity.setTitle(e.getTitle());
            messageEntity.setContent(e.getContent());
            return messageEntity;
        }).collect(Collectors.toList());
        messageManager.saveBatch(messageEntityList);
    }
}
