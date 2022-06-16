package com.example.admin.listener;

import com.example.admin.service.WnNewsCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WmNewsListener {
    @Autowired
    private WnNewsCheckService wnNewsCheckService;

    @KafkaListener(topics = "WM_NEWS_TOPIC",groupId = "test")
    public void getNewsId(String id) {
        log.info("kafka接收的消息，id:{}", id);
        //审核
        wnNewsCheckService.check(Integer.valueOf(id));

    }
}
