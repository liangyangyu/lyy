package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.util.CookieUtils;
import com.pinyougou.vo.Cart;
import com.pinyougou.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/cart")
@RestController
public class CartController {

    //存储在浏览器cookie中的名称
    private static final String COOKIE_CART_LIST = "PYG_CART_LIST";
    //存储在浏览器cookie中的购物车最大生存时间；1天
    private static final int COOKIE_CART_MAX_AGE = 3600*24;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Reference
    private CartService cartService;

    /**
     * 增减购物车购买商品数量
     * @param itemId 商品sku id
     * @param num 购买数量
     * @return 操作结果
     */
    @GetMapping("/addCartToCartList")
    public Result addCartToCartList(Long itemId, Integer num) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            //查询购物车列表
            List<Cart> cartList = findCartList();

            //将最新的购买数量添加到对应的购物车列表中
            cartList = cartService.addCartToCartList(cartList, itemId, num);

            if ("anonymousUser".equals(username)) {
                //未登录；操作在cookie中的购物车数据并将最新的购物车列表写回cookie
                String cartListJsonStr = JSONArray.toJSONString(cartList);
                CookieUtils.setCookie(request, response, COOKIE_CART_LIST, cartListJsonStr, COOKIE_CART_MAX_AGE, true);
            } else {
                //已经登录；操作在redis中的购物车数据并将最新的购物车列表写回redis
                cartService.saveCartListToRedis(cartList, username);
            }
            return Result.ok("加入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("加入购物车失败");
    }

    /**
     * 查询登录或者未登录情况下购物车列表数据
     * @return 购物车列表
     */
    @GetMapping("/findCartList")
    public List<Cart> findCartList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //未登录；从cookie中查询购物车数据
        List<Cart> cookie_cartList = new ArrayList<>();
        //1、获取cookie中购物车数据
        String cartListJsonStr = CookieUtils.getCookieValue(request, COOKIE_CART_LIST, true);

        //2、将cookie的购物车json格式字符串转换为集合
        if(!StringUtils.isEmpty(cartListJsonStr)){
            cookie_cartList = JSONArray.parseArray(cartListJsonStr, Cart.class);
        }

        if ("anonymousUser".equals(username)) {
            return cookie_cartList;
        } else {
            //已经登录；从redis中查询购物车数据
            List<Cart> redis_cartList = cartService.findCartListInRedisByUsername(username);

            //合并购物车数据
            if (cookie_cartList != null && cookie_cartList.size() > 0) {
                redis_cartList = cartService.mergeCartList(cookie_cartList, redis_cartList);

                //将最新的购物车数据写回redis
                cartService.saveCartListToRedis(redis_cartList, username);

                //删除cookie中的购物车数据
                CookieUtils.deleteCookie(request, response, COOKIE_CART_LIST);
            }

            return redis_cartList;
        }

    }

    /**
     * 获取用户名
     *
     * @return
     */
    @GetMapping("/getUsername")
    public Map<String, Object> getUsername() {
        Map<String, Object> map = new HashMap<String, Object>();

        //如果是配置了IS_AUTHENTICATED_ANONYMOUSLY则在没有登录的情况下返回的username的值为anonymousUser
        //如果注册的时候用户名就为anonymousUser；则如何区分是否已经登录？
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        map.put("username", username);

        return map;
    }
}
