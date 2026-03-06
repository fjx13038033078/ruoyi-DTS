package com.ruoyi.web.controller.ticket;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.ticket.service.ITicketStatsService;

/**
 * 票务统计
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ticket/stats")
public class TicketStatsController extends BaseController {

    private final ITicketStatsService ticketStatsService;

    public TicketStatsController(ITicketStatsService ticketStatsService) {
        this.ticketStatsService = ticketStatsService;
    }

    /**
     * 概览统计
     */
    @PreAuthorize("@ss.hasPermi('ticket:order:list')")
    @GetMapping("/summary")
    public AjaxResult summary() {
        Map<String, Object> result = ticketStatsService.getSummary();
        return success(result);
    }

    /**
     * 售票统计（日/周/月）
     */
    @PreAuthorize("@ss.hasPermi('ticket:order:list')")
    @GetMapping("/sales")
    public AjaxResult salesStats(@RequestParam(defaultValue = "day") String period) {
        Map<String, Object> result = ticketStatsService.getSalesStats(period);
        return success(result);
    }

    /**
     * 热门座位区域统计
     */
    @PreAuthorize("@ss.hasPermi('ticket:order:list')")
    @GetMapping("/area")
    public AjaxResult areaStats() {
        List<Map<String, Object>> list = ticketStatsService.getAreaStats();
        return success(list);
    }
}
