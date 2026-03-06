package com.ruoyi.ticket.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ruoyi.ticket.domain.BizOrder;
import com.ruoyi.ticket.domain.BizOrderTicket;
import com.ruoyi.ticket.domain.BizSessionSeat;
import com.ruoyi.ticket.service.IBizOrderService;
import com.ruoyi.ticket.service.IBizOrderTicketService;
import com.ruoyi.ticket.service.IBizSessionSeatService;
import com.ruoyi.ticket.service.ITicketStatsService;

/**
 * 票务统计 服务层实现
 *
 * @author ruoyi
 */
@Service
public class TicketStatsServiceImpl implements ITicketStatsService {

    private final IBizOrderService bizOrderService;
    private final IBizOrderTicketService bizOrderTicketService;
    private final IBizSessionSeatService bizSessionSeatService;

    public TicketStatsServiceImpl(IBizOrderService bizOrderService,
                                  IBizOrderTicketService bizOrderTicketService,
                                  IBizSessionSeatService bizSessionSeatService) {
        this.bizOrderService = bizOrderService;
        this.bizOrderTicketService = bizOrderTicketService;
        this.bizSessionSeatService = bizSessionSeatService;
    }

    @Override
    public Map<String, Object> getSummary() {
        BizOrder orderQuery = new BizOrder();
        orderQuery.setStatus("1");
        List<BizOrder> paidOrders = bizOrderService.selectBizOrderList(orderQuery);
        int ticketCount = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (BizOrder o : paidOrders) {
            BizOrderTicket tq = new BizOrderTicket();
            tq.setOrderId(o.getOrderId());
            tq.setStatus("1");
            ticketCount += bizOrderTicketService.selectBizOrderTicketList(tq).size();
            totalAmount = totalAmount.add(o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO);
        }
        BizSessionSeat seatQuery = new BizSessionSeat();
        List<BizSessionSeat> allSeats = bizSessionSeatService.selectBizSessionSeatList(seatQuery);
        int totalSeats = allSeats.size();
        seatQuery.setStatus("2");
        int soldSeats = bizSessionSeatService.selectBizSessionSeatList(seatQuery).size();
        double occupancy = totalSeats > 0 ? (soldSeats * 100.0 / totalSeats) : 0;
        Map<String, Object> result = new HashMap<>();
        result.put("ticketCount", ticketCount);
        result.put("orderCount", paidOrders.size());
        result.put("totalAmount", totalAmount);
        result.put("totalSeats", totalSeats);
        result.put("soldSeats", soldSeats);
        result.put("occupancyRate", Math.round(occupancy * 100) / 100.0);
        return result;
    }

    @Override
    public Map<String, Object> getSalesStats(String period) {
        BizOrder query = new BizOrder();
        query.setStatus("1");
        Calendar cal = Calendar.getInstance();
        if ("day".equals(period)) {
            cal.add(Calendar.DAY_OF_MONTH, -7);
        } else if ("week".equals(period)) {
            cal.add(Calendar.WEEK_OF_YEAR, -4);
        } else {
            cal.add(Calendar.MONTH, -6);
        }
        String beginTime = String.format("%tF", cal);
        query.getParams().put("beginTime", beginTime);
        List<BizOrder> orders = bizOrderService.selectBizOrderList(query);
        Map<String, Map<String, Object>> dayMap = new HashMap<>();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
        for (BizOrder o : orders) {
            if (o.getCreateTime() == null) continue;
            String key;
            if ("day".equals(period)) {
                key = sdfDay.format(o.getCreateTime());
            } else if ("week".equals(period)) {
                cal.setTime(o.getCreateTime());
                key = cal.get(Calendar.YEAR) + "-W" + cal.get(Calendar.WEEK_OF_YEAR);
            } else {
                key = sdfMonth.format(o.getCreateTime());
            }
            dayMap.computeIfAbsent(key, k -> {
                Map<String, Object> m = new HashMap<>();
                m.put("count", 0);
                m.put("amount", BigDecimal.ZERO);
                return m;
            });
            Map<String, Object> m = dayMap.get(key);
            m.put("count", (Integer) m.get("count") + 1);
            m.put("amount", ((BigDecimal) m.get("amount")).add(o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO));
        }
        List<Map<String, Object>> list = dayMap.entrySet().stream()
                .map(e -> {
                    Map<String, Object> m = new HashMap<>(e.getValue());
                    m.put("date", e.getKey());
                    return m;
                })
                .sorted(Comparator.comparing(m -> (String) m.get("date")))
                .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", orders.size());
        result.put("totalAmount", orders.stream().map(BizOrder::getTotalAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        return result;
    }

    @Override
    public List<Map<String, Object>> getAreaStats() {
        BizOrderTicket ticketQuery = new BizOrderTicket();
        ticketQuery.setStatus("1");
        List<BizOrderTicket> tickets = bizOrderTicketService.selectBizOrderTicketList(ticketQuery);
        Map<String, Integer> areaCount = new HashMap<>();
        for (BizOrderTicket t : tickets) {
            BizSessionSeat seat = bizSessionSeatService.selectBizSessionSeatById(t.getSeatId());
            String area = seat != null && seat.getAreaName() != null ? seat.getAreaName() : "其他";
            areaCount.merge(area, 1, (a, b) -> a + b);
        }
        return areaCount.entrySet().stream()
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", e.getKey());
                    m.put("value", e.getValue());
                    return m;
                })
                .sorted((a, b) -> ((Integer) b.get("value")).compareTo((Integer) a.get("value")))
                .collect(Collectors.toList());
    }
}
