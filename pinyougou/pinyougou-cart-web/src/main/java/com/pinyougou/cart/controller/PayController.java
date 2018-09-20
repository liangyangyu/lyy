package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/pay")
@RestController
public class PayController {

    @Reference
    private OrderService orderService;

    @Reference(timeout = 3000)
    private WeixinPayService weixinPayService;

    /**
     * 调用支付系统的统一下单地址返回支付二维码等信息
     * @param outTradeNo 支付日志id
     * @return 支付二维码等信息
     */
    @GetMapping("/createNative")
    public Map<String, String> createNative(String outTradeNo){
        try {
            //1、查询出本次要支付总金额
            TbPayLog payLog = orderService.findPayLogById(outTradeNo);

            if(payLog != null) {
                //本次要支付的总金额
                String totalFee = payLog.getTotalFee().toString();

                //2、调用支付系统业务对象的生成二维码的方法返回一些信息
                return weixinPayService.crateNative(outTradeNo, totalFee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}
