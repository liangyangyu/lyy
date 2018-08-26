package cn.itcast.jd.service.impl;

import cn.itcast.jd.pojo.Product;
import cn.itcast.jd.pojo.Result;
import cn.itcast.jd.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private HttpSolrServer httpSolrServer;

    public Result search(String queryString, String catalog_name, String price, Integer page, String sort) throws SolrServerException {
        Result result = new Result();

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();

        if (StringUtils.isNotBlank(queryString)) {
            solrQuery.setQuery(queryString);
        } else {
            solrQuery.setQuery("*:*");
        }

        //设置查询的默认域
        solrQuery.set("df", "product_keywords");

        //设置分类过滤
        if (StringUtils.isNotBlank(catalog_name)) {
            catalog_name = "product_catalog_name:" + catalog_name;
        }

        //设置价格过滤
        if (StringUtils.isNotBlank(price)) {
            //分隔价格
            String[] prices = price.split("-");

            price = "product_price:[" + prices[0] + " TO " + prices[1] + "]";
        }

        solrQuery.addFilterQuery(catalog_name, price);

        //设置分页
        int pageSize = 10;
        solrQuery.setStart((page-1)*pageSize);
        solrQuery.setRows(pageSize);

        result.setCurPage(page);

        //排序
        if ("1".equals(sort)) {
            solrQuery.setSort("price", SolrQuery.ORDER.asc);
        } else {
            solrQuery.setSort("price", SolrQuery.ORDER.desc);
        }

        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("product_name");
        solrQuery.setHighlightSimplePre("<font style='color:red'>");
        solrQuery.setHighlightSimplePost("</font>");


        //查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);

        SolrDocumentList results = queryResponse.getResults();

        //总记录数
        long totalCount = results.getNumFound();
        result.setRecordCount(totalCount);

        //计算总页数
        int pageCount = (int)totalCount/pageSize;
        if (totalCount % pageSize != 0) {
            pageCount++;
        }
        result.setPageCount(pageCount);

        List<Product> productList = new ArrayList<Product>();
        Product product = null;

        //获取高亮标题集合
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        for (SolrDocument solrDocument : results) {
            product = new Product();
            String id = solrDocument.get("id").toString();
            product.setPid(id);
            product.setPicture(solrDocument.get("product_picture").toString());
            product.setName(solrDocument.get("product_name").toString());

            List<String> list = highlighting.get(id).get("product_name");
            if (list != null && list.size() > 0) {
                product.setName(list.get(0));
            }


            product.setPrice(solrDocument.get("product_price").toString());
            productList.add(product);
        }
        result.setProductList(productList);

        return result;
    }
}
