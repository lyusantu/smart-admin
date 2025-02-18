package com.lyusantu.easy.base.module.support.repeatsubmit.ticket;

import java.util.function.Function;

/**
 * 凭证（用于校验重复提交的东西）
 */
public abstract class AbstractRepeatSubmitTicket {

    private Function<String, String> ticketFunction;


    public AbstractRepeatSubmitTicket(Function<String, String> ticketFunction) {
        this.ticketFunction = ticketFunction;
    }


    /**
     * 获取凭证
     *
     * @param ticketToken
     * @return
     */
    public String getTicket(String ticketToken) {
        return this.ticketFunction.apply(ticketToken);
    }

    /**
     * 获取凭证 时间戳
     *
     * @param ticket
     * @return
     */
    public abstract Long getTicketTimestamp(String ticket);


    /**
     * 设置本次请求时间
     *
     * @param ticket
     */
    public abstract void putTicket(String ticket);

    /**
     * 移除凭证
     *
     * @param ticket
     */
    public abstract void removeTicket(String ticket);
}
