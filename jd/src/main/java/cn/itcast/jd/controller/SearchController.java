package cn.itcast.jd.controller;

import cn.itcast.jd.pojo.Result;
import cn.itcast.jd.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/search")
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;


    /**
     * 根据搜索关键字搜索solr中的商品
     * @param queryString 搜索关键字
     * @param catalog_name 分类
     * @param price 价格；如：0-9
     * @param page 页号
     * @param sort 排序；1升序，0降序
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(String queryString, String catalog_name, String price,
                             @RequestParam(value = "page", defaultValue = "1") Integer page, String sort){
        ModelAndView mv = new ModelAndView("product_list");
        try {
            Result result = searchService.search(queryString, catalog_name, price, page, sort);
            //查询关键字
            mv.addObject("queryString", queryString);
            //分类
            mv.addObject("catalog_name", catalog_name);
            //价格
            mv.addObject("price", price);
            //分页对象
            mv.addObject("result", result);
            //排序
            mv.addObject("sort", sort);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }
}
