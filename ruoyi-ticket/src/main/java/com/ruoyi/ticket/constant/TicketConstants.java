package com.ruoyi.ticket.constant;

/**
 * 票务常量（含防黄牛策略配置）
 *
 * @author ruoyi
 */
public class TicketConstants
{
    /**
     * 单笔订单最大购票数量（防黄牛：限制单次购买量）
     */
    public static final int MAX_TICKETS_PER_ORDER = 4;

    /**
     * 同一用户同一场次最大购票总数（防黄牛：限制囤票）
     */
    public static final int MAX_TICKETS_PER_USER_PER_SESSION = 4;

    /**
     * 订单状态：待支付
     */
    public static final String ORDER_STATUS_PENDING = "0";

    /**
     * 订单状态：已支付
     */
    public static final String ORDER_STATUS_PAID = "1";

    /**
     * 票状态：有效
     */
    public static final String TICKET_STATUS_VALID = "1";
}
