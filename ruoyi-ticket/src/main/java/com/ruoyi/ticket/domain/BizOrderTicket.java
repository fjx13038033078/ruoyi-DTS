package com.ruoyi.ticket.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 订单明细表 biz_order_ticket
 *
 * @author ruoyi
 */
@Data
public class BizOrderTicket
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long ticketId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 关联的场次座位ID */
    @Excel(name = "座位ID")
    private Long seatId;

    /** 购票时单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 单张票状态（1有效 2已退票） */
    @Excel(name = "票状态", readConverterExp = "1=有效,2=已退票")
    private String status;
}
