package cn.itcast.solr;

import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

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
