package com.lyusantu.easy.base.module.support.message.controller;

import com.lyusantu.easy.base.module.support.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.message.domain.MessageQueryForm;
import com.lyusantu.easy.base.module.support.message.domain.MessageVO;
import org.springframework.web.bind.annotation.*;

/**
 * 消息
 */
@RequiredArgsConstructor
@RestController
@Tag(name = SwaggerTagConst.Support.MESSAGE)
public class MessageController extends SupportBaseController {

    private final MessageService messageService;

    @Operation(summary = "分页查询我的消息")
    @PostMapping("/message/queryMyMessage")
    public ResponseDTO<PageResult<MessageVO>> query(@RequestBody @Valid MessageQueryForm queryForm) {
        RequestUser user = RequestUtil.getRequestUser();
        if(user == null){
            return ResponseDTO.userErrorParam("用户未登录");
        }

        queryForm.setSearchCount(false);
        queryForm.setReceiverUserId(user.getUserId());
        queryForm.setReceiverUserType(user.getUserType().getValue());
        return ResponseDTO.ok(messageService.query(queryForm));
    }

    @Operation(summary = "查询未读消息数量")
    @GetMapping("/message/getUnreadCount")
    public ResponseDTO<Long> getUnreadCount() {
        RequestUser user = RequestUtil.getRequestUser();
        if(user == null){
            return ResponseDTO.userErrorParam("用户未登录");
        }
        return ResponseDTO.ok(messageService.getUnreadCount(user.getUserType(), user.getUserId()));
    }

    @Operation(summary = "更新已读")
    @GetMapping("/message/read/{messageId}")
    public ResponseDTO<String> updateReadFlag(@PathVariable Long messageId) {
        RequestUser user = RequestUtil.getRequestUser();
        if(user == null){
            return ResponseDTO.userErrorParam("用户未登录");
        }

        messageService.updateReadFlag(messageId, user.getUserType(), user.getUserId());
        return ResponseDTO.ok();
    }

}
