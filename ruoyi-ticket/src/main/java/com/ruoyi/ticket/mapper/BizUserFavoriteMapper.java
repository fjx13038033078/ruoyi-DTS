package com.ruoyi.ticket.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.ticket.domain.BizUserFavorite;

/**
 * 剧目收藏 数据层
 *
 * @author ruoyi
 */
public interface BizUserFavoriteMapper
{
    /**
     * 查询剧目收藏
     *
     * @param favoriteId 收藏ID
     * @return 剧目收藏
     */
    BizUserFavorite selectBizUserFavoriteById(Long favoriteId);

    /**
     * 查询剧目收藏列表
     *
     * @param bizUserFavorite 剧目收藏
     * @return 剧目收藏集合
     */
    List<BizUserFavorite> selectBizUserFavoriteList(BizUserFavorite bizUserFavorite);

    /**
     * 新增剧目收藏
     *
     * @param bizUserFavorite 剧目收藏
     * @return 结果
     */
    int insertBizUserFavorite(BizUserFavorite bizUserFavorite);

    /**
     * 修改剧目收藏
     *
     * @param bizUserFavorite 剧目收藏
     * @return 结果
     */
    int updateBizUserFavorite(BizUserFavorite bizUserFavorite);

    /**
     * 删除剧目收藏
     *
     * @param favoriteId 收藏ID
     * @return 结果
     */
    int deleteBizUserFavoriteById(Long favoriteId);

    /**
     * 批量删除剧目收藏
     *
     * @param favoriteIds 需要删除的收藏ID
     * @return 结果
     */
    int deleteBizUserFavoriteByIds(Long[] favoriteIds);

    /**
     * 按用户ID和演出ID删除收藏
     *
     * @param userId 用户ID
     * @param performanceId 演出ID
     * @return 结果
     */
    int deleteByUserIdAndPerformanceId(@Param("userId") Long userId, @Param("performanceId") Long performanceId);
}
