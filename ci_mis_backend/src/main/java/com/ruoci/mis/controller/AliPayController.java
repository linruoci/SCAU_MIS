package com.ruoci.mis.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ruoci.mis.common.config.AliPayConfig;
import com.ruoci.mis.entity.Orders;
import com.ruoci.mis.service.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ruoci
 **/
@RestController
@RequestMapping("/alipay")
public class AliPayController {


    @Resource
    private AliPayConfig aliPayConfig;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private OrdersService ordersService;

    @GetMapping("/pay")
    public void pay(String orderNo, HttpServletResponse httpServletResponse) throws IOException {


        Orders orders = ordersService.selectByOrderNo(orderNo);
        if (orders == null){
            return;
        }

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(),
                SIGN_TYPE);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());
        bizContent.set("total_amount", orders.getAmount());
        bizContent.set("subject", orders.getName());
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:8080/orders");

        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpServletResponse.setContentType("text/html;charset=" + CHARSET);
        httpServletResponse.getWriter().write(form);
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }


    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");


            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParameterMap = request.getParameterMap();

            for (String name : requestParameterMap.keySet()){
                params.put(name, request.getParameter(name));
            }
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean flag = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), CHARSET);

            if (flag){
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                String tradeNo =  params.get("out_trade_no");
                String gmPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");

                Orders orders = ordersService.selectByOrderNo(tradeNo);
                orders.setStatus("已支付");
                orders.setPayTime(gmPayment);
                ordersService.updateById(orders);
            }



        }
        return "success";
    }




}
