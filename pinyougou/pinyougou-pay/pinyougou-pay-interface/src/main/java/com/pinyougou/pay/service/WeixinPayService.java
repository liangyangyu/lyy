package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /**
     * 调用微信支付 统一下单 的接口生成二维码等信息
     * @param outTradeNo 商户订单号
     * @param totalFee 本次要支付的总金额
     * @return 二维码地址等信息
     */
    Map<String, String> crateNative(String outTradeNo, String totalFee);
}
