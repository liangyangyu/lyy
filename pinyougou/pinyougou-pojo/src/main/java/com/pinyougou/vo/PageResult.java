package com.pinyougou.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 因为以后需要将该对象进行网络传递，所以必须实现序列化接口
 * 序列化：将一个java对象转换为一个特有格式的字符串
 * 反序列化：将一个有特定格式的字符串转换为一个java对象（自定义对象，Map,List。。。）
 */
public class PageResult implements Serializable {
    //总记录数
    private Long total;
    //记录列表；?表示占位符类似泛型，除此以外；使用了?的该属性在赋值以后不可以改变其值
    private List<?> rows;

    public PageResult() {
    }

    public PageResult(Long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
