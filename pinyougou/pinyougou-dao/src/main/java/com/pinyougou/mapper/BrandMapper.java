package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<TbBrand> {

    //即使使用了通用Mapper也不影响原有的方法（xml）
    List<TbBrand> queryAll();
}
