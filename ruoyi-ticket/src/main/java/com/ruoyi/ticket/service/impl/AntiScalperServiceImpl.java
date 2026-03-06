package com.ruoyi.ticket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.ticket.constant.TicketConstants;
import com.ruoyi.ticket.domain.BizOrder;
import com.ruoyi.ticket.mapper.BizOrderMapper;
import com.ruoyi.ticket.mapper.BizOrderTicketMapper;
import com.ruoyi.ticket.service.IAntiScalperService;
import com.ruoyi.ticket.domain.BizOrderTicket;

/**
 * 防黄牛策略 服务层实现
 * <p>
 * 策略说明：
 * 1. 单笔订单最多购买 N 张票（限制单次囤票）
 * 2. 同一用户同一场次累计最多购买 N 张票（限制多笔订单囤票）
 * </p>
 *
 * @author ruoyi
 */
@Service
public class AntiScalperServiceImpl implements IAntiScalperService
{
    @Autowired
    private BizOrderMapper bizOrderMapper;

    @Autowired
    private BizOrderTicketMapper bizOrderTicketMapper;

    @Override
    public void validateBeforeAddTicket(BizOrderTicket orderTicket)
    {
        if (orderTicket == null || orderTicket.getOrderId() == null)
        {
            return;
        }
        BizOrder order = bizOrderMapper.selectBizOrderById(orderTicket.getOrderId());
        if (order == null || order.getUserId() == null || order.getSessionId() == null)
        {
            return;
        }

        int orderTicketCount = bizOrderTicketMapper.countValidTicketsByOrderId(order.getOrderId());
        if (orderTicketCount >= TicketConstants.MAX_TICKETS_PER_ORDER)
        {
            throw new ServiceException("单笔订单最多购买" + TicketConstants.MAX_TICKETS_PER_ORDER + "张票，请勿囤票");
        }

        int userSessionTicketCount = bizOrderTicketMapper.countUserValidTicketsForSession(order.getUserId(), order.getSessionId());
        if (userSessionTicketCount >= TicketConstants.MAX_TICKETS_PER_USER_PER_SESSION)
        {
            throw new ServiceException("您在该场次已购票" + userSessionTicketCount + "张，每人每场最多" + TicketConstants.MAX_TICKETS_PER_USER_PER_SESSION + "张");
        }
    }
}
