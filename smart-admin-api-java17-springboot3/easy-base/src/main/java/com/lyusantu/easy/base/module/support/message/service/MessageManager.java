package com.lyusantu.easy.base.module.support.message.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyusantu.easy.base.module.support.message.domain.MessageEntity;
import com.lyusantu.easy.base.module.support.message.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 * 消息manager
 */
@Service
public class MessageManager extends ServiceImpl<MessageMapper, MessageEntity> {

}
