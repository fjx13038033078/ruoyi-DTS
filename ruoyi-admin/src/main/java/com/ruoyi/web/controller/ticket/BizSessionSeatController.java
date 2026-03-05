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
import com.ruoyi.ticket.domain.BizSessionSeat;
import com.ruoyi.ticket.service.IBizSessionSeatService;

/**
 * 场次座位与票价 后台管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ticket/seat")
public class BizSessionSeatController extends BaseController
{
    @Autowired
    private IBizSessionSeatService bizSessionSeatService;

    /**
     * 获取座位列表
     */
    @PreAuthorize("@ss.hasPermi('ticket:seat:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizSessionSeat bizSessionSeat)
    {
        startPage();
        List<BizSessionSeat> list = bizSessionSeatService.selectBizSessionSeatList(bizSessionSeat);
        return getDataTable(list);
    }

    /**
     * 导出座位列表
     */
    @Log(title = "座位管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('ticket:seat:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizSessionSeat bizSessionSeat)
    {
        List<BizSessionSeat> list = bizSessionSeatService.selectBizSessionSeatList(bizSessionSeat);
        ExcelUtil<BizSessionSeat> util = new ExcelUtil<>(BizSessionSeat.class);
        util.exportExcel(response, list, "座位数据");
    }

    /**
     * 根据座位ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('ticket:seat:query')")
    @GetMapping(value = "/{seatId}")
    public AjaxResult getInfo(@PathVariable Long seatId)
    {
        return success(bizSessionSeatService.selectBizSessionSeatById(seatId));
    }

    /**
     * 新增座位
     */
    @PreAuthorize("@ss.hasPermi('ticket:seat:add')")
    @Log(title = "座位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizSessionSeat bizSessionSeat)
    {
        return toAjax(bizSessionSeatService.insertBizSessionSeat(bizSessionSeat));
    }

    /**
     * 修改座位
     */
    @PreAuthorize("@ss.hasPermi('ticket:seat:edit')")
    @Log(title = "座位管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody BizSessionSeat bizSessionSeat)
    {
        return toAjax(bizSessionSeatService.updateBizSessionSeat(bizSessionSeat));
    }

    /**
     * 删除座位
     */
    @PreAuthorize("@ss.hasPermi('ticket:seat:remove')")
    @Log(title = "座位管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestParam("seatIds") Long[] seatIds)
    {
        return toAjax(bizSessionSeatService.deleteBizSessionSeatByIds(seatIds));
    }
}
