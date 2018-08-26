package cn.itcast.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 利用solrj操作solr
 */
public class SolrjQueryTest {

    @Test
    public void test() throws Exception{
        //创建httpSolrServer
        HttpSolrServer httpSolrServer = new HttpSolrServer("http://localhost:8080/solr");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.set("q", "彩色");

        //设置过滤条件；也就是在查询条件的结果下再执行如下的过滤条件
        //查询product_name包含彩色的商品，然后再返回的结果中查询价格大于等于3小于5的那些商品
        solrQuery.setFilterQueries("product_price:[3 TO 5}");

        //设置根据价格降序排序
        solrQuery.setSort("product_price", SolrQuery.ORDER.desc);

        //设置分页
        solrQuery.setStart(0);//起始索引号
        solrQuery.setRows(5);//页大小

        //设置要返回文档中包含哪些域
        solrQuery.set("fl", "id,product_name,product_catalog_name,product_price");

        //设置默认的查询域
        solrQuery.set("df", "product_name");

        //设置返回数据格式（json/xml）
        solrQuery.set("wt", "json");

        //设置高亮；是否需要高亮某个域中的搜索关键字
        solrQuery.setHighlight(true);
        //设置高亮的域名
        solrQuery.addHighlightField("product_name");
        //高亮的起始标签
        solrQuery.setHighlightSimplePre("<font style='color:red'>");
        //高亮的结束标签
        solrQuery.setHighlightSimplePost("</font>");

        //设置分片统计
        solrQuery.setFacet(true);
        //设置分片统计的查询条件
        solrQuery.addFacetQuery("product_name:彩色");
        //设置分片的域（相当于按照该域进行分组）
        solrQuery.addFacetField("product_catalog_name");

        //查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);

        SolrDocumentList results = queryResponse.getResults();

        System.out.println("符合本次查询的总记录数为：" + results.getNumFound());

        //处理分片结果
        List<FacetField> facetFields = queryResponse.getFacetFields();
        for (FacetField facetField : facetFields) {
            System.out.println("分片的域名称为：" + facetField.getName() + "；本次总分片的数据总数为：" + facetField.getValueCount());
            //获取本次分片的所有分片统计情况
            List<FacetField.Count> counts = facetField.getValues();
            for (FacetField.Count count : counts) {
                System.out.println("分片的名称为："+ count.getName() + "；该分片对应的统计数为:" + count.getCount());
            }
        }

        //处理高亮内容；获取高亮的返回结果
        /**
         * "highlighting": {
         *     "771": {
         *       "product_name": [
         *         "韩国文具  苏格兰风情<em>彩色</em>铅笔12支装2909"
         *       ]
         *     },。。。}
         */
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        for (SolrDocument solrDocument : results) {
            System.out.println("id=" + solrDocument.get("id"));
            System.out.println("product_name=" + solrDocument.get("product_name"));

            System.out.println("高亮的标题为：" + highlighting.get(solrDocument.get("id").toString()).get("product_name").get(0));

            System.out.println("product_catalog_name=" + solrDocument.get("product_catalog_name"));
            System.out.println("product_price=" + solrDocument.get("product_price"));
            System.out.println("--------------------------------------------------");
        }
    }
}
