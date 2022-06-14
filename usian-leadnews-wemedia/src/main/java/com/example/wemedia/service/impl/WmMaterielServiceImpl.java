package com.example.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.wemedia.mapper.WmMaterielMapper;
import com.example.wemedia.mapper.WmNewsMaterielMapper;
import com.example.wemedia.service.WmMaterielService;
import com.example.wemedia.utils.BytesToFileUtil;
import com.example.wemedia.utils.FastDFSClientUtil;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.pojos.WmMaterial;
import com.usian.model.media.pojos.WmNewsMaterial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class WmMaterielServiceImpl implements WmMaterielService {
    @Value("${file.path}")
    private String filepath;
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;
    @Autowired
    private WmMaterielMapper wmMaterielMapper;
    @Autowired
    private WmNewsMaterielMapper wmNewsMaterielMapper;

    @Override
    public ResponseResult uploadFile(MultipartFile file) throws IOException {
        if (file == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String s = fastDFSClientUtil.uploadFile(file);
        if (s == null || s.equals("")) {
            return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
        }
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUrl(s);
        wmMaterial.setCreatedTime(new Date());
        wmMaterial.setType(0);
        wmMaterial.setUserId(1);
        wmMaterial.setIsCollection(false);
        int insert = wmMaterielMapper.insert(wmMaterial);
        if (insert > 0) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
    }

    @Override
    public ResponseResult delete(Integer id) {
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmMaterial wmMaterial = wmMaterielMapper.selectById(id);
        if (wmMaterial == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        LambdaQueryWrapper<WmNewsMaterial> wWrapper = new LambdaQueryWrapper<>();
        wWrapper.eq(WmNewsMaterial::getMaterialId, id);
        List<WmNewsMaterial> wmNewsMaterials = wmNewsMaterielMapper.selectList(wWrapper);
        if (!CollectionUtils.isEmpty(wmNewsMaterials)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_DELETE);
        }
        fastDFSClientUtil.delFile(wmMaterial.getUrl());
        wmMaterielMapper.deleteById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult download(Integer id) throws IOException {
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmMaterial wmMaterial = wmMaterielMapper.selectById(id);
        if (wmMaterial == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String url = wmMaterial.getUrl();
        if (url == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
        }
        Map<String, String> map = subStringUrl(url);
        byte[] s = fastDFSClientUtil.download(map.get("group"), map.get("path"));
        String fileName = BytesToFileUtil.createFileName(url);
        log.info("------------->type:{}", fileName);

        File file = new File(filepath + fileName);
        BytesToFileUtil.genFile(s, file);
         /*byte[] group1s = fastDFSClientUtil.download("group1", "M00/00/00/wKjIgmKl5-6AKsnlAABNafnEMpU910.jpg");
   File file = new File("d:\\wKjIgl5rdHyAOYC4AACgYxIi_v0227.png");
        BytesToFileUtil.genFile(group1s, file);
*/
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    public Map subStringUrl(String url) {
        HashMap<String, String> map = new HashMap<>();
        int i = url.indexOf("/");
        String group = url.substring(0,i);
        log.info("------------>group:{}", group);
        String path = url.replaceAll(group + "/", "");
        map.put("group", group);
        map.put("path", path);
        return map;
    }

}
