package cn.itcast.lucene;

import cn.itcast.dao.BookDao;
import cn.itcast.dao.impl.BookDaoImpl;
import cn.itcast.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IndexSearchTest {

    /**
     * 高亮查询
     */
    @Test
    public void highlightQuery() throws Exception {
        //1、创建分词器analyzer
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        //2、创建查询对象Query(queryParser-->Query)
        QueryParser queryParser = new QueryParser("bookName", analyzer);
        Query query = queryParser.parse("bookName:java");

        //查询评分对象
        QueryScorer queryScorer = new QueryScorer(query);

        //创建分片片段
        SimpleSpanFragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);

        //创建高亮对象；默认如果不使用高亮的标签则为<B>
        //Highlighter highlighter = new Highlighter(queryScorer);

        /**
         * 创建自定义的高亮标签
         * 参数1：开始标签
         * 参数2：结束标签
         */
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font style='color:red'>", "</font>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, queryScorer);
        highlighter.setTextFragmenter(fragmenter);

        //3、创建、指定索引库目录Directory
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));

        //4、创建索引读取对象IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);

        //5、创建索引搜索对象IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //6、搜索数据并处理返回的结果（根据文档id，查询对应的文档并输出文档中的各个域）
        /**
         * 参数1：查询对象（设置查询条件）
         * 参数2：查询符合本次搜索关键字的前n条记录
         */
        TopDocs topDocs = indexSearcher.search(query, 10);

        System.out.println("符合本次搜索关键字的总文档数（命中数）为：" + topDocs.totalHits);

        //获取本次查询的文档id集合
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //遍历每个文档的id，再根据文档id到索引目录查询文档
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            System.out.println("文档在lucene中的id为：" + scoreDoc.doc);

            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("bookId为：" + document.get("bookId"));
            String bookName = document.get("bookName");
            System.out.println("bookName为：" + bookName);


            TokenStream tokenStream = TokenSources.getTokenStream(document, "bookName", analyzer);
            String highlighterBestFragment = highlighter.getBestFragment(tokenStream, bookName);
            System.out.println("高亮后的标题为：" + highlighterBestFragment);

            System.out.println("bookDesc为：" + document.get("bookDesc"));
            System.out.println("bookPrice为：" + document.get("bookPrice"));
            System.out.println("bookPic为：" + document.get("bookPic"));
            System.out.println("------------------------------------------");
        }

        //7、释放资源
        indexReader.close();
    }


    @Test
    public void queryParser() throws Exception{
        IKAnalyzer analyzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser("bookName", analyzer);

        //bookName:test AND price:{10 TO 100]

        Query query = queryParser.parse("bookName:java AND bookName:lucene");

        searchIndex(query);
    }

    /**
     * 组合查询
     * @throws Exception
     */
    @Test
    public void booleanQuery() throws Exception{
        /**
         * 参数1：查询的域名称
         * 参数2：起始区间
         * 参数3：结束区间
         * 参数4：是否要包含起始区间端点
         * 参数5：是否要包含结束区间端点
         */
        NumericRangeQuery<Float> query1 =
                NumericRangeQuery.newFloatRange("bookPrice", 80f, 100f, true, false);
        //query = bookPrice:[80.0 TO 100.0}


        //must表示两个条件必须同时成立
        BooleanQuery query = new BooleanQuery();
        query.add(query1, BooleanClause.Occur.MUST);

        TermQuery query2 = new TermQuery(new Term("bookName", "java"));
        query.add(query2, BooleanClause.Occur.MUST);

        //query = +bookPrice:[80.0 TO 100.0} +bookName:java
        searchIndex(query);
    }

    /**
     * 数值范围查询
     * @throws Exception
     */
    @Test
    public void numericRangeQuery() throws Exception{
        /**
         * 参数1：查询的域名称
         * 参数2：起始区间
         * 参数3：结束区间
         * 参数4：是否要包含起始区间端点
         * 参数5：是否要包含结束区间端点
         */
        NumericRangeQuery<Float> query =
                NumericRangeQuery.newFloatRange("bookPrice", 80f, 100f, true, false);
        //query = bookPrice:[80.0 TO 100.0}
        searchIndex(query);
    }

    @Test
    public void termQuery() throws Exception{
        TermQuery query = new TermQuery(new Term("bookName", "java"));
        searchIndex(query);
    }


    /**
     * 将索引库中的索引数据通过Lucene的api查询到
     */
    private void searchIndex(Query query) throws Exception {
        System.out.println("query = " + query);
        //1、创建分词器analyzer
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        //2、创建查询对象Query(queryParser-->Query)

        //3、创建、指定索引库目录Directory
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));

        //4、创建索引读取对象IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);

        //5、创建索引搜索对象IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //6、搜索数据并处理返回的结果（根据文档id，查询对应的文档并输出文档中的各个域）
        /**
         * 参数1：查询对象（设置查询条件）
         * 参数2：查询符合本次搜索关键字的前n条记录
         */
        TopDocs topDocs = indexSearcher.search(query, 10);

        System.out.println("符合本次搜索关键字的总文档数（命中数）为：" + topDocs.totalHits);

        //获取本次查询的文档id集合
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //遍历每个文档的id，再根据文档id到索引目录查询文档
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            System.out.println("文档在lucene中的id为：" + scoreDoc.doc);

            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("bookId为：" + document.get("bookId"));
            System.out.println("bookName为：" + document.get("bookName"));
            System.out.println("bookDesc为：" + document.get("bookDesc"));
            System.out.println("bookPrice为：" + document.get("bookPrice"));
            System.out.println("bookPic为：" + document.get("bookPic"));
            System.out.println("------------------------------------------");
        }

        //7、释放资源
        indexReader.close();
    }


}
