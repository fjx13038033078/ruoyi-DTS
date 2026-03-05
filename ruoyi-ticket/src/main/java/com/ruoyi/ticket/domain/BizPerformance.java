package com.ruoyi.ticket.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 演出基本信息表 biz_performance
 *
 * @author ruoyi
 */
@Data
public class BizPerformance
{
    private static final long serialVersionUID = 1L;

    /** 演出ID */
    private Long performanceId;

    /** 演出标题 */
    @Excel(name = "演出标题")
    private String title;

    /** 海报图片 */
    @Excel(name = "海报图片")
    private String posterUrl;

    /** 详情描述 */
    private String description;

    /** 是否推荐（0否 1是） */
    @Excel(name = "是否推荐", readConverterExp = "0=否,1=是")
    private String isRecommend;

    /** 上架状态（0下架 1上架） */
    @Excel(name = "上架状态", readConverterExp = "0=下架,1=上架")
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
