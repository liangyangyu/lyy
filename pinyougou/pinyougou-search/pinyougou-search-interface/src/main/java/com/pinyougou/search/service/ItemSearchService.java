package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
    /**
     * 根据搜索条件查询商品
     * @param searchMap 搜索条件对象
     * @return 返回结果对象
     */
    Map<String, Object> search(Map<String, Object> searchMap);

    /**
     * 更新商品列表
     * @param itemList 商品列表
     */
    void importItemList(List<TbItem> itemList);

    /**
     * 根据商品spu id集合删除solr中商品数据
     * @param goodsIds 商品spu id集合
     */
    void deleteItemByGoodsIds(List<Long> goodsIds);
}
