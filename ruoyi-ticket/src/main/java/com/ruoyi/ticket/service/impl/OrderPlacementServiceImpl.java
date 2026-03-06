package com.ruoyi.ticket.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.ticket.constant.TicketConstants;
import com.ruoyi.ticket.domain.BizOrder;
import com.ruoyi.ticket.domain.BizOrderTicket;
import com.ruoyi.ticket.domain.BizSessionSeat;
import com.ruoyi.ticket.mapper.BizOrderMapper;
import com.ruoyi.ticket.mapper.BizOrderTicketMapper;
import com.ruoyi.ticket.mapper.BizSessionSeatMapper;
import com.ruoyi.ticket.service.IOrderPlacementService;

/**
 * 选座下单 服务层实现
 *
 * @author ruoyi
 */
@Service
public class OrderPlacementServiceImpl implements IOrderPlacementService {

    private static final int LOCK_MINUTES = 15;

    private final BizSessionSeatMapper bizSessionSeatMapper;
    private final BizOrderMapper bizOrderMapper;
    private final BizOrderTicketMapper bizOrderTicketMapper;

    public OrderPlacementServiceImpl(BizSessionSeatMapper bizSessionSeatMapper,
                                     BizOrderMapper bizOrderMapper,
                                     BizOrderTicketMapper bizOrderTicketMapper) {
        this.bizSessionSeatMapper = bizSessionSeatMapper;
        this.bizOrderMapper = bizOrderMapper;
        this.bizOrderTicketMapper = bizOrderTicketMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizOrder placeOrder(Long userId, Long sessionId, List<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("请选择座位");
        }
        if (seatIds.size() > TicketConstants.MAX_TICKETS_PER_ORDER) {
            throw new ServiceException("单笔订单最多购买" + TicketConstants.MAX_TICKETS_PER_ORDER + "张票");
        }
        int userSessionCount = bizOrderTicketMapper.countUserValidTicketsForSession(userId, sessionId);
        if (userSessionCount + seatIds.size() > TicketConstants.MAX_TICKETS_PER_USER_PER_SESSION) {
            throw new ServiceException("您在该场次已购" + userSessionCount + "张，每人每场最多" + TicketConstants.MAX_TICKETS_PER_USER_PER_SESSION + "张");
        }
        List<BizSessionSeat> seats = bizSessionSeatMapper.selectByIds(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("部分座位不存在");
        }
        for (BizSessionSeat seat : seats) {
            if (!seat.getSessionId().equals(sessionId)) {
                throw new IllegalArgumentException("座位与场次不匹配");
            }
            if (!"0".equals(seat.getStatus())) {
                throw new IllegalArgumentException("座位[" + seat.getSeatName() + "]已被占用，请重新选座");
            }
        }
        Date lockExpireTime = addMinutes(new Date(), LOCK_MINUTES);
        int locked = bizSessionSeatMapper.lockSeatsByIds(seatIds, lockExpireTime);
        if (locked != seatIds.size()) {
            throw new IllegalStateException("座位锁定失败，请重新选座");
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (BizSessionSeat seat : seats) {
            totalAmount = totalAmount.add(seat.getPrice() != null ? seat.getPrice() : BigDecimal.ZERO);
        }
        BizOrder order = new BizOrder();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setSessionId(sessionId);
        order.setTotalAmount(totalAmount);
        order.setStatus("0");
        bizOrderMapper.insertBizOrder(order);
        List<BizOrderTicket> tickets = new ArrayList<>(seats.size());
        for (BizSessionSeat seat : seats) {
            BizOrderTicket ticket = new BizOrderTicket();
            ticket.setOrderId(order.getOrderId());
            ticket.setSeatId(seat.getSeatId());
            ticket.setPrice(seat.getPrice());
            ticket.setStatus("1");
            tickets.add(ticket);
        }
        bizOrderTicketMapper.batchInsert(tickets);
        return order;
    }

    private Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
