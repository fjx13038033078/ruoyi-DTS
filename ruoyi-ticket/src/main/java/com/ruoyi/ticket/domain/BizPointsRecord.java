package com.ruoyi.ticket.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分兑换记录表 biz_points_record
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizPointsRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 兑换记录ID */
    private Long recordId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 商品ID */
    @Excel(name = "商品ID")
    private Long itemId;

    /** 本次扣除的积分 */
    @Excel(name = "扣除积分")
    private Integer pointsDeducted;

    /** 商品名称（关联查询，非表字段） */
    private String itemName;
}
