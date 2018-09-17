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

    /**
     * 根据用户名查询在redis中的购物车数据
     * @param username 用户名
     * @return 购物车列表
     */
    List<Cart> findCartListInRedisByUsername(String username);

    /**
     * 将购物车列表保存到redis中
     * @param cartList 购物车列表
     * @param username 用户名
     */
    void saveCartListToRedis(List<Cart> cartList, String username);

    /**
     * 合并两个列表数据
     * @param cartList1 购物车集合
     * @param cartList2 购物车集合
     * @return 购物车集合
     */
    List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2);
}
