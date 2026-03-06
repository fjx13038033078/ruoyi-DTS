package com.ruoyi.ticket.task;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ruoyi.ticket.domain.BizSessionSeat;
import com.ruoyi.ticket.mapper.BizOrderMapper;
import com.ruoyi.ticket.mapper.BizOrderTicketMapper;
import com.ruoyi.ticket.mapper.BizSessionSeatMapper;

/**
 * 座位锁定超时释放定时任务
 *
 * @author ruoyi
 */
@Component
public class SeatLockTask {

    private static final Logger log = LoggerFactory.getLogger(SeatLockTask.class);

    private final BizSessionSeatMapper bizSessionSeatMapper;
    private final BizOrderTicketMapper bizOrderTicketMapper;
    private final BizOrderMapper bizOrderMapper;

    public SeatLockTask(BizSessionSeatMapper bizSessionSeatMapper,
                        BizOrderTicketMapper bizOrderTicketMapper,
                        BizOrderMapper bizOrderMapper) {
        this.bizSessionSeatMapper = bizSessionSeatMapper;
        this.bizOrderTicketMapper = bizOrderTicketMapper;
        this.bizOrderMapper = bizOrderMapper;
    }

    /**
     * 每分钟执行：释放超时锁定的座位，并将对应未支付订单改为已取消
     */
    @Scheduled(cron = "0 * * * * ?")
    public void releaseExpiredSeats() {
        List<BizSessionSeat> expiredSeats = bizSessionSeatMapper.selectExpiredLockedSeats();
        if (expiredSeats.isEmpty()) {
            return;
        }
        List<Long> seatIds = expiredSeats.stream()
                .map(BizSessionSeat::getSeatId)
                .collect(Collectors.toList());
        List<Long> orderIds = bizOrderTicketMapper.selectOrderIdsBySeatIds(seatIds);
        if (!orderIds.isEmpty()) {
            int cancelled = bizOrderMapper.updateStatusToCancelledByIds(orderIds);
            log.info("座位锁定超时释放：取消未支付订单 {} 个", cancelled);
        }
        int released = bizSessionSeatMapper.releaseSeatsByIds(seatIds);
        log.info("座位锁定超时释放：释放座位 {} 个", released);
    }
}
