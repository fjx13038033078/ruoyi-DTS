package com.ruoyi.ticket.mapper;

import java.util.List;
import com.ruoyi.ticket.domain.BizPerformance;

/**
 * 演出基本信息 数据层
 *
 * @author ruoyi
 */
public interface BizPerformanceMapper
{
    /**
     * 查询演出基本信息
     *
     * @param performanceId 演出ID
     * @return 演出基本信息
     */
    BizPerformance selectBizPerformanceById(Long performanceId);

    /**
     * 查询演出基本信息列表
     *
     * @param bizPerformance 演出基本信息
     * @return 演出基本信息集合
     */
    List<BizPerformance> selectBizPerformanceList(BizPerformance bizPerformance);

    /**
     * 前台：查询上架演出列表，按是否推荐排序
     *
     * @param bizPerformance 演出基本信息
     * @return 演出基本信息集合
     */
    List<BizPerformance> selectBizPerformanceListForFront(BizPerformance bizPerformance);

    /**
     * 新增演出基本信息
     *
     * @param bizPerformance 演出基本信息
     * @return 结果
     */
    int insertBizPerformance(BizPerformance bizPerformance);

    /**
     * 修改演出基本信息
     *
     * @param bizPerformance 演出基本信息
     * @return 结果
     */
    int updateBizPerformance(BizPerformance bizPerformance);

    /**
     * 删除演出基本信息
     *
     * @param performanceId 演出ID
     * @return 结果
     */
    int deleteBizPerformanceById(Long performanceId);

    /**
     * 批量删除演出基本信息
     *
     * @param performanceIds 需要删除的演出ID
     * @return 结果
     */
    int deleteBizPerformanceByIds(Long[] performanceIds);
}
