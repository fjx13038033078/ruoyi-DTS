package com.ruoyi.ticket.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ticket.mapper.BizOrderMapper;
import com.ruoyi.ticket.domain.BizOrder;
import com.ruoyi.ticket.service.IBizOrderService;

/**
 * 购票订单 服务层实现
 *
 * @author ruoyi
 */
@Service
public class BizOrderServiceImpl implements IBizOrderService
{
    @Autowired
    private BizOrderMapper bizOrderMapper;

    /**
     * 查询购票订单
     *
     * @param orderId 订单ID
     * @return 购票订单
     */
    @Override
    public BizOrder selectBizOrderById(Long orderId)
    {
        return bizOrderMapper.selectBizOrderById(orderId);
    }

    /**
     * 查询购票订单列表
     *
     * @param bizOrder 购票订单
     * @return 购票订单
     */
    @Override
    public List<BizOrder> selectBizOrderList(BizOrder bizOrder)
    {
        return bizOrderMapper.selectBizOrderList(bizOrder);
    }

    /**
     * 新增购票订单
     *
     * @param bizOrder 购票订单
     * @return 结果
     */
    @Override
    public int insertBizOrder(BizOrder bizOrder)
    {
        return bizOrderMapper.insertBizOrder(bizOrder);
    }

    /**
     * 修改购票订单
     *
     * @param bizOrder 购票订单
     * @return 结果
     */
    @Override
    public int updateBizOrder(BizOrder bizOrder)
    {
        return bizOrderMapper.updateBizOrder(bizOrder);
    }

    /**
     * 批量删除购票订单
     *
     * @param orderIds 需要删除的订单ID
     * @return 结果
     */
    @Override
    public int deleteBizOrderByIds(Long[] orderIds)
    {
        return bizOrderMapper.deleteBizOrderByIds(orderIds);
    }

    /**
     * 删除购票订单
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public int deleteBizOrderById(Long orderId)
    {
        return bizOrderMapper.deleteBizOrderById(orderId);
    }
}
