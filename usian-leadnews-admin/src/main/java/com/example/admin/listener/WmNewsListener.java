package com.example.admin.listener;

import com.example.admin.service.WnNewsCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class WmNewsListener {
    @Autowired
    private WnNewsCheckService wnNewsCheckService;

    @KafkaListener(topics = "WM_NEWS_TOPIC")
    public void getNewsId(Integer id) {
        //审核
        wnNewsCheckService.check(id);

    }
}
