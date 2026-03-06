package com.ruoyi.ticket.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.ticket.domain.BizPointsItem;
import com.ruoyi.ticket.domain.BizPointsRecord;
import com.ruoyi.ticket.mapper.BizPointsItemMapper;
import com.ruoyi.ticket.service.IBizPointsItemService;
import com.ruoyi.ticket.service.IBizPointsRecordService;

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

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IBizPointsRecordService bizPointsRecordService;

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

    /**
     * 积分兑换商品
     * 1. 校验用户是否存在及积分是否充足
     * 2. 校验商品是否存在、上架、库存
     * 3. 扣减用户积分、商品库存，插入兑换流水
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exchangeItem(Long userId, Long itemId)
    {
        // 1. 校验用户是否存在，获取当前积分
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 2. 校验商品是否存在、是否上架、库存是否充足
        BizPointsItem item = bizPointsItemMapper.selectBizPointsItemById(itemId);
        if (item == null) {
            throw new ServiceException("兑换商品不存在");
        }
        if (!"1".equals(item.getStatus())) {
            throw new ServiceException("商品已下架");
        }
        if (item.getStock() == null || item.getStock() <= 0) {
            throw new ServiceException("库存不足");
        }

        // 3. 校验所需积分
        Integer pointsRequired = item.getPointsRequired();
        if (pointsRequired == null || pointsRequired <= 0) {
            throw new ServiceException("商品配置异常");
        }
        if (user.getPoints() == null || user.getPoints() < pointsRequired) {
            throw new ServiceException("积分不足");
        }

        // 4. 扣减用户积分
        if (!sysUserService.deductPoints(userId, pointsRequired)) {
            throw new ServiceException("积分扣减失败，请重试");
        }

        // 5. 扣减商品库存
        item.setStock(item.getStock() - 1);
        bizPointsItemMapper.updateBizPointsItem(item);

        // 6. 插入兑换流水
        BizPointsRecord record = new BizPointsRecord();
        record.setUserId(userId);
        record.setItemId(itemId);
        record.setPointsDeducted(pointsRequired);
        bizPointsRecordService.insertBizPointsRecord(record);
    }
}
