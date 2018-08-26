package cn.itcast.dao;

import cn.itcast.pojo.Book;

import java.util.List;

public interface BookDao {
    List<Book> queryBookList();
}
