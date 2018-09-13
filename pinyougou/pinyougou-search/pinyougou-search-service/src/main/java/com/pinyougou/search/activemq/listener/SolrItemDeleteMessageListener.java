package com.pinyougou.search.activemq.listener;

import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 在运营商管理后台中如果删除商品之后应该发送商品的id集合到MQ；搜索系统监听MQ并接收商品id集合，根据商品id集合删除solr中的商品数据
 */
public class SolrItemDeleteMessageListener extends AbstractAdaptableMessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;

            //1、获取消息（商品id集合）
            Long[] ids = (Long[]) objectMessage.getObject();

            //2、将solr的商品数据删除
            itemSearchService.deleteItemByGoodsIds(Arrays.asList(ids));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
