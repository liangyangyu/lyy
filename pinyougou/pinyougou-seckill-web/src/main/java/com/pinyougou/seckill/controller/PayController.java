package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/pay")
@RestController
public class PayController {

    @Reference
    private SeckillOrderService orderService;

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
            TbSeckillOrder seckillOrder = orderService.findSeckillOrderInRedisByOrderId(outTradeNo);

            if(seckillOrder != null) {
                //本次要支付的总金额
                String totalFee = (long)(seckillOrder.getMoney().doubleValue()*100) + "";

                //2、调用支付系统业务对象的生成二维码的方法返回一些信息
                return weixinPayService.crateNative(outTradeNo, totalFee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }


    /**
     * 根据支付日志id查询支付状态
     * @param outTradeNo 支付日志id
     * @return 操作结果
     */
    @GetMapping("/queryPayStatus")
    public Result queryPayStatus(String outTradeNo){
        Result result = Result.fail("查询支付状态失败");
        //如果1分钟未支付则提示超时重新再秒杀
        int count = 0;
        try {
            while(true){
                //1、到支付系统查询订单的 支付状态
                Map<String, String> resultMap = weixinPayService.queryPayStatus(outTradeNo);

                if (resultMap == null) {
                    break;
                }

                if("SUCCESS".equals(resultMap.get("trade_state"))){
                    //2、如果支付成功要更新订单支付状态
                    orderService.updateSeckillOrderInRedisToDb(outTradeNo, resultMap.get("transaction_id"));

                    result = Result.ok("支付成功");

                    return result;
                }

                //每隔3秒执行一次
                Thread.sleep(3000);

                count++;
                if(count > 20){
                    result = Result.fail("支付超时");


                    //1、关闭在微信中的订单
                    resultMap = weixinPayService.closeOrder(outTradeNo);

                    if("ORDERPAID".equals(resultMap.get("err_code"))) {
                        //1.1、如果关闭订单的过程中被人支付了则也一样需要将redis的订单修改为已支付并同步到mysql
                        orderService.updateSeckillOrderInRedisToDb(outTradeNo, resultMap.get("transaction_id"));

                        result = Result.ok("支付成功");

                        return result;
                    } else {
                        //1.2、如果是关闭订单成功；则删除redis中的订单并加回库存
                        orderService.deleteSeckillOrderInRedis(outTradeNo);
                    }

                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
