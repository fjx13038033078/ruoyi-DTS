package com.ruoyi.ticket.service;

import java.util.List;
import com.ruoyi.ticket.domain.BizOrder;

/**
 * 购票订单 服务层
 *
 * @author ruoyi
 */
public interface IBizOrderService
{
    /**
     * 查询购票订单
     *
     * @param orderId 订单ID
     * @return 购票订单
     */
    BizOrder selectBizOrderById(Long orderId);

    /**
     * 查询购票订单列表
     *
     * @param bizOrder 购票订单
     * @return 购票订单集合
     */
    List<BizOrder> selectBizOrderList(BizOrder bizOrder);

    /**
     * 新增购票订单
     *
     * @param bizOrder 购票订单
     * @return 结果
     */
    int insertBizOrder(BizOrder bizOrder);

    /**
     * 修改购票订单
     *
     * @param bizOrder 购票订单
     * @return 结果
     */
    int updateBizOrder(BizOrder bizOrder);

    /**
     * 批量删除购票订单
     *
     * @param orderIds 需要删除的订单ID
     * @return 结果
     */
    int deleteBizOrderByIds(Long[] orderIds);

    /**
     * 删除购票订单
     *
     * @param orderId 订单ID
     * @return 结果
     */
    int deleteBizOrderById(Long orderId);
}
