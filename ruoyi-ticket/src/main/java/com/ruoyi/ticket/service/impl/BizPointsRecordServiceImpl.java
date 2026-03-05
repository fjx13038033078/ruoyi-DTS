package com.ruoyi.ticket.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ticket.mapper.BizPointsRecordMapper;
import com.ruoyi.ticket.domain.BizPointsRecord;
import com.ruoyi.ticket.service.IBizPointsRecordService;

/**
 * 积分兑换记录 服务层实现
 *
 * @author ruoyi
 */
@Service
public class BizPointsRecordServiceImpl implements IBizPointsRecordService
{
    @Autowired
    private BizPointsRecordMapper bizPointsRecordMapper;

    /**
     * 查询积分兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 积分兑换记录
     */
    @Override
    public BizPointsRecord selectBizPointsRecordById(Long recordId)
    {
        return bizPointsRecordMapper.selectBizPointsRecordById(recordId);
    }

    /**
     * 查询积分兑换记录列表
     *
     * @param bizPointsRecord 积分兑换记录
     * @return 积分兑换记录
     */
    @Override
    public List<BizPointsRecord> selectBizPointsRecordList(BizPointsRecord bizPointsRecord)
    {
        return bizPointsRecordMapper.selectBizPointsRecordList(bizPointsRecord);
    }

    /**
     * 新增积分兑换记录
     *
     * @param bizPointsRecord 积分兑换记录
     * @return 结果
     */
    @Override
    public int insertBizPointsRecord(BizPointsRecord bizPointsRecord)
    {
        return bizPointsRecordMapper.insertBizPointsRecord(bizPointsRecord);
    }

    /**
     * 修改积分兑换记录
     *
     * @param bizPointsRecord 积分兑换记录
     * @return 结果
     */
    @Override
    public int updateBizPointsRecord(BizPointsRecord bizPointsRecord)
    {
        return bizPointsRecordMapper.updateBizPointsRecord(bizPointsRecord);
    }

    /**
     * 批量删除积分兑换记录
     *
     * @param recordIds 需要删除的兑换记录ID
     * @return 结果
     */
    @Override
    public int deleteBizPointsRecordByIds(Long[] recordIds)
    {
        return bizPointsRecordMapper.deleteBizPointsRecordByIds(recordIds);
    }

    /**
     * 删除积分兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 结果
     */
    @Override
    public int deleteBizPointsRecordById(Long recordId)
    {
        return bizPointsRecordMapper.deleteBizPointsRecordById(recordId);
    }
}
