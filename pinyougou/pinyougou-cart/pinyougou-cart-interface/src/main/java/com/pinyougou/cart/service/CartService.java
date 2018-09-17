package com.pinyougou.cart.service;

import com.pinyougou.vo.Cart;

import java.util.List;

public interface CartService {
    /**
     * 增减购物车购买商品数量
     * @param cartList 购物车列表
     * @param itemId 商品sku id
     * @param num 购买数量
     * @return 操作结果
     */
    List<Cart> addCartToCartList(List<Cart> cartList, Long itemId, Integer num);
}
