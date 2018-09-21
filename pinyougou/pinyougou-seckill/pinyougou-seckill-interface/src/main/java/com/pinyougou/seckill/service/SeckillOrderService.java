package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

public interface SeckillOrderService extends BaseService<TbSeckillOrder> {

    PageResult search(Integer page, Integer rows, TbSeckillOrder seckillOrder);

    /**
     * 根据秒杀商品id生成秒杀订单
     *
     * @param seckillId 秒杀商品id
     * @param username 当前去秒杀的用户
     * @return 秒杀订单id
     */
    String submitOrder(Long seckillId, String username) throws InterruptedException;

    /**
     * 根据秒杀订单id查询redis中的秒杀订单
     * @param outTradeNo 秒杀订单id
     * @return 秒杀订单
     */
    TbSeckillOrder findSeckillOrderInRedisByOrderId(String outTradeNo);

    /**
     * 将秒杀订单id对应的redis未支付的订单修改为已支付再将数据同步到mysql中
     * @param outTradeNo 秒杀订单id
     * @param transaction_id 微信交易号
     */
    void updateSeckillOrderInRedisToDb(String outTradeNo, String transaction_id);
}