package com.example.wemedia.task;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.wemedia.constants.CommonConstant;
import com.example.wemedia.mapper.WmNewsMapper;
import com.usian.model.media.pojos.WmNews;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

//注入spring容器
@Component
@Slf4j
public class XxlJobTask {
    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @XxlJob("HelloXxl")
    public ReturnT hello(String param) {
        log.info("定时发布文章任务........");
        //每隔30秒扫描一次文章状态是8（待发布）并且当前时间小于发布时间的文章，进行发布
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //文章状态是8
        lambdaQueryWrapper.eq(WmNews::getStatus, CommonConstant.WM_NEWS_CONTENT_STATUS_PASS_RELEASE);
        //当前时间小于发布时间的文章
        lambdaQueryWrapper.lt(WmNews::getPublishTime, new Date());
        //筛选出的文章集合
        List<WmNews> wmNewsList = wmNewsMapper.selectList(lambdaQueryWrapper);
        for (WmNews wmNews : wmNewsList) {
            //将文章的id发送给admin进行文章发送
            kafkaTemplate.send(CommonConstant.WM_NEWS_TOPIC, String.valueOf(wmNews.getId()));
            log.info("将文章的id发送给admin进行文章发送");
        }
        log.info("未查询到待发布文章");
        return ReturnT.SUCCESS;
    }
}
