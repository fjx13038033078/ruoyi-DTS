package com.ruoyi.ticket.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ticket.mapper.BizPointsItemMapper;
import com.ruoyi.ticket.domain.BizPointsItem;
import com.ruoyi.ticket.service.IBizPointsItemService;

/**
 * 积分兑换商品 服务层实现
 *
 * @author ruoyi
 */
@Service
public class BizPointsItemServiceImpl implements IBizPointsItemService
{
    @Autowired
    private BizPointsItemMapper bizPointsItemMapper;

    /**
     * 查询积分兑换商品
     *
     * @param itemId 商品ID
     * @return 积分兑换商品
     */
    @Override
    public BizPointsItem selectBizPointsItemById(Long itemId)
    {
        return bizPointsItemMapper.selectBizPointsItemById(itemId);
    }

    /**
     * 查询积分兑换商品列表
     *
     * @param bizPointsItem 积分兑换商品
     * @return 积分兑换商品
     */
    @Override
    public List<BizPointsItem> selectBizPointsItemList(BizPointsItem bizPointsItem)
    {
        return bizPointsItemMapper.selectBizPointsItemList(bizPointsItem);
    }

    /**
     * 新增积分兑换商品
     *
     * @param bizPointsItem 积分兑换商品
     * @return 结果
     */
    @Override
    public int insertBizPointsItem(BizPointsItem bizPointsItem)
    {
        return bizPointsItemMapper.insertBizPointsItem(bizPointsItem);
    }

    /**
     * 修改积分兑换商品
     *
     * @param bizPointsItem 积分兑换商品
     * @return 结果
     */
    @Override
    public int updateBizPointsItem(BizPointsItem bizPointsItem)
    {
        return bizPointsItemMapper.updateBizPointsItem(bizPointsItem);
    }

    /**
     * 批量删除积分兑换商品
     *
     * @param itemIds 需要删除的商品ID
     * @return 结果
     */
    @Override
    public int deleteBizPointsItemByIds(Long[] itemIds)
    {
        return bizPointsItemMapper.deleteBizPointsItemByIds(itemIds);
    }

    /**
     * 删除积分兑换商品
     *
     * @param itemId 商品ID
     * @return 结果
     */
    @Override
    public int deleteBizPointsItemById(Long itemId)
    {
        return bizPointsItemMapper.deleteBizPointsItemById(itemId);
    }
}
