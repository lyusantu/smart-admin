package com.lyusantu.easy.base.module.support.mail;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyusantu.easy.base.module.support.mail.domain.MailTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 邮件模板
 */
@Mapper
@Component
public interface MailTemplateMapper extends BaseMapper<MailTemplateEntity> {

}
