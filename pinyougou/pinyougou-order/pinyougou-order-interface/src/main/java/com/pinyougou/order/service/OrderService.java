package com.pinyougou.order.service;

import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

public interface OrderService extends BaseService<TbOrder> {

    PageResult search(Integer page, Integer rows, TbOrder order);

    /**
     * 保存订单、明细、支付日志到数据库中
     * @param order 订单信息
     * @return 支付日志id
     */
    String saveOrder(TbOrder order);

    /**
     * 根据支付日志id查询支付日志
     * @param outTradeNo 支付日志id
     * @return 支付日志
     */
    TbPayLog findPayLogById(String outTradeNo);

    /**
     * 更新订单支付状态：支付信息中的支付状态，本支付对应的所有订单的支付状态
     * @param outTradeNo 支付日志id
     * @param transaction_id 微信交易号
     */
    void updateOrderStatus(String outTradeNo, String transaction_id);
}