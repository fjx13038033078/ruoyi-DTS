package com.ruoyi.ticket.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ticket.mapper.BizOrderTicketMapper;
import com.ruoyi.ticket.domain.BizOrderTicket;
import com.ruoyi.ticket.service.IAntiScalperService;
import com.ruoyi.ticket.service.IBizOrderTicketService;

/**
 * 订单明细 服务层实现
 *
 * @author ruoyi
 */
@Service
public class BizOrderTicketServiceImpl implements IBizOrderTicketService
{
    @Autowired
    private BizOrderTicketMapper bizOrderTicketMapper;

    @Autowired
    private IAntiScalperService antiScalperService;

    /**
     * 查询订单明细
     *
     * @param ticketId 明细ID
     * @return 订单明细
     */
    @Override
    public BizOrderTicket selectBizOrderTicketById(Long ticketId)
    {
        return bizOrderTicketMapper.selectBizOrderTicketById(ticketId);
    }

    /**
     * 查询订单明细列表
     *
     * @param bizOrderTicket 订单明细
     * @return 订单明细
     */
    @Override
    public List<BizOrderTicket> selectBizOrderTicketList(BizOrderTicket bizOrderTicket)
    {
        return bizOrderTicketMapper.selectBizOrderTicketList(bizOrderTicket);
    }

    /**
     * 新增订单明细
     *
     * @param bizOrderTicket 订单明细
     * @return 结果
     */
    @Override
    public int insertBizOrderTicket(BizOrderTicket bizOrderTicket)
    {
        antiScalperService.validateBeforeAddTicket(bizOrderTicket);
        return bizOrderTicketMapper.insertBizOrderTicket(bizOrderTicket);
    }

    /**
     * 修改订单明细
     *
     * @param bizOrderTicket 订单明细
     * @return 结果
     */
    @Override
    public int updateBizOrderTicket(BizOrderTicket bizOrderTicket)
    {
        return bizOrderTicketMapper.updateBizOrderTicket(bizOrderTicket);
    }

    /**
     * 批量删除订单明细
     *
     * @param ticketIds 需要删除的明细ID
     * @return 结果
     */
    @Override
    public int deleteBizOrderTicketByIds(Long[] ticketIds)
    {
        return bizOrderTicketMapper.deleteBizOrderTicketByIds(ticketIds);
    }

    /**
     * 删除订单明细
     *
     * @param ticketId 明细ID
     * @return 结果
     */
    @Override
    public int deleteBizOrderTicketById(Long ticketId)
    {
        return bizOrderTicketMapper.deleteBizOrderTicketById(ticketId);
    }
}
