package cn.itcast.lucene;

import cn.itcast.dao.BookDao;
import cn.itcast.dao.impl.BookDaoImpl;
import cn.itcast.pojo.Book;
import com.sun.javafx.util.TempState;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IndexManagerTest {

    /**
     * 修改索引数据
     */
    @Test
    public void updateIndexBoost() throws Exception {
        //创建分词器
        Analyzer analyzer = new IKAnalyzer();
        //指定索引库目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));
        //创建indexWriterConfig
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
        //创建IndexWriter
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        /**
         * 参数1：更新修改的条件
         * 参数2：要修改成为的文档；只要符合条件的那些文档都会被修改为该文档
         * 如果文档存在则修改；不存在则新增
         */
        Term term = new Term("bookId", "5");

        Document document = new Document();
        document.add(new StringField("bookId", "5", Field.Store.YES));
        TextField textField = new TextField("bookName", "Lucene Java精华版", Field.Store.YES);
        //设置权重
        textField.setBoost(2.0f);
        document.add(textField);

        //根据条件修改
        indexWriter.updateDocument(term, document);
        //释放资源
        indexWriter.close();
    }


    /**
     * 修改索引数据
     */
    @Test
    public void updateIndex() throws Exception {
        //创建分词器
        Analyzer analyzer = new IKAnalyzer();
        //指定索引库目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));
        //创建indexWriterConfig
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
        //创建IndexWriter
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        /**
         * 参数1：更新修改的条件
         * 参数2：要修改成为的文档；只要符合条件的那些文档都会被修改为该文档
         * 如果文档存在则修改；不存在则新增
         */
        Term term = new Term("name", "mybatis");

        Document document = new Document();
        document.add(new StringField("id", "123", Field.Store.YES));
        document.add(new TextField("name", "44 spring springmvc 222", Field.Store.YES));

        //根据条件修改
        indexWriter.updateDocument(term, document);
        //释放资源
        indexWriter.close();
    }

    /**
     * 删除全部索引数据
     */
    @Test
    public void deleteAll() throws Exception {
        //创建分词器
        Analyzer analyzer = new IKAnalyzer();
        //指定索引库目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));
        //创建indexWriterConfig
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
        //创建IndexWriter
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //删除
        indexWriter.deleteAll();
        //释放资源
        indexWriter.close();
    }

    /**
     * 根据词条删除索引数据
     */
    @Test
    public void deleteByTerm() throws Exception {
        //创建分词器
        Analyzer analyzer = new IKAnalyzer();
        //指定索引库目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));
        //创建indexWriterConfig
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
        //创建IndexWriter
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        //创建词条
        Term term = new Term("bookName", "java");
        //删除
        indexWriter.deleteDocuments(term);
        //释放资源
        indexWriter.close();
    }

    /**
     * 将索引库中的索引数据通过Lucene的api查询到
     */
    @Test
    public void searchIndex() throws Exception {
        //1、创建分词器analyzer
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        //2、创建查询对象Query(queryParser-->Query)
        QueryParser queryParser = new QueryParser("bookName", analyzer);
        Query query = queryParser.parse("bookName:java");

        //3、创建、指定索引库目录Directory
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));

        //4、创建索引读取对象IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);

        //5、创建索引搜索对象IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //设置分页；在lucene中是伪分页（实际上是查询了前n条，再对这些数据遍历返回需要的数据） limit 起始索引号，页大小
        //页号
        int pageNo = 1;
        //页大小
        int pageSize = 20;
        //分页起始索引号；（页号-1）*页大小
        int start = (pageNo - 1) * pageSize;
        //分页结束索引号
        int end = start + pageSize;

        //6、搜索数据并处理返回的结果（根据文档id，查询对应的文档并输出文档中的各个域）
        /**
         * 参数1：查询对象（设置查询条件）
         * 参数2：查询符合本次搜索关键字的前n条记录
         */
        TopDocs topDocs = indexSearcher.search(query, end);

        System.out.println("符合本次搜索关键字的总文档数（命中数）为：" + topDocs.totalHits);

        //获取本次查询的文档id集合
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        if (end > topDocs.totalHits) {
            end= topDocs.totalHits;
        }

        //遍历每个文档的id，再根据文档id到索引目录查询文档
        for (int i = start; i < end; i++) {
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

    /**
     * 将数据库中的那些数据获取到并将其利用lucene的api生成索引并保存到索引库中
     */
    @Test
    public void createIndex() throws Exception {
        //1、获取数据
        BookDao bookDao = new BookDaoImpl();
        List<Book> bookList = bookDao.queryBookList();
        //2、将数据转换为lucene可以接受的文档（文档列表）
        List<Document> docList = new ArrayList<Document>();
        Document doc = null;
        for (Book book : bookList) {
            doc = new Document();

            /**
             * bookId文档id
             * 是否要分词：否
             * 是否要索引：是
             * 是否要存储：是
             * 可以使用：StringField
             */
            doc.add(new StringField("bookId", book.getId() + "", Field.Store.YES));

            /**
             *bookName 标题、书名
             * 是否要分词：是
             * 是否要索引：是
             * 是否要存储：是
             * 可以使用：TextField
             */
            doc.add(new TextField("bookName", book.getBookName(), Field.Store.YES));
            /**
             *bookDesc 描述
             * 是否要分词：是
             * 是否要索引：是
             * 是否要存储：否
             * 可以使用：TextField
             */
            doc.add(new TextField("bookDesc", book.getBookDesc(), Field.Store.YES));

            /**
             *bookPrice 价格
             * 是否要分词：是
             * 是否要索引：是
             * 是否要存储：是
             * 可以使用：FloatField
             */
            doc.add(new FloatField("bookPrice", book.getPrice(), Field.Store.YES));

            /**
             * bookPic 图片地址
             * 是否要分词：否
             * 是否要索引：否
             * 是否要存储：是
             * 可以使用：StoreField
             */
            doc.add(new StoredField("bookPic", book.getPic()));
            docList.add(doc);
        }
        //3、创建文档的分词器analyzer
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();

        //4、创建索引存放目录directory
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));

        //5、创建索引编写器配置对象IndexWriterConfig
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //6、创建索引编写器IndexWriter
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //7、遍历文档列表将文档写入索引目录
        for (Document document : docList) {
            indexWriter.addDocument(document);
        }

        //8、释放资源
        indexWriter.close();
    }
}
