package com.ruoyi.ticket.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.ticket.domain.BizPerformanceSession;

/**
 * 演出场次 服务层
 *
 * @author ruoyi
 */
public interface IBizPerformanceSessionService
{
    /**
     * 批量生成场次座位
     *
     * @param sessionId  场次ID
     * @param areaName   区域名称
     * @param price      票价
     * @param rows       排数
     * @param cols       每排座位数
     * @return 生成的座位数量
     */
    int batchGenerateSeats(Long sessionId, String areaName, BigDecimal price, Integer rows, Integer cols);
    /**
     * 查询演出场次
     *
     * @param sessionId 场次ID
     * @return 演出场次
     */
    BizPerformanceSession selectBizPerformanceSessionById(Long sessionId);

    /**
     * 查询演出场次列表
     *
     * @param bizPerformanceSession 演出场次
     * @return 演出场次集合
     */
    List<BizPerformanceSession> selectBizPerformanceSessionList(BizPerformanceSession bizPerformanceSession);

    /**
     * 新增演出场次
     *
     * @param bizPerformanceSession 演出场次
     * @return 结果
     */
    int insertBizPerformanceSession(BizPerformanceSession bizPerformanceSession);

    /**
     * 修改演出场次
     *
     * @param bizPerformanceSession 演出场次
     * @return 结果
     */
    int updateBizPerformanceSession(BizPerformanceSession bizPerformanceSession);

    /**
     * 批量删除演出场次
     *
     * @param sessionIds 需要删除的场次ID
     * @return 结果
     */
    int deleteBizPerformanceSessionByIds(Long[] sessionIds);

    /**
     * 删除演出场次
     *
     * @param sessionId 场次ID
     * @return 结果
     */
    int deleteBizPerformanceSessionById(Long sessionId);
}
