package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

public interface BrandService {
    //查询品牌
    List<TbBrand> queryAll();

    /**
     * 根据分页信息分页查询品牌数据
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    List<TbBrand> testPage(Integer page, Integer rows);
}
