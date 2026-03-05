package com.ruoyi.ticket.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 演出场次表 biz_performance_session
 *
 * @author ruoyi
 */
@Data
public class BizPerformanceSession
{
    private static final long serialVersionUID = 1L;

    /** 场次ID */
    private Long sessionId;

    /** 关联演出ID */
    @Excel(name = "演出ID")
    private Long performanceId;

    /** 演出场馆 */
    @Excel(name = "演出场馆")
    private String venueName;

    /** 演出开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "演出开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 售票开启时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "售票开启时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date saleStartTime;

    /** 状态（0未开始 1售票中 2已结束） */
    @Excel(name = "状态", readConverterExp = "0=未开始,1=售票中,2=已结束")
    private String status;
}
