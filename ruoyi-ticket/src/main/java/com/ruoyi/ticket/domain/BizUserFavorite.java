package com.ruoyi.ticket.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 剧目收藏表 biz_user_favorite
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizUserFavorite extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    private Long favoriteId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 演出的ID */
    @Excel(name = "演出ID")
    private Long performanceId;

    /** 关联的演出信息（查询时填充，非表字段） */
    private BizPerformance performance;
}
