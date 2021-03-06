package cn.itcast.solr;

import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    //多条件查询
    @Test
    public void testMultiQuery(){
        SimpleQuery query = new SimpleQuery();

        //参数1：在schema.xml文件中对应的编写了的域名；contains表示查询该域包含相应关键字的那些文档是不会分词的；is会分词
        Criteria criteria1 = new Criteria("item_title").is("魅族");
        query.addCriteria(criteria1);

        Criteria criteria2 = new Criteria("item_price").greaterThanEqual(500);
        query.addCriteria(criteria2);


        //查询；参数1：查询对象，参数2：是返回结果中的每个文档封装的实体类
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);

        showMsg(scoredPage);
    }

    @Test
    public void testSimpleQuery(){
        SimpleQuery query = new SimpleQuery("item_title:魅族");

        //设置分页
        query.setOffset(0);// 起始索引号
        query.setRows(20);// 本次要查询多少条

        //查询；参数1：查询对象，参数2：是返回结果中的每个文档封装的实体类
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);

        showMsg(scoredPage);
    }

    private void showMsg(ScoredPage<TbItem> scoredPage) {
        System.out.println("总记录数：" + scoredPage.getTotalElements());
        System.out.println("总页数数：" + scoredPage.getTotalPages());

        for (TbItem item : scoredPage) {
            System.out.println("id=" + item.getId());
            System.out.println("title=" + item.getTitle());
            System.out.println("price=" + item.getPrice());
            System.out.println("image=" + item.getImage());
            System.out.println("更新时间=" + item.getUpdateTime());
        }
    }

    @Test
    public void testDeleteByQuery() {
        //根据条件删除；参数：查询表达式
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);

        solrTemplate.commit();
    }

    @Test
    public void testDeleteById() {
        solrTemplate.deleteById("5424543");
        solrTemplate.commit();
    }

    @Test
    public void testAddOrUpdate(){
        //创建要添加到solr中的对象，该对象中的属性应该添加solr的注解
        TbItem item = new TbItem();
        item.setId(5424543L);
        item.setTitle("222 魅族 魅蓝 Note6 4GB+32GB 全网通公开版 香槟金 移动联通电信4G手机 双卡双待");
        item.setPrice(new BigDecimal(899));
        item.setUpdateTime(new Date());
        item.setSellerId("meizu");
        item.setImage("https://item.jd.com/5424543.html");

        solrTemplate.saveBean(item);

        solrTemplate.commit();
    }

}
