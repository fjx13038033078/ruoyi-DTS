package com.ruoyi.web.controller.ticket;

import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.ticket.domain.BizPerformanceSession;
import com.ruoyi.ticket.service.IBizPerformanceSessionService;

/**
 * 演出场次 后台管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ticket/session")
public class BizPerformanceSessionController extends BaseController
{
    @Autowired
    private IBizPerformanceSessionService bizPerformanceSessionService;

    /**
     * 获取场次列表
     */
    @PreAuthorize("@ss.hasPermi('ticket:session:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPerformanceSession bizPerformanceSession)
    {
        startPage();
        List<BizPerformanceSession> list = bizPerformanceSessionService.selectBizPerformanceSessionList(bizPerformanceSession);
        return getDataTable(list);
    }

    /**
     * 导出场次列表
     */
    @Log(title = "场次管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('ticket:session:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPerformanceSession bizPerformanceSession)
    {
        List<BizPerformanceSession> list = bizPerformanceSessionService.selectBizPerformanceSessionList(bizPerformanceSession);
        ExcelUtil<BizPerformanceSession> util = new ExcelUtil<>(BizPerformanceSession.class);
        util.exportExcel(response, list, "场次数据");
    }

    /**
     * 根据场次ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('ticket:session:query')")
    @GetMapping(value = "/{sessionId}")
    public AjaxResult getInfo(@PathVariable Long sessionId)
    {
        return success(bizPerformanceSessionService.selectBizPerformanceSessionById(sessionId));
    }

    /**
     * 新增场次
     */
    @PreAuthorize("@ss.hasPermi('ticket:session:add')")
    @Log(title = "场次管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizPerformanceSession bizPerformanceSession)
    {
        return toAjax(bizPerformanceSessionService.insertBizPerformanceSession(bizPerformanceSession));
    }

    /**
     * 修改场次
     */
    @PreAuthorize("@ss.hasPermi('ticket:session:edit')")
    @Log(title = "场次管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody BizPerformanceSession bizPerformanceSession)
    {
        return toAjax(bizPerformanceSessionService.updateBizPerformanceSession(bizPerformanceSession));
    }

    /**
     * 删除场次
     */
    @PreAuthorize("@ss.hasPermi('ticket:session:remove')")
    @Log(title = "场次管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestParam("sessionIds") Long[] sessionIds)
    {
        return toAjax(bizPerformanceSessionService.deleteBizPerformanceSessionByIds(sessionIds));
    }

    /**
     * 批量生成场次座位
     *
     * @param sessionId 场次ID
     * @param areaName  区域名称
     * @param price     票价
     * @param rows      排数（默认10）
     * @param cols      每排座位数（默认20）
     */
    @PreAuthorize("@ss.hasPermi('ticket:session:edit')")
    @Log(title = "场次管理", businessType = BusinessType.INSERT)
    @PostMapping("/batchGenerateSeats")
    public AjaxResult batchGenerateSeats(@RequestParam Long sessionId,
                                        @RequestParam String areaName,
                                        @RequestParam BigDecimal price,
                                        @RequestParam(required = false, defaultValue = "10") Integer rows,
                                        @RequestParam(required = false, defaultValue = "20") Integer cols)
    {
        int count = bizPerformanceSessionService.batchGenerateSeats(sessionId, areaName, price, rows, cols);
        return success("成功生成" + count + "个座位");
    }
}
