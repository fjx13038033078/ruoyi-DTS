package com.ruoyi.ticket.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ticket.mapper.BizSessionSeatMapper;
import com.ruoyi.ticket.domain.BizSessionSeat;
import com.ruoyi.ticket.service.IBizSessionSeatService;

/**
 * 场次座位与票价 服务层实现
 *
 * @author ruoyi
 */
@Service
public class BizSessionSeatServiceImpl implements IBizSessionSeatService
{
    @Autowired
    private BizSessionSeatMapper bizSessionSeatMapper;

    /**
     * 查询场次座位
     *
     * @param seatId 场次座位ID
     * @return 场次座位
     */
    @Override
    public BizSessionSeat selectBizSessionSeatById(Long seatId)
    {
        return bizSessionSeatMapper.selectBizSessionSeatById(seatId);
    }

    /**
     * 查询场次座位列表
     *
     * @param bizSessionSeat 场次座位
     * @return 场次座位
     */
    @Override
    public List<BizSessionSeat> selectBizSessionSeatList(BizSessionSeat bizSessionSeat)
    {
        return bizSessionSeatMapper.selectBizSessionSeatList(bizSessionSeat);
    }

    /**
     * 新增场次座位
     *
     * @param bizSessionSeat 场次座位
     * @return 结果
     */
    @Override
    public int insertBizSessionSeat(BizSessionSeat bizSessionSeat)
    {
        return bizSessionSeatMapper.insertBizSessionSeat(bizSessionSeat);
    }

    /**
     * 修改场次座位
     *
     * @param bizSessionSeat 场次座位
     * @return 结果
     */
    @Override
    public int updateBizSessionSeat(BizSessionSeat bizSessionSeat)
    {
        return bizSessionSeatMapper.updateBizSessionSeat(bizSessionSeat);
    }

    /**
     * 批量删除场次座位
     *
     * @param seatIds 需要删除的场次座位ID
     * @return 结果
     */
    @Override
    public int deleteBizSessionSeatByIds(Long[] seatIds)
    {
        return bizSessionSeatMapper.deleteBizSessionSeatByIds(seatIds);
    }

    /**
     * 删除场次座位
     *
     * @param seatId 场次座位ID
     * @return 结果
     */
    @Override
    public int deleteBizSessionSeatById(Long seatId)
    {
        return bizSessionSeatMapper.deleteBizSessionSeatById(seatId);
    }
}
