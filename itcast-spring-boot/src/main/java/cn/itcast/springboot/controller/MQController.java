package cn.itcast.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/mq")
@RestController
public class MQController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @GetMapping("/send")
    public String sendMapMsg(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", 123);
        map.put("name", "传智播客");

        //默认使用的是队列模式
        jmsMessagingTemplate.convertAndSend("spring.boot.map.queue", map);

        return "发送了map消息到队列spring.boot.map.queue：";
    }

    @GetMapping("/sendSms")
    public String sendSmsMsg(){
        Map<String, String> map = new HashMap<>();
        map.put("mobile", "18312329179");
        map.put("signName", "黑马");
        map.put("templateCode", "SMS_125018593");
        map.put("templateParam", "{\"code\":\"666888\"}");

        //默认使用的是队列模式
        jmsMessagingTemplate.convertAndSend("itcast_sms_queue", map);

        return "发送了map消息到队列itcast_sms_queue";
    }
}
