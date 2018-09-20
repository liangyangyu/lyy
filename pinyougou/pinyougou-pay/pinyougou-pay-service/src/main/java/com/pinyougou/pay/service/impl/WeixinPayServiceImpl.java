package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.common.util.HttpClient;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {


    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    @Value("${notifyurl}")
    private String notifyurl;

    @Override
    public Map<String, String> crateNative(String outTradeNo, String totalFee) {
        Map<String, String> returnMap = new HashMap<>();

        try {
            //1、组装微信支付系统需要的数据
            Map<String, String> param = new HashMap<>();
            //公众账号ID
            param.put("appid", appid);
            //商户号
            param.put("mch_id", partner);
            //随机字符串；微信提供了工具类可生成
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            //签名；可以使用微信工具类在提交的时候生成
            //param.put("sign", "");
            //商品描述
            param.put("body", "品优购");
            //商户订单号
            param.put("out_trade_no", outTradeNo);
            //标价金额
            param.put("total_fee", totalFee);
            //终端IP
            param.put("spbill_create_ip", "127.0.0.1");
            //通知地址
            param.put("notify_url", notifyurl);
            //交易类型
            param.put("trade_type", "NATIVE");

            //转换成为签名了的xml内容
            String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);

            System.out.println("发送到微信支付系统 统一下单 的请求内容为：" + signedXml);

            //2、发送请求到微信支付系统
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();

            //3、处理返回结果并返回
            String content = httpClient.getContent();
            System.out.println("微信支付系统 统一下单 返回的内容为：" + content);

            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);

            //业务操作结果
            returnMap.put("result_code", resultMap.get("result_code"));
            //支付二维码地址
            returnMap.put("code_url", resultMap.get("code_url"));
            returnMap.put("totalFee", totalFee);
            returnMap.put("outTradeNo", outTradeNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnMap;
    }

    @Override
    public Map<String, String> queryPayStatus(String outTradeNo) {
        try {
            //1、组装微信支付系统需要的数据
            Map<String, String> param = new HashMap<>();
            //公众账号ID
            param.put("appid", appid);
            //商户号
            param.put("mch_id", partner);
            //随机字符串；微信提供了工具类可生成
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            //签名；可以使用微信工具类在提交的时候生成
            //param.put("sign", "");

            //商户订单号
            param.put("out_trade_no", outTradeNo);

            //转换成为签名了的xml内容
            String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);

            System.out.println("发送到微信支付系统 查询状态 的请求内容为：" + signedXml);

            //2、发送请求到微信支付系统
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();

            //3、处理返回结果并返回
            String content = httpClient.getContent();
            System.out.println("微信支付系统 查询状态 返回的内容为：" + content);

            return WXPayUtil.xmlToMap(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
