package com.example.wemedia.service;

import com.usian.model.common.dtos.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface WmMaterielService {
    ResponseResult uploadFile(MultipartFile file) throws IOException;

    ResponseResult delete(Integer id);

    ResponseResult download(Integer id) throws IOException;
}
