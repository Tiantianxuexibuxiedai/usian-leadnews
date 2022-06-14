package com.example.wemedia.controller;


import com.example.wemedia.service.WmMaterielService;
import com.example.wemedia.utils.BytesToFileUtil;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

//图片操作素材管理
@RestController
@RequestMapping("/api/v1/wmMateriel")
public class WmMaterielController {
    @Autowired
    private WmMaterielService wmMaterielService;

    /**
     * 上传图片
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file) throws IOException {
        return wmMaterielService.uploadFile(file);

    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/delete")
    public ResponseResult delete(Integer id)  {
      return   wmMaterielService.delete(id);

    }


    /**
     * 下载图片
     *
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/download")
    public ResponseResult download(Integer id) throws IOException {
       return wmMaterielService.download(id);
    }

}
