package com.pinyougou.search.activemq.listener;

import com.alibaba.fastjson.JSONArray;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.List;

/**
 * 在运营商管理后台中如果审核通过商品之后应该发送商品变更的消息（商品sku列表）在搜索系统接收消息并将数据保存到solr中
 */
public class SolrItemQueueMessageListener extends AbstractAdaptableMessageListener {


    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        try {
            //1、接收消息
            TextMessage textMessage = (TextMessage) message;

            //将json格式字符串转换为java集合对象
            List<TbItem> itemList = JSONArray.parseArray(textMessage.getText(), TbItem.class);

            //2、处理消息；把商品数据同步保存到solr中
            itemSearchService.importItemList(itemList);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
