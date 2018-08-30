package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService extends BaseService<TbBrand> {
    //查询品牌
    List<TbBrand> queryAll();

    /**
     * 根据分页信息分页查询品牌数据
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    @Deprecated
    List<TbBrand> testPage(Integer page, Integer rows);

    /**
     * 根据条件分页查询
     * @param brand 查询条件对象
     * @param page 页号
     * @param rows 页大小
     * @return 分页结果
     */
    PageResult search(TbBrand brand, Integer page, Integer rows);

    /**
     * 查询数据库中的所有品牌；并返回一个集合，集合中的数据结构如下：
     *
     * @return [{id:'1',text:'联想'},{id:'2',text:'华为'}]
     */
    List<Map<String, Object>> selectOptionList();
}
