package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService extends BaseService<TbTypeTemplate> {

    PageResult search(Integer page, Integer rows, TbTypeTemplate typeTemplate);

    /**
     * 根据分类模版id查询其信息和选项
     * @param id 分类模版id
     * @return 结构：[{"id":27,"text":" 网 络 ","options":[{"id":111,"optionName":" 移 动
     * 3G","specId":27,"orders":null}]},{"id":32,"text":" 机 身 内 存
     * ","options":[{"id":222,"optionName":"16G","specId":32,"orders":null},{"id":223,"
     * optionName":"32G","specId":32,"orders":null}]}]
     */
    List<Map> findSpecList(Long id);
}