package com.ruoyi.ticket.service;

import java.util.List;
import com.ruoyi.ticket.domain.BizOrder;

/**
 * 选座下单 服务层
 *
 * @author ruoyi
 */
public interface IOrderPlacementService {

    /**
     * 选座下单：校验座位、锁定、生成订单及明细
     *
     * @param userId    用户ID
     * @param sessionId 场次ID
     * @param seatIds   座位ID列表
     * @return 生成的订单
     */
    BizOrder placeOrder(Long userId, Long sessionId, List<Long> seatIds);
}
