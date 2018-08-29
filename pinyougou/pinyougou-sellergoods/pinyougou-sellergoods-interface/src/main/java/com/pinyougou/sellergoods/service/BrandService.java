package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

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
}
