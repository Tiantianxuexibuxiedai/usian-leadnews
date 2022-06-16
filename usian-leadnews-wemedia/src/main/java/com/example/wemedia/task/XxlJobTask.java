package com.example.wemedia.task;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//注入spring容器
@Component
@Slf4j
public class XxlJobTask {

    @XxlJob("HelloXxl")
    public ReturnT hello(String param) {
        log.info("xxl-job........");
        return ReturnT.SUCCESS;
    }
}
