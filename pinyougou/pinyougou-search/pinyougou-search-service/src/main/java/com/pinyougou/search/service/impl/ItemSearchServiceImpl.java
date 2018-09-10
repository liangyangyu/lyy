package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //创建查询对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //设置查询条件
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //设置高亮域
        HighlightOptions highlightOptions = new HighlightOptions();
        //高亮域名称
        highlightOptions.addField("item_title");

        //设置高亮的起始标签
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        //设置高亮的结束标签
        highlightOptions.setSimplePostfix("</em>");

        query.setHighlightOptions(highlightOptions);

        //根据商品分类过滤查询：在查询条件的结果下应用过滤条件进行过滤
        if(!StringUtils.isEmpty(searchMap.get("category"))){
            Criteria categoryCriteria = new Criteria("item_category").is(searchMap.get("category"));
            SimpleFilterQuery categoryFilterQuery = new SimpleFilterQuery(categoryCriteria);
            query.addFilterQuery(categoryFilterQuery);
        }

        //根据品牌过滤查询：
        if(!StringUtils.isEmpty(searchMap.get("brand"))){
            Criteria brandCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            SimpleFilterQuery brandFilterQuery = new SimpleFilterQuery(brandCriteria);
            query.addFilterQuery(brandFilterQuery);
        }

        //根据规格过滤查询
        if (searchMap.get("spec") != null) {
            Map<String, String> specMap = (Map<String, String>)searchMap.get("spec");

            Set<Map.Entry<String, String>> entries = specMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                //在schema.xml文件中定义的域名称为：item_spec_* --》 item_spec_网络
                Criteria specCriteria = new Criteria("item_spec_" + entry.getKey()).is(entry.getValue());
                SimpleFilterQuery specFilterQuery = new SimpleFilterQuery(specCriteria);
                query.addFilterQuery(specFilterQuery);
            }
        }

        //根据价格过滤查询：
        if(!StringUtils.isEmpty(searchMap.get("price"))){

            String[] prices = searchMap.get("price").toString().split("-");

            //价格起始区间
            Criteria startCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
            SimpleFilterQuery startFilterQuery = new SimpleFilterQuery(startCriteria);
            query.addFilterQuery(startFilterQuery);

            //价格结束区间；如果不为*的话
            if (!"*".equals(prices[1])) {
                Criteria endCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                SimpleFilterQuery endFilterQuery = new SimpleFilterQuery(endCriteria);
                query.addFilterQuery(endFilterQuery);
            }
        }

        //查询
        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //获取高亮的标题
        List<HighlightEntry<TbItem>> highlighted = highlightPage.getHighlighted();

        //对每个商品的标题获取高亮标题并回填

        if(highlighted != null && highlighted.size() > 0) {
            for (HighlightEntry<TbItem> entry : highlighted) {
                if (entry.getHighlights() != null && entry.getHighlights().size() > 0) {
                    entry.getEntity().setTitle(entry.getHighlights().get(0).getSnipplets().get(0));
                }
            }
        }

        //设置返回结果
        resultMap.put("rows", highlightPage.getContent());

        return resultMap;
    }
}
