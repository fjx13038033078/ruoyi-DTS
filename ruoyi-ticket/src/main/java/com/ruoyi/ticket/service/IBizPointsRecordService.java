package com.ruoyi.ticket.service;

import java.util.List;
import com.ruoyi.ticket.domain.BizPointsRecord;

/**
 * 积分兑换记录 服务层
 *
 * @author ruoyi
 */
public interface IBizPointsRecordService
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
     * 批量删除积分兑换记录
     *
     * @param recordIds 需要删除的兑换记录ID
     * @return 结果
     */
    int deleteBizPointsRecordByIds(Long[] recordIds);

    /**
     * 删除积分兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 结果
     */
    int deleteBizPointsRecordById(Long recordId);
}
