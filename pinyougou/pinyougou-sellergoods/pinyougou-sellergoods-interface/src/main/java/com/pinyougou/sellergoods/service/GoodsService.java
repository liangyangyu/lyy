package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;

public interface GoodsService extends BaseService<TbGoods> {

    PageResult search(Integer page, Integer rows, TbGoods goods);

    /**
     * 保存商品基本、描述、sku列表
     * @param goods 商品信息
     */
    void addGoods(Goods goods);

    /**
     * 根据商品id查询商品基本、描述信息、sku列表
     * @param id 商品id
     * @return 商品信息
     */
    Goods findGoodsById(Long id);

    /**
     * 更新商品基本、描述信息、sku列表
     * @param goods 商品信息
     */
    void updateGoods(Goods goods);

    /**
     * 根据商品id集合更新对应的商品的状态
     * @param ids 商品id集合
     * @param status 商品的状态
     */
    void updateStatus(Long[] ids, String status);

    /**
     * 更新商品id集合对应的那些商品的是否删除状态字段的值为1
     * @param ids 商品id集合
     */
    void deleteGoodsByIds(Long[] ids);
}