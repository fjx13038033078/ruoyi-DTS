package com.ruoyi.web.controller.ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
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
    private final IOrderPlacementService orderPlacementService;
    private final IBizOrderService bizOrderService;

    public FrontTicketController(IBizPerformanceService bizPerformanceService,
                                 IBizPerformanceSessionService bizPerformanceSessionService,
                                 IBizSessionSeatService bizSessionSeatService,
                                 IBizUserFavoriteService bizUserFavoriteService,
                                 IBizPointsItemService bizPointsItemService,
                                 IBizPointsRecordService bizPointsRecordService,
                                 IOrderPlacementService orderPlacementService,
                                 IBizOrderService bizOrderService) {
        this.bizPerformanceService = bizPerformanceService;
        this.bizPerformanceSessionService = bizPerformanceSessionService;
        this.bizSessionSeatService = bizSessionSeatService;
        this.bizUserFavoriteService = bizUserFavoriteService;
        this.bizPointsItemService = bizPointsItemService;
        this.bizPointsRecordService = bizPointsRecordService;
        this.orderPlacementService = orderPlacementService;
        this.bizOrderService = bizOrderService;
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
     * 我的订单列表（当前登录用户）
     */
    @GetMapping("/order/list")
    public TableDataInfo listMyOrders(BizOrder query) {
        query.setUserId(getUserId());
        startPage();
        List<BizOrder> list = bizOrderService.selectBizOrderList(query);
        return getDataTable(list);
    }

    /**
     * 获取当前用户的收藏列表（含演出详情）
     */
    @GetMapping("/favorite/list")
    public TableDataInfo listMyFavorites() {
        BizUserFavorite query = new BizUserFavorite();
        query.setUserId(getUserId());
        startPage();
        List<BizUserFavorite> list = bizUserFavoriteService.selectBizUserFavoriteList(query);
        List<BizPerformance> performances = new java.util.ArrayList<>(list.size());
        for (BizUserFavorite f : list) {
            BizPerformance p = bizPerformanceService.selectBizPerformanceById(f.getPerformanceId());
            if (p != null) {
                performances.add(p);
            }
        }
        PageInfo<BizUserFavorite> pageInfo = new PageInfo<>(list);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(performances);
        rspData.setTotal(pageInfo.getTotal());
        return rspData;
    }

    /**
     * 检查当前用户是否已收藏某演出
     */
    @GetMapping("/favorite/check")
    public AjaxResult checkFavorite(@RequestParam Long performanceId) {
        Long userId = getUserId();
        BizUserFavorite query = new BizUserFavorite();
        query.setUserId(userId);
        query.setPerformanceId(performanceId);
        List<BizUserFavorite> list = bizUserFavoriteService.selectBizUserFavoriteList(query);
        return success(!list.isEmpty());
    }

    /**
     * 获取当前用户已收藏的演出ID列表（用于列表页批量展示收藏状态）
     */
    @GetMapping("/favorite/ids")
    public AjaxResult getMyFavoriteIds() {
        BizUserFavorite query = new BizUserFavorite();
        query.setUserId(getUserId());
        List<BizUserFavorite> list = bizUserFavoriteService.selectBizUserFavoriteList(query);
        List<Long> ids = list.stream().map(BizUserFavorite::getPerformanceId).collect(java.util.stream.Collectors.toList());
        return success(ids);
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
     * 获取上架的积分兑换商品列表
     */
    @GetMapping("/points/item/list")
    public TableDataInfo listPointsItems(BizPointsItem query) {
        query.setStatus("1");
        startPage();
        List<BizPointsItem> list = bizPointsItemService.selectBizPointsItemList(query);
        return getDataTable(list);
    }

    /**
     * 获取当前用户的积分兑换记录
     */
    @GetMapping("/points/record/list")
    public TableDataInfo listMyPointsRecords(BizPointsRecord query) {
        query.setUserId(getUserId());
        startPage();
        List<BizPointsRecord> list = bizPointsRecordService.selectBizPointsRecordList(query);
        return getDataTable(list);
    }

    /**
     * 积分兑换：扣减用户积分并增加兑换记录
     */
    @PostMapping("/points/exchange")
    public AjaxResult exchangePoints(@RequestParam Long itemId) {
        Long userId = getUserId();
        bizPointsItemService.exchangeItem(userId, itemId);
        return success("兑换成功");
    }
}
