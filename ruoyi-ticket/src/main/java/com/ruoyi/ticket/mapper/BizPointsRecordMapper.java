package com.ruoyi.ticket.mapper;

import java.util.List;
import com.ruoyi.ticket.domain.BizPointsRecord;

/**
 * 积分兑换记录 数据层
 *
 * @author ruoyi
 */
public interface BizPointsRecordMapper
{
    /**
     * 查询积分兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 积分兑换记录
     */
    BizPointsRecord selectBizPointsRecordById(Long recordId);

    /**
     * 查询积分兑换记录列表
     *
     * @param bizPointsRecord 积分兑换记录
     * @return 积分兑换记录集合
     */
    List<BizPointsRecord> selectBizPointsRecordList(BizPointsRecord bizPointsRecord);

    /**
     * 新增积分兑换记录
     *
     * @param bizPointsRecord 积分兑换记录
     * @return 结果
     */
    int insertBizPointsRecord(BizPointsRecord bizPointsRecord);

    /**
     * 修改积分兑换记录
     *
     * @param bizPointsRecord 积分兑换记录
     * @return 结果
     */
    int updateBizPointsRecord(BizPointsRecord bizPointsRecord);

    /**
     * 删除积分兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 结果
     */
    int deleteBizPointsRecordById(Long recordId);

    /**
     * 批量删除积分兑换记录
     *
     * @param recordIds 需要删除的兑换记录ID
     * @return 结果
     */
    int deleteBizPointsRecordByIds(Long[] recordIds);
}
