package com.ruoyi.ticket.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.ticket.domain.BizOrderTicket;

/**
 * 订单明细 数据层
 *
 * @author ruoyi
 */
public interface BizOrderTicketMapper
{
    /**
     * 查询订单明细
     *
     * @param ticketId 明细ID
     * @return 订单明细
     */
    BizOrderTicket selectBizOrderTicketById(Long ticketId);

    /**
     * 查询订单明细列表
     *
     * @param bizOrderTicket 订单明细
     * @return 订单明细集合
     */
    List<BizOrderTicket> selectBizOrderTicketList(BizOrderTicket bizOrderTicket);

    /**
     * 新增订单明细
     *
     * @param bizOrderTicket 订单明细
     * @return 结果
     */
    int insertBizOrderTicket(BizOrderTicket bizOrderTicket);

    /**
     * 修改订单明细
     *
     * @param bizOrderTicket 订单明细
     * @return 结果
     */
    int updateBizOrderTicket(BizOrderTicket bizOrderTicket);

    /**
     * 删除订单明细
     *
     * @param ticketId 明细ID
     * @return 结果
     */
    int deleteBizOrderTicketById(Long ticketId);

    /**
     * 批量删除订单明细
     *
     * @param ticketIds 需要删除的明细ID
     * @return 结果
     */
    int deleteBizOrderTicketByIds(Long[] ticketIds);

    /**
     * 根据座位ID列表查询关联的订单ID（去重）
     */
    List<Long> selectOrderIdsBySeatIds(@Param("seatIds") List<Long> seatIds);

    /**
     * 批量插入订单明细
     */
    int batchInsert(@Param("list") List<BizOrderTicket> list);
}
