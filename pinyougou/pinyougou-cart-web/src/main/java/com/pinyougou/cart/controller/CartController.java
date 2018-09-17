package com.pinyougou.cart.controller;

import com.alibaba.fastjson.JSONArray;
import com.pinyougou.common.util.CookieUtils;
import com.pinyougou.vo.Cart;
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

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 查询登录或者未登录情况下购物车列表数据
     * @return 购物车列表
     */
    @GetMapping("/findCartList")
    public List<Cart> findCartList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ("anonymousUser".equals(username)) {
            //未登录；从cookie中查询购物车数据
            List<Cart> cookie_cartList = new ArrayList<>();
            //1、获取cookie中购物车数据
            String cartListJsonStr = CookieUtils.getCookieValue(request, COOKIE_CART_LIST, true);

            //2、将cookie的购物车json格式字符串转换为集合
            if(!StringUtils.isEmpty(cartListJsonStr)){
                cookie_cartList = JSONArray.parseArray(cartListJsonStr, Cart.class);
            }

            return cookie_cartList;
        } else {
            //已经登录；从redis中查询购物车数据
        }

        return null;
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
