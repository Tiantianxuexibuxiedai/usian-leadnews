package com.usian.model.article.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usian.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleReqDto extends PageRequestDto {

    private Integer channelId;
    //操作类型1上拉2下拉
    private Integer type;
    //更新列表
    //publicTime>endDateTime最新频道
    //publicTime<startDateTime历史频道
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDataTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDateTime;

}
