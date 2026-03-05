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
import com.ruoyi.ticket.domain.BizPointsItem;
import com.ruoyi.ticket.service.IBizPointsItemService;

/**
 * 积分兑换商品 后台管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ticket/pointsItem")
public class BizPointsItemController extends BaseController
{
    @Autowired
    private IBizPointsItemService bizPointsItemService;

    /**
     * 获取积分商品列表
     */
    @PreAuthorize("@ss.hasPermi('ticket:pointsItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPointsItem bizPointsItem)
    {
        startPage();
        List<BizPointsItem> list = bizPointsItemService.selectBizPointsItemList(bizPointsItem);
        return getDataTable(list);
    }

    /**
     * 导出积分商品列表
     */
    @Log(title = "积分商品管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('ticket:pointsItem:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPointsItem bizPointsItem)
    {
        List<BizPointsItem> list = bizPointsItemService.selectBizPointsItemList(bizPointsItem);
        ExcelUtil<BizPointsItem> util = new ExcelUtil<>(BizPointsItem.class);
        util.exportExcel(response, list, "积分商品数据");
    }

    /**
     * 根据商品ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('ticket:pointsItem:query')")
    @GetMapping(value = "/{itemId}")
    public AjaxResult getInfo(@PathVariable Long itemId)
    {
        return success(bizPointsItemService.selectBizPointsItemById(itemId));
    }

    /**
     * 新增积分商品
     */
    @PreAuthorize("@ss.hasPermi('ticket:pointsItem:add')")
    @Log(title = "积分商品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizPointsItem bizPointsItem)
    {
        return toAjax(bizPointsItemService.insertBizPointsItem(bizPointsItem));
    }

    /**
     * 修改积分商品
     */
    @PreAuthorize("@ss.hasPermi('ticket:pointsItem:edit')")
    @Log(title = "积分商品管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody BizPointsItem bizPointsItem)
    {
        return toAjax(bizPointsItemService.updateBizPointsItem(bizPointsItem));
    }

    /**
     * 删除积分商品
     */
    @PreAuthorize("@ss.hasPermi('ticket:pointsItem:remove')")
    @Log(title = "积分商品管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestParam("itemIds") Long[] itemIds)
    {
        return toAjax(bizPointsItemService.deleteBizPointsItemByIds(itemIds));
    }
}
