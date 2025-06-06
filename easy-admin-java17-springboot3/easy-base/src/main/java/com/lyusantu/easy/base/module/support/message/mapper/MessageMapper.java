package com.lyusantu.easy.base.module.support.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.message.domain.MessageEntity;
import com.lyusantu.easy.base.module.support.message.domain.MessageVO;
import com.lyusantu.easy.base.module.support.message.domain.MessageQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息 接受者类型枚举
 */
@Component
@Mapper
public interface MessageMapper extends BaseMapper<MessageEntity> {

    /**
     * 分页查询消息
     *
     */
    List<MessageVO> query(Page<?> page, @Param("query") MessageQueryForm queryForm);

    /**
     * 更新已读状态
     */
    Integer updateReadFlag(@Param("messageId") Long messageId,
                           @Param("receiverUserType") Integer receiverUserType,
                           @Param("receiverUserId") Long receiverUserId,
                           @Param("readFlag") Boolean readFlag);

    /**
     * 查询未读消息数
     */
    Long getUnreadCount( @Param("receiverUserType") Integer receiverUserType,
                         @Param("receiverUserId") Long receiverUserId);



}
