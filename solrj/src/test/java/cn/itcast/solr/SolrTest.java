package cn.itcast.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

public class SolrTest {

    private HttpSolrServer httpSolrServer;

    @Before
    public void setup(){
        //默认操作的为collection1
        String url = "http://localhost:8080/solr";
        //创建httpSolrServer
        httpSolrServer = new HttpSolrServer(url);
    }

    @Test
    public void searchByCollection() throws Exception {

        httpSolrServer = new HttpSolrServer("http://localhost:8080/solr/collection2");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery("*:*");

        //执行查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);

        //输出本次查询的总记录数
        System.out.println("本次查询的总记录数为：" + queryResponse.getResults().getNumFound());

        //遍历返回的文档集合
        SolrDocumentList results = queryResponse.getResults();
        for (SolrDocument solrDocument : results) {
            System.out.println("id为：" + solrDocument.get("id"));
            System.out.println("title为：" + solrDocument.get("title"));
        }

    }

    @Test
    public void search() throws Exception {

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery("*:*");

        //执行查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);

        //输出本次查询的总记录数
        System.out.println("本次查询的总记录数为：" + queryResponse.getResults().getNumFound());

        //遍历返回的文档集合
        SolrDocumentList results = queryResponse.getResults();
        for (SolrDocument solrDocument : results) {
            System.out.println("id为：" + solrDocument.get("id"));
            System.out.println("title为：" + solrDocument.get("title"));
        }

    }

    /**
     * 根据条件删除
     * @throws Exception
     */
    @Test
    public void deleteByQuery() throws Exception {
        httpSolrServer.deleteByQuery("title:smartisan");
        httpSolrServer.commit();
    }

    @Test
    public void deleteById() throws Exception {
        httpSolrServer.deleteById("8578888");
        httpSolrServer.commit();
    }

    /**
     * 如果存在则更新，如果不存在则新增
     * @throws Exception
     */
    @Test
    public void addOrUpdate() throws Exception{
        //1、创建一个输入文档
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("id", 8578888);
        solrInputDocument.setField("title", "333 锤子（smartisan ) 坚果 Pro 2S 6G+64GB 碳黑色（细红线版）全面屏双摄 全网通4G手机 双卡双待 游戏手机");

        //2、新增或更新文档
        httpSolrServer.add(solrInputDocument);

        //3、提交
        httpSolrServer.commit();
    }

}
