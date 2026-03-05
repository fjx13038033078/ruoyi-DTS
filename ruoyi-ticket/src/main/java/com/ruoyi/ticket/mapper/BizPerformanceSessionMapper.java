package com.ruoyi.ticket.mapper;

import java.util.List;
import com.ruoyi.ticket.domain.BizPerformanceSession;

/**
 * 演出场次 数据层
 *
 * @author ruoyi
 */
public interface BizPerformanceSessionMapper
{
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
     * 删除演出场次
     *
     * @param sessionId 场次ID
     * @return 结果
     */
    int deleteBizPerformanceSessionById(Long sessionId);

    /**
     * 批量删除演出场次
     *
     * @param sessionIds 需要删除的场次ID
     * @return 结果
     */
    int deleteBizPerformanceSessionByIds(Long[] sessionIds);
}
