package com.example.admin.controller;

import com.example.admin.utils.BytesToFileUtil;
import com.example.admin.utils.FastDFSClientUtil;
import com.usian.common.aliyun.AliyunImageScanRequest;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.utils.common.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//图片操作
@RestController
public class FileController {
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;
    @Autowired
    private AliyunImageScanRequest aliyunImageScanRequest;

    /**
     * 上传图片
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file) throws IOException {
        String s = fastDFSClientUtil.uploadFile(file);
        return ResponseResult.okResult(s);
    }

    /**
     * 删除图片
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    @PostMapping("/del")
    public ResponseResult del(String filePath) throws IOException {
        fastDFSClientUtil.delFile(filePath);
        return ResponseResult.okResult(null);
    }


    /**
     * 下载图片
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    @PostMapping("/download")
    public ResponseResult download(String filePath) throws IOException {
        byte[] group1s = fastDFSClientUtil.download("group1", "M00/00/00/wKjIgmKl5-6AKsnlAABNafnEMpU910.jpg");
        File file = new File("d:\\wKjIgl5rdHyAOYC4AACgYxIi_v0227.png");
        BytesToFileUtil.genFile(group1s, file);
        return ResponseResult.okResult(null);
    }

    /**
     * 检查下载图片
     *
     * @param
     * @return
     * @throws IOException
     */
    @PostMapping("/checkImg")
    public ResponseResult checkImg() throws IOException {
        byte[] group1s = fastDFSClientUtil.download("group1", "M00/00/00/wKjIgmKpq3-AE9m2AAB7zzsEfgc674.jpg");
        List<byte[]> img = new ArrayList<>();
        img.add(group1s);
        String result="";
        try {
         result = aliyunImageScanRequest.imageScan(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.okResult(result);
    }

}
