package com.ruoyi.ticket.service;

import java.util.List;
import java.util.Map;

/**
 * 票务统计 服务层
 *
 * @author ruoyi
 */
public interface ITicketStatsService {

    /**
     * 概览统计（售票量、销售额、上座率、订单数）
     *
     * @return 统计结果
     */
    Map<String, Object> getSummary();

    /**
     * 售票统计（日/周/月）
     *
     * @param period day|week|month
     * @return 按时间维度的销售列表及汇总
     */
    Map<String, Object> getSalesStats(String period);

    /**
     * 热门座位区域统计
     *
     * @return 区域名称及售票数量列表
     */
    List<Map<String, Object>> getAreaStats();
}
