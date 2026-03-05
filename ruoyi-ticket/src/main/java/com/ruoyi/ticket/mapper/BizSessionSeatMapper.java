package com.ruoyi.ticket.mapper;

import java.util.List;
import com.ruoyi.ticket.domain.BizSessionSeat;

/**
 * 场次座位与票价 数据层
 *
 * @author ruoyi
 */
public interface BizSessionSeatMapper
{
    /**
     * 查询场次座位
     *
     * @param seatId 场次座位ID
     * @return 场次座位
     */
    BizSessionSeat selectBizSessionSeatById(Long seatId);

    /**
     * 查询场次座位列表
     *
     * @param bizSessionSeat 场次座位
     * @return 场次座位集合
     */
    List<BizSessionSeat> selectBizSessionSeatList(BizSessionSeat bizSessionSeat);

    /**
     * 新增场次座位
     *
     * @param bizSessionSeat 场次座位
     * @return 结果
     */
    int insertBizSessionSeat(BizSessionSeat bizSessionSeat);

    /**
     * 修改场次座位
     *
     * @param bizSessionSeat 场次座位
     * @return 结果
     */
    int updateBizSessionSeat(BizSessionSeat bizSessionSeat);

    /**
     * 删除场次座位
     *
     * @param seatId 场次座位ID
     * @return 结果
     */
    int deleteBizSessionSeatById(Long seatId);

    /**
     * 批量删除场次座位
     *
     * @param seatIds 需要删除的场次座位ID
     * @return 结果
     */
    int deleteBizSessionSeatByIds(Long[] seatIds);
}
