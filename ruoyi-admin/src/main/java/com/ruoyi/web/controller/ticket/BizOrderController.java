package com.ruoyi.web.controller.ticket;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.ticket.domain.BizOrder;
import com.ruoyi.ticket.service.IBizOrderService;

/**
 * 购票订单 后台管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ticket/order")
public class BizOrderController extends BaseController
{
    @Autowired
    private IBizOrderService bizOrderService;

    /**
     * 获取订单列表
     */
    @PreAuthorize("@ss.hasPermi('ticket:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizOrder bizOrder)
    {
        startPage();
        List<BizOrder> list = bizOrderService.selectBizOrderList(bizOrder);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @Log(title = "订单管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('ticket:order:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizOrder bizOrder)
    {
        List<BizOrder> list = bizOrderService.selectBizOrderList(bizOrder);
        ExcelUtil<BizOrder> util = new ExcelUtil<>(BizOrder.class);
        util.exportExcel(response, list, "订单数据");
    }

    /**
     * 根据订单ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('ticket:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable Long orderId)
    {
        return success(bizOrderService.selectBizOrderById(orderId));
    }

    /**
     * 新增订单
     */
    @PreAuthorize("@ss.hasPermi('ticket:order:add')")
    @Log(title = "订单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizOrder bizOrder)
    {
        return toAjax(bizOrderService.insertBizOrder(bizOrder));
    }

    /**
     * 修改订单
     */
    @PreAuthorize("@ss.hasPermi('ticket:order:edit')")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody BizOrder bizOrder)
    {
        return toAjax(bizOrderService.updateBizOrder(bizOrder));
    }

    /**
     * 删除订单
     */
    @PreAuthorize("@ss.hasPermi('ticket:order:remove')")
    @Log(title = "订单管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestParam("orderIds") Long[] orderIds)
    {
        return toAjax(bizOrderService.deleteBizOrderByIds(orderIds));
    }
}
