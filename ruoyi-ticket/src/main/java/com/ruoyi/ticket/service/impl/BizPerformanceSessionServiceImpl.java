package com.ruoyi.ticket.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ticket.domain.BizSessionSeat;
import com.ruoyi.ticket.mapper.BizPerformanceSessionMapper;
import com.ruoyi.ticket.mapper.BizSessionSeatMapper;
import com.ruoyi.ticket.domain.BizPerformanceSession;
import com.ruoyi.ticket.service.IBizPerformanceSessionService;

/**
 * 演出场次 服务层实现
 *
 * @author ruoyi
 */
@Service
public class BizPerformanceSessionServiceImpl implements IBizPerformanceSessionService
{
    @Autowired
    private BizPerformanceSessionMapper bizPerformanceSessionMapper;

    @Autowired
    private BizSessionSeatMapper bizSessionSeatMapper;

    /**
     * 批量生成场次座位
     *
     * @param sessionId 场次ID
     * @param areaName  区域名称
     * @param price     票价
     * @param rows      排数
     * @param cols      每排座位数
     * @return 生成的座位数量
     */
    @Override
    public int batchGenerateSeats(Long sessionId, String areaName, BigDecimal price, Integer rows, Integer cols)
    {
        int count = 0;
        int rowCount = rows != null && rows > 0 ? rows : 10;
        int colCount = cols != null && cols > 0 ? cols : 20;
        for (int row = 1; row <= rowCount; row++)
        {
            for (int col = 1; col <= colCount; col++)
            {
                BizSessionSeat seat = new BizSessionSeat();
                seat.setSessionId(sessionId);
                seat.setAreaName(areaName);
                seat.setSeatName(row + "排" + col + "座");
                seat.setPrice(price);
                seat.setStatus("0");
                bizSessionSeatMapper.insertBizSessionSeat(seat);
                count++;
            }
        }
        return count;
    }

    /**
     * 查询演出场次
     *
     * @param sessionId 场次ID
     * @return 演出场次
     */
    @Override
    public BizPerformanceSession selectBizPerformanceSessionById(Long sessionId)
    {
        return bizPerformanceSessionMapper.selectBizPerformanceSessionById(sessionId);
    }

    /**
     * 查询演出场次列表
     *
     * @param bizPerformanceSession 演出场次
     * @return 演出场次
     */
    @Override
    public List<BizPerformanceSession> selectBizPerformanceSessionList(BizPerformanceSession bizPerformanceSession)
    {
        return bizPerformanceSessionMapper.selectBizPerformanceSessionList(bizPerformanceSession);
    }

    /**
     * 新增演出场次
     *
     * @param bizPerformanceSession 演出场次
     * @return 结果
     */
    @Override
    public int insertBizPerformanceSession(BizPerformanceSession bizPerformanceSession)
    {
        return bizPerformanceSessionMapper.insertBizPerformanceSession(bizPerformanceSession);
    }

    /**
     * 修改演出场次
     *
     * @param bizPerformanceSession 演出场次
     * @return 结果
     */
    @Override
    public int updateBizPerformanceSession(BizPerformanceSession bizPerformanceSession)
    {
        return bizPerformanceSessionMapper.updateBizPerformanceSession(bizPerformanceSession);
    }

    /**
     * 批量删除演出场次
     *
     * @param sessionIds 需要删除的场次ID
     * @return 结果
     */
    @Override
    public int deleteBizPerformanceSessionByIds(Long[] sessionIds)
    {
        return bizPerformanceSessionMapper.deleteBizPerformanceSessionByIds(sessionIds);
    }

    /**
     * 删除演出场次
     *
     * @param sessionId 场次ID
     * @return 结果
     */
    @Override
    public int deleteBizPerformanceSessionById(Long sessionId)
    {
        return bizPerformanceSessionMapper.deleteBizPerformanceSessionById(sessionId);
    }
}
