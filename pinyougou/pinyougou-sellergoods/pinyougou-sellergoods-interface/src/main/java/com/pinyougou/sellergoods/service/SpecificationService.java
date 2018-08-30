package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Specification;

public interface SpecificationService extends BaseService<TbSpecification> {

    PageResult search(Integer page, Integer rows, TbSpecification specification);

    /**
     * 保存规格及其选项到数据库中
     * @param specification 规格及规格选项列表
     */
    void add(Specification specification);

    /**
     * 根据规格id 查询规格及选项列表
     * @param id 规格id
     * @return 规格及选项列表
     */
    Specification findOne(Long id);

    /**
     * 更新规格及其选项到数据库中
     * @param specification 规格及规格选项列表
     */
    void update(Specification specification);
}