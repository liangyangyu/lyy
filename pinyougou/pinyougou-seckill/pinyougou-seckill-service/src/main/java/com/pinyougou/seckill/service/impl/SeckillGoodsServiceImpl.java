package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = SeckillGoodsService.class)
public class SeckillGoodsServiceImpl extends BaseServiceImpl<TbSeckillGoods> implements SeckillGoodsService {

    //秒杀商品在redis中的key的名称
    private static final String SECKILL_GOODS = "SECKILL_GOODS";
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult search(Integer page, Integer rows, TbSeckillGoods seckillGoods) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbSeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(seckillGoods.get***())){
            criteria.andLike("***", "%" + seckillGoods.get***() + "%");
        }*/

        List<TbSeckillGoods> list = seckillGoodsMapper.selectByExample(example);
        PageInfo<TbSeckillGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<TbSeckillGoods> findList() {

        List<TbSeckillGoods> seckillGoodsList = null;

        //从redis中查询秒杀商品列表；如果查询到则直接返回，如果查询不到则从mysql中查询后存入redis
        //秒杀商品id集合
        //redisTemplate.boundHashOps(SECKILL_GOODS).keys();
        //秒杀商品列表
        try {
            seckillGoodsList = redisTemplate.boundHashOps(SECKILL_GOODS).values();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(seckillGoodsList == null || seckillGoodsList.size() == 0) {
            //本方法要执行；
            // 如：select * from tb_seckill_goods where status='1' and stock_count > 0 and start_time<=? and end_time > ? order by start_time

            Example example = new Example(TbSeckillGoods.class);

            Example.Criteria criteria = example.createCriteria();

            //状态为审核通过
            criteria.andEqualTo("status", "1");
            //库存量大于0
            criteria.andGreaterThan("stockCount", 0);
            //开始时间小于等于当前时间
            criteria.andLessThanOrEqualTo("startTime", new Date());
            //结束时间大于当前时间
            criteria.andGreaterThan("endTime", new Date());

            //根据开始时间升序排序
            example.orderBy("startTime");

            seckillGoodsList = seckillGoodsMapper.selectByExample(example);

            try {
                //存入redis
                for (TbSeckillGoods seckillGoods : seckillGoodsList) {
                    redisTemplate.boundHashOps(SECKILL_GOODS).put(seckillGoods.getId().toString(), seckillGoods);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("从缓存中读取了秒杀商品列表...");
        }

        return seckillGoodsList;
    }
}
