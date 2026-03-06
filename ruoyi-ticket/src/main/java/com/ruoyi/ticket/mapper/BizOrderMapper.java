package com.ruoyi.ticket.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.ticket.domain.BizOrder;

/**
 * 购票订单 数据层
 *
 * @author ruoyi
 */
public interface BizOrderMapper
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
     * 删除购票订单
     *
     * @param orderId 订单ID
     * @return 结果
     */
    int deleteBizOrderById(Long orderId);

    /**
     * 批量删除购票订单
     *
     * @param orderIds 需要删除的订单ID
     * @return 结果
     */
    int deleteBizOrderByIds(Long[] orderIds);

    /**
     * 将指定订单状态更新为已取消
     */
    int updateStatusToCancelledByIds(@Param("orderIds") List<Long> orderIds);
}
