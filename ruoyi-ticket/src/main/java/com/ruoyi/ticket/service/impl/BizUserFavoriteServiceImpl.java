package com.ruoyi.ticket.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ticket.mapper.BizUserFavoriteMapper;
import com.ruoyi.ticket.domain.BizUserFavorite;
import com.ruoyi.ticket.service.IBizUserFavoriteService;

/**
 * 剧目收藏 服务层实现
 *
 * @author ruoyi
 */
@Service
public class BizUserFavoriteServiceImpl implements IBizUserFavoriteService
{
    @Autowired
    private BizUserFavoriteMapper bizUserFavoriteMapper;

    /**
     * 查询剧目收藏
     *
     * @param favoriteId 收藏ID
     * @return 剧目收藏
     */
    @Override
    public BizUserFavorite selectBizUserFavoriteById(Long favoriteId)
    {
        return bizUserFavoriteMapper.selectBizUserFavoriteById(favoriteId);
    }

    /**
     * 查询剧目收藏列表
     *
     * @param bizUserFavorite 剧目收藏
     * @return 剧目收藏
     */
    @Override
    public List<BizUserFavorite> selectBizUserFavoriteList(BizUserFavorite bizUserFavorite)
    {
        return bizUserFavoriteMapper.selectBizUserFavoriteList(bizUserFavorite);
    }

    /**
     * 新增剧目收藏
     *
     * @param bizUserFavorite 剧目收藏
     * @return 结果
     */
    @Override
    public int insertBizUserFavorite(BizUserFavorite bizUserFavorite)
    {
        return bizUserFavoriteMapper.insertBizUserFavorite(bizUserFavorite);
    }

    /**
     * 修改剧目收藏
     *
     * @param bizUserFavorite 剧目收藏
     * @return 结果
     */
    @Override
    public int updateBizUserFavorite(BizUserFavorite bizUserFavorite)
    {
        return bizUserFavoriteMapper.updateBizUserFavorite(bizUserFavorite);
    }

    /**
     * 批量删除剧目收藏
     *
     * @param favoriteIds 需要删除的收藏ID
     * @return 结果
     */
    @Override
    public int deleteBizUserFavoriteByIds(Long[] favoriteIds)
    {
        return bizUserFavoriteMapper.deleteBizUserFavoriteByIds(favoriteIds);
    }

    /**
     * 删除剧目收藏
     *
     * @param favoriteId 收藏ID
     * @return 结果
     */
    @Override
    public int deleteBizUserFavoriteById(Long favoriteId)
    {
        return bizUserFavoriteMapper.deleteBizUserFavoriteById(favoriteId);
    }
}
