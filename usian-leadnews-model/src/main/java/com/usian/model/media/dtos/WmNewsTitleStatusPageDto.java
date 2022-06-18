package com.usian.model.media.dtos;

import com.usian.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmNewsTitleStatusPageDto extends PageRequestDto {
    private String title;
    private Integer status;
}
