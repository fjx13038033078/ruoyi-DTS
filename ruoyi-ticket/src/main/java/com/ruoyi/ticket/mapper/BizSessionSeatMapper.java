package com.ruoyi.ticket.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 根据ID列表查询座位
     */
    List<BizSessionSeat> selectByIds(@Param("seatIds") List<Long> seatIds);

    /**
     * 查询超时锁定的座位（status=1 且 lock_expire_time < 当前时间）
     */
    List<BizSessionSeat> selectExpiredLockedSeats();

    /**
     * 批量将座位释放为待售（status=0, lock_expire_time=null）
     */
    int releaseSeatsByIds(@Param("seatIds") List<Long> seatIds);

    /**
     * 批量锁定座位（status=1, lock_expire_time=指定时间）
     */
    int lockSeatsByIds(@Param("seatIds") List<Long> seatIds, @Param("lockExpireTime") Date lockExpireTime);
}
