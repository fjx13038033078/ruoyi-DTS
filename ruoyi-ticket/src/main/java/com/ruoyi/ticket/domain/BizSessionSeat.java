package com.ruoyi.ticket.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 场次座位与票价表 biz_session_seat
 *
 * @author ruoyi
 */
@Data
public class BizSessionSeat
{
    private static final long serialVersionUID = 1L;

    /** 场次座位ID */
    private Long seatId;

    /** 场次ID */
    @Excel(name = "场次ID")
    private Long sessionId;

    /** 区域名称(如A区、VIP区) */
    @Excel(name = "区域名称")
    private String areaName;

    /** 座位号(如: 5排12座) */
    @Excel(name = "座位号")
    private String seatName;

    /** 该座位票价 */
    @Excel(name = "票价")
    private BigDecimal price;

    /** 售卖状态（0待售 1锁定中 2已售出） */
    @Excel(name = "售卖状态", readConverterExp = "0=待售,1=锁定中,2=已售出")
    private String status;

    /** 锁定过期时间(配合定时任务释放) */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lockExpireTime;
}
