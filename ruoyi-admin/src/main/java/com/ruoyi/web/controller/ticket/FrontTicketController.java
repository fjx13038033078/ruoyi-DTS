package com.ruoyi.web.controller.ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.ticket.domain.*;
import com.ruoyi.ticket.service.*;

/**
 * 前台票务接口（面向普通观众）
 * 需登录，无 @PreAuthorize 权限校验
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/front/ticket")
public class FrontTicketController extends BaseController {

    private final IBizPerformanceService bizPerformanceService;
    private final IBizPerformanceSessionService bizPerformanceSessionService;
    private final IBizSessionSeatService bizSessionSeatService;
    private final IBizUserFavoriteService bizUserFavoriteService;
    private final IBizPointsItemService bizPointsItemService;
    private final IBizPointsRecordService bizPointsRecordService;
    private final ISysUserService sysUserService;
    private final IOrderPlacementService orderPlacementService;

    public FrontTicketController(IBizPerformanceService bizPerformanceService,
                                 IBizPerformanceSessionService bizPerformanceSessionService,
                                 IBizSessionSeatService bizSessionSeatService,
                                 IBizUserFavoriteService bizUserFavoriteService,
                                 IBizPointsItemService bizPointsItemService,
                                 IBizPointsRecordService bizPointsRecordService,
                                 ISysUserService sysUserService,
                                 IOrderPlacementService orderPlacementService) {
        this.bizPerformanceService = bizPerformanceService;
        this.bizPerformanceSessionService = bizPerformanceSessionService;
        this.bizSessionSeatService = bizSessionSeatService;
        this.bizUserFavoriteService = bizUserFavoriteService;
        this.bizPointsItemService = bizPointsItemService;
        this.bizPointsRecordService = bizPointsRecordService;
        this.sysUserService = sysUserService;
        this.orderPlacementService = orderPlacementService;
    }

    /**
     * 获取上架的演出列表（支持按是否推荐排序）
     */
    @GetMapping("/performance/list")
    public TableDataInfo listPerformance(BizPerformance bizPerformance) {
        startPage();
        List<BizPerformance> list = bizPerformanceService.selectBizPerformanceListForFront(bizPerformance);
        return getDataTable(list);
    }

    /**
     * 获取演出详情及关联的场次列表
     */
    @GetMapping("/performance/{performanceId}")
    public AjaxResult getPerformanceDetail(@PathVariable Long performanceId) {
        BizPerformance performance = bizPerformanceService.selectBizPerformanceById(performanceId);
        if (performance == null) {
            return error("演出不存在");
        }
        if (!"1".equals(performance.getStatus())) {
            return error("演出已下架");
        }
        BizPerformanceSession query = new BizPerformanceSession();
        query.setPerformanceId(performanceId);
        List<BizPerformanceSession> sessions = bizPerformanceSessionService.selectBizPerformanceSessionList(query);
        Map<String, Object> result = new HashMap<>(4);
        result.put("performance", performance);
        result.put("sessions", sessions);
        return success(result);
    }

    /**
     * 获取某场次的所有座位及其状态
     */
    @GetMapping("/session/{sessionId}/seats")
    public AjaxResult getSessionSeats(@PathVariable Long sessionId) {
        BizSessionSeat query = new BizSessionSeat();
        query.setSessionId(sessionId);
        List<BizSessionSeat> seats = bizSessionSeatService.selectBizSessionSeatList(query);
        return success(seats);
    }

    /**
     * 选座下单
     * @param sessionId 场次ID
     * @param seatIds 座位ID列表
     */
    @PostMapping("/order/place")
    public AjaxResult placeOrder(@RequestParam Long sessionId, @RequestParam List<Long> seatIds) {
        try {
            BizOrder order = orderPlacementService.placeOrder(getUserId(), sessionId, seatIds);
            return success(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 收藏剧目
     */
    @PostMapping("/favorite/add")
    public AjaxResult addFavorite(@RequestParam Long performanceId) {
        Long userId = getUserId();
        BizPerformance performance = bizPerformanceService.selectBizPerformanceById(performanceId);
        if (performance == null || !"1".equals(performance.getStatus())) {
            return error("演出不存在或已下架");
        }
        BizUserFavorite exist = new BizUserFavorite();
        exist.setUserId(userId);
        exist.setPerformanceId(performanceId);
        List<BizUserFavorite> list = bizUserFavoriteService.selectBizUserFavoriteList(exist);
        if (!list.isEmpty()) {
            return warn("已收藏过该剧目");
        }
        BizUserFavorite favorite = new BizUserFavorite();
        favorite.setUserId(userId);
        favorite.setPerformanceId(performanceId);
        return toAjax(bizUserFavoriteService.insertBizUserFavorite(favorite));
    }

    /**
     * 取消收藏
     */
    @PostMapping("/favorite/remove")
    public AjaxResult removeFavorite(@RequestParam Long performanceId) {
        Long userId = getUserId();
        return toAjax(bizUserFavoriteService.deleteByUserIdAndPerformanceId(userId, performanceId));
    }

    /**
     * 积分兑换：扣减用户积分并增加兑换记录
     */
    @PostMapping("/points/exchange")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult exchangePoints(@RequestParam Long itemId) {
        Long userId = getUserId();
        BizPointsItem item = bizPointsItemService.selectBizPointsItemById(itemId);
        if (item == null) {
            return error("兑换商品不存在");
        }
        if (!"1".equals(item.getStatus())) {
            return error("商品已下架");
        }
        if (item.getStock() == null || item.getStock() <= 0) {
            return error("库存不足");
        }
        Integer pointsRequired = item.getPointsRequired();
        if (pointsRequired == null || pointsRequired <= 0) {
            return error("商品配置异常");
        }
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null || user.getPoints() == null || user.getPoints() < pointsRequired) {
            return error("积分不足");
        }
        if (!sysUserService.deductPoints(userId, pointsRequired)) {
            return error("积分扣减失败，请重试");
        }
        item.setStock(item.getStock() - 1);
        bizPointsItemService.updateBizPointsItem(item);
        BizPointsRecord record = new BizPointsRecord();
        record.setUserId(userId);
        record.setItemId(itemId);
        record.setPointsDeducted(pointsRequired);
        bizPointsRecordService.insertBizPointsRecord(record);
        return success("兑换成功");
    }
}
