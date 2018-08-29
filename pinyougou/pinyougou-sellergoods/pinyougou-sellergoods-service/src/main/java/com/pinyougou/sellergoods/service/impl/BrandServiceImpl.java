package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

//Servive注解来自阿里的注解
@Service(interfaceClass = BrandService.class)
public class BrandServiceImpl extends BaseServiceImpl<TbBrand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<TbBrand> queryAll() {
        return brandMapper.queryAll();
    }

    @Override
    @Deprecated
    public List<TbBrand> testPage(Integer page, Integer rows) {
        //设置分页;  limit offset,rows
        PageHelper.startPage(page, rows);

        return brandMapper.selectAll();
    }

    @Override
    public PageResult search(TbBrand brand, Integer page, Integer rows) {

        //分页查询
        PageHelper.startPage(page, rows);

        //创建一个查询对象
        Example example = new Example(TbBrand.class);

        //创建一个查询条件对象
        Example.Criteria criteria = example.createCriteria();

        //根据首字母查询
        if(!StringUtils.isEmpty(brand.getFirstChar())){
            criteria.andEqualTo("firstChar", brand.getFirstChar());
        }

        //根据品牌名称模糊查询
        if(!StringUtils.isEmpty(brand.getName())){
            criteria.andLike("name", "%" + brand.getName() + "%");
        }

        //根据条件查询
        List<TbBrand> list = brandMapper.selectByExample(example);

        //转换为分页信息对象
        PageInfo<TbBrand> pageInfo = new PageInfo<>(list);

        //创建返回结果
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
