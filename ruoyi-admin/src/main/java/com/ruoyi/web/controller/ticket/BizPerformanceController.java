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
import com.ruoyi.ticket.domain.BizPerformance;
import com.ruoyi.ticket.service.IBizPerformanceService;

/**
 * 演出基本信息 后台管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ticket/performance")
public class BizPerformanceController extends BaseController
{
    @Autowired
    private IBizPerformanceService bizPerformanceService;

    /**
     * 获取演出列表
     */
    @PreAuthorize("@ss.hasPermi('ticket:performance:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPerformance bizPerformance)
    {
        startPage();
        List<BizPerformance> list = bizPerformanceService.selectBizPerformanceList(bizPerformance);
        return getDataTable(list);
    }

    /**
     * 导出演出列表
     */
    @Log(title = "演出管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('ticket:performance:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPerformance bizPerformance)
    {
        List<BizPerformance> list = bizPerformanceService.selectBizPerformanceList(bizPerformance);
        ExcelUtil<BizPerformance> util = new ExcelUtil<>(BizPerformance.class);
        util.exportExcel(response, list, "演出数据");
    }

    /**
     * 根据演出ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('ticket:performance:query')")
    @GetMapping(value = "/{performanceId}")
    public AjaxResult getInfo(@PathVariable Long performanceId)
    {
        return success(bizPerformanceService.selectBizPerformanceById(performanceId));
    }

    /**
     * 新增演出
     */
    @PreAuthorize("@ss.hasPermi('ticket:performance:add')")
    @Log(title = "演出管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizPerformance bizPerformance)
    {
        return toAjax(bizPerformanceService.insertBizPerformance(bizPerformance));
    }

    /**
     * 修改演出
     */
    @PreAuthorize("@ss.hasPermi('ticket:performance:edit')")
    @Log(title = "演出管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody BizPerformance bizPerformance)
    {
        return toAjax(bizPerformanceService.updateBizPerformance(bizPerformance));
    }

    /**
     * 删除演出
     */
    @PreAuthorize("@ss.hasPermi('ticket:performance:remove')")
    @Log(title = "演出管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestParam("performanceIds") Long[] performanceIds)
    {
        return toAjax(bizPerformanceService.deleteBizPerformanceByIds(performanceIds));
    }
}
