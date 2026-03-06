package com.ruoyi.ticket.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ticket.mapper.BizPerformanceMapper;
import com.ruoyi.ticket.domain.BizPerformance;
import com.ruoyi.ticket.service.IBizPerformanceService;

/**
 * 演出基本信息 服务层实现
 *
 * @author ruoyi
 */
@Service
public class BizPerformanceServiceImpl implements IBizPerformanceService
{
    @Autowired
    private BizPerformanceMapper bizPerformanceMapper;

    /**
     * 查询演出基本信息
     *
     * @param performanceId 演出ID
     * @return 演出基本信息
     */
    @Override
    public BizPerformance selectBizPerformanceById(Long performanceId)
    {
        return bizPerformanceMapper.selectBizPerformanceById(performanceId);
    }

    /**
     * 查询演出基本信息列表
     *
     * @param bizPerformance 演出基本信息
     * @return 演出基本信息
     */
    @Override
    public List<BizPerformance> selectBizPerformanceList(BizPerformance bizPerformance)
    {
        return bizPerformanceMapper.selectBizPerformanceList(bizPerformance);
    }

    @Override
    public List<BizPerformance> selectBizPerformanceListForFront(BizPerformance bizPerformance)
    {
        return bizPerformanceMapper.selectBizPerformanceListForFront(bizPerformance);
    }

    /**
     * 新增演出基本信息
     *
     * @param bizPerformance 演出基本信息
     * @return 结果
     */
    @Override
    public int insertBizPerformance(BizPerformance bizPerformance)
    {
        return bizPerformanceMapper.insertBizPerformance(bizPerformance);
    }

    /**
     * 修改演出基本信息
     *
     * @param bizPerformance 演出基本信息
     * @return 结果
     */
    @Override
    public int updateBizPerformance(BizPerformance bizPerformance)
    {
        return bizPerformanceMapper.updateBizPerformance(bizPerformance);
    }

    /**
     * 批量删除演出基本信息
     *
     * @param performanceIds 需要删除的演出ID
     * @return 结果
     */
    @Override
    public int deleteBizPerformanceByIds(Long[] performanceIds)
    {
        return bizPerformanceMapper.deleteBizPerformanceByIds(performanceIds);
    }

    /**
     * 删除演出基本信息
     *
     * @param performanceId 演出ID
     * @return 结果
     */
    @Override
    public int deleteBizPerformanceById(Long performanceId)
    {
        return bizPerformanceMapper.deleteBizPerformanceById(performanceId);
    }
}
