package com.ruoyi.ticket.mapper;

import java.util.List;
import com.ruoyi.ticket.domain.BizPointsItem;

/**
 * 积分兑换商品 数据层
 *
 * @author ruoyi
 */
public interface BizPointsItemMapper
{
    /**
     * 查询积分兑换商品
     *
     * @param itemId 商品ID
     * @return 积分兑换商品
     */
    BizPointsItem selectBizPointsItemById(Long itemId);

    /**
     * 查询积分兑换商品列表
     *
     * @param bizPointsItem 积分兑换商品
     * @return 积分兑换商品集合
     */
    List<BizPointsItem> selectBizPointsItemList(BizPointsItem bizPointsItem);

    /**
     * 新增积分兑换商品
     *
     * @param bizPointsItem 积分兑换商品
     * @return 结果
     */
    int insertBizPointsItem(BizPointsItem bizPointsItem);

    /**
     * 修改积分兑换商品
     *
     * @param bizPointsItem 积分兑换商品
     * @return 结果
     */
    int updateBizPointsItem(BizPointsItem bizPointsItem);

    /**
     * 删除积分兑换商品
     *
     * @param itemId 商品ID
     * @return 结果
     */
    int deleteBizPointsItemById(Long itemId);

    /**
     * 批量删除积分兑换商品
     *
     * @param itemIds 需要删除的商品ID
     * @return 结果
     */
    int deleteBizPointsItemByIds(Long[] itemIds);
}
