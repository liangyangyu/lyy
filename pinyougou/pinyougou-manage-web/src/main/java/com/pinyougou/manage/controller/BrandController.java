package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/brand")
//@Controller
@RestController //组合注解；包括了Controller ResponseBody两个注解；对该类的所有方法生效
public class BrandController {

    /**
     * 从注册中心获取该对象；在配置文件中已经指定了注册中心
     */
    @Reference(timeout = 10000)
    private BrandService brandService;

    /**
     * 获取并输出所有品牌列表
     * @return
     */
    @GetMapping("/findAll")
    //@RequestMapping(value = "/findAll", method =  RequestMethod.GET)
    //@ResponseBody
    public List<TbBrand> findAll(){
        //return brandService.queryAll();
        return brandService.findAll();
    }

    /**
     * 根据分页信息分页查询品牌数据
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    @GetMapping("/testPage")
    public List<TbBrand> testPage(@RequestParam(value="page", defaultValue = "1")Integer page,
                                  @RequestParam(value="rows", defaultValue = "5")Integer rows){
        //return brandService.testPage(page, rows);
        return (List<TbBrand>) brandService.findByPage(page, rows).getRows();
    }
}
