package com.pinyougou.search.service;

import java.util.Map;

public interface ItemSearchService {
    /**
     * 根据搜索条件查询商品
     * @param searchMap 搜索条件对象
     * @return 返回结果对象
     */
    Map<String, Object> search(Map<String, Object> searchMap);
}
