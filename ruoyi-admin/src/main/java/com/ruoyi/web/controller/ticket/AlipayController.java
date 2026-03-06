package com.ruoyi.web.controller.ticket;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ruoyi.common.annotation.Anonymous;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ruoyi.ticket.config.AlipayConfig.AlipayProperties;
import com.ruoyi.ticket.domain.BizOrder;
import com.ruoyi.ticket.domain.BizOrderTicket;
import com.ruoyi.ticket.domain.BizPerformance;
import com.ruoyi.ticket.domain.BizPerformanceSession;
import com.ruoyi.ticket.domain.BizSessionSeat;
import com.ruoyi.ticket.service.IBizOrderService;
import com.ruoyi.ticket.service.IBizOrderTicketService;
import com.ruoyi.ticket.service.IBizPerformanceService;
import com.ruoyi.ticket.service.IBizPerformanceSessionService;
import com.ruoyi.ticket.service.IBizSessionSeatService;

/**
 * 支付宝沙箱支付
 * 不加 @PreAuthorize 权限校验
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/order/alipay")
public class AlipayController {

    private static final String SUCCESS_REDIRECT_URL = "http://localhost:80/front/pay-success";

    private final AlipayClient alipayClient;
    private final AlipayProperties alipayProperties;
    private final IBizOrderService bizOrderService;
    private final IBizOrderTicketService bizOrderTicketService;
    private final IBizSessionSeatService bizSessionSeatService;
    private final IBizPerformanceService bizPerformanceService;
    private final IBizPerformanceSessionService bizPerformanceSessionService;

    public AlipayController(AlipayClient alipayClient,
                            AlipayProperties alipayProperties,
                            IBizOrderService bizOrderService,
                            IBizOrderTicketService bizOrderTicketService,
                            IBizSessionSeatService bizSessionSeatService,
                            IBizPerformanceService bizPerformanceService,
                            IBizPerformanceSessionService bizPerformanceSessionService) {
        this.alipayClient = alipayClient;
        this.alipayProperties = alipayProperties;
        this.bizOrderService = bizOrderService;
        this.bizOrderTicketService = bizOrderTicketService;
        this.bizSessionSeatService = bizSessionSeatService;
        this.bizPerformanceService = bizPerformanceService;
        this.bizPerformanceSessionService = bizPerformanceSessionService;
    }

    /**
     * 发起支付（GET 或 POST）
     * 接收订单号 orderNo，查询订单并生成支付宝收银台 HTML 表单字符串返回给前端
     */
    @Anonymous
    @GetMapping("/pay")
    @PostMapping("/pay")
    public void pay(@RequestParam String orderNo, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        BizOrder query = new BizOrder();
        query.setOrderNo(orderNo);
        List<BizOrder> list = bizOrderService.selectBizOrderList(query);
        if (list == null || list.isEmpty()) {
            response.getWriter().write("<html><body><script>alert('订单不存在');history.back();</script></body></html>");
            return;
        }
        BizOrder order = list.get(0);
        if (!"0".equals(order.getStatus())) {
            response.getWriter().write("<html><body><script>alert('订单状态异常，无法支付');history.back();</script></body></html>");
            return;
        }
        String subject = buildSubject(order);
        String totalAmount = order.getTotalAmount() != null ? order.getTotalAmount().toString() : "0.01";

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(alipayProperties.getReturnUrl());
        request.setNotifyUrl(alipayProperties.getNotifyUrl());
        request.setBizContent("{" +
            "\"out_trade_no\":\"" + orderNo + "\"," +
            "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
            "\"total_amount\":" + totalAmount + "," +
            "\"subject\":\"" + escapeJson(subject) + "\"" +
            "}");

        try {
            String form = alipayClient.pageExecute(request).getBody();
            response.getWriter().write(form != null ? form : "");
        } catch (AlipayApiException e) {
            response.getWriter().write("<html><body><script>alert('支付请求失败：" + escapeJs(e.getMessage()) + "');history.back();</script></body></html>");
        }
    }

    /**
     * 同步返回：支付宝支付成功后浏览器跳转
     * 处理订单状态并重定向到前端成功页
     */
    @Anonymous
    @GetMapping("/return")
    @Transactional(rollbackFor = Exception.class)
    public void alipayReturn(@RequestParam(required = false) String out_trade_no,
                             @RequestParam(required = false) String trade_no,
                             HttpServletResponse response) throws Exception {
        if (out_trade_no == null || out_trade_no.isEmpty()) {
            response.sendRedirect(SUCCESS_REDIRECT_URL + "?error=no_order");
            return;
        }
        BizOrder query = new BizOrder();
        query.setOrderNo(out_trade_no);
        List<BizOrder> list = bizOrderService.selectBizOrderList(query);
        if (list == null || list.isEmpty()) {
            response.sendRedirect(SUCCESS_REDIRECT_URL + "?error=order_not_found");
            return;
        }
        BizOrder order = list.get(0);
        if (!"0".equals(order.getStatus())) {
            response.sendRedirect(SUCCESS_REDIRECT_URL);
            return;
        }
        order.setStatus("1");
        order.setTransactionId(trade_no);
        order.setPayTime(new Date());
        bizOrderService.updateBizOrder(order);

        BizOrderTicket ticketQuery = new BizOrderTicket();
        ticketQuery.setOrderId(order.getOrderId());
        ticketQuery.setStatus("1");
        List<BizOrderTicket> tickets = bizOrderTicketService.selectBizOrderTicketList(ticketQuery);
        for (BizOrderTicket t : tickets) {
            BizSessionSeat seat = bizSessionSeatService.selectBizSessionSeatById(t.getSeatId());
            if (seat != null) {
                seat.setStatus("2");
                bizSessionSeatService.updateBizSessionSeat(seat);
            }
        }
        response.sendRedirect(SUCCESS_REDIRECT_URL);
    }

    private String buildSubject(BizOrder order) {
        if (order.getSessionId() == null) {
            return "音乐剧票务订单-" + order.getOrderNo();
        }
        BizPerformanceSession session = bizPerformanceSessionService.selectBizPerformanceSessionById(order.getSessionId());
        if (session == null || session.getPerformanceId() == null) {
            return "音乐剧票务订单-" + order.getOrderNo();
        }
        BizPerformance performance = bizPerformanceService.selectBizPerformanceById(session.getPerformanceId());
        if (performance != null && performance.getTitle() != null) {
            return performance.getTitle() + "-" + order.getOrderNo();
        }
        return "音乐剧票务订单-" + order.getOrderNo();
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String escapeJs(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
    }
}
