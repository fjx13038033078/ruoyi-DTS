package com.ruoyi.ticket.service;

import com.ruoyi.ticket.domain.BizOrderTicket;

/**
 * 防黄牛策略 服务层
 *
 * @author ruoyi
 */
public interface IAntiScalperService
{
    /**
     * 校验购票是否违反防黄牛规则（在新增订单票前调用）
     *
     * @param orderTicket 待新增的订单票（需包含 orderId、seatId）
     * @throws com.ruoyi.common.exception.ServiceException 若违反规则
     */
    void validateBeforeAddTicket(BizOrderTicket orderTicket);
}
