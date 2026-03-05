package com.ruoyi.ticket.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分兑换商品表 biz_points_item
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizPointsItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    private Long itemId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String name;

    /** 所需积分 */
    @Excel(name = "所需积分")
    private Integer pointsRequired;

    /** 剩余库存 */
    @Excel(name = "剩余库存")
    private Integer stock;

    /** 状态（0下架 1上架） */
    @Excel(name = "状态", readConverterExp = "0=下架,1=上架")
    private String status;
}
