package com.ruoyi.ticket.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 购票订单主表 biz_order
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单编号(唯一) */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 购票用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 场次ID */
    @Excel(name = "场次ID")
    private Long sessionId;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private BigDecimal totalAmount;

    /** 状态（0待支付 1已支付 2已取消 3退款处理中 4已退款） */
    @Excel(name = "状态", readConverterExp = "0=待支付,1=已支付,2=已取消,3=退款处理中,4=已退款")
    private String status;

    /** 支付宝/微信交易流水号 */
    private String transactionId;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
}
