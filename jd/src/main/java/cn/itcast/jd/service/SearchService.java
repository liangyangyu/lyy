package cn.itcast.jd.service;

import cn.itcast.jd.pojo.Result;
import org.apache.solr.client.solrj.SolrServerException;

public interface SearchService {

     /**
     * 根据搜索关键字搜索solr中的商品
     * @param queryString 搜索关键字
     * @param catalog_name 分类
     * @param price 价格；如：0-9
     * @param page 页号
     * @param sort 排序；1升序，0降序
     * @return 分页对象
     */
    Result search(String queryString, String catalog_name, String price, Integer page, String sort) throws SolrServerException;
}
