package com.example.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wemedia.mapper.WmMaterielMapper;
import com.example.wemedia.mapper.WmNewsMapper;
import com.example.wemedia.mapper.WmNewsMaterielMapper;
import com.example.wemedia.service.WmNewsService;
import com.example.wemedia.utils.FastDFSClientUtil;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.ContentDto;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.pojos.WmMaterial;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.pojos.WmNewsMaterial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WmNewsServiceImpl implements WmNewsService {
    @Value("${fileServerUrl}")
    private String fileServerUrl;
    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private WmMaterielMapper wmMaterielMapper;
    @Autowired
    private WmNewsMaterielMapper wmNewsMaterielMapper;
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;

    @Override
    public ResponseResult queryWmNewsList(WmNewsPageReqDto wmNewsPageReqDto) {
        LambdaQueryWrapper<WmNews> wrapper = new LambdaQueryWrapper<>();
        //判断状态
        if (wmNewsPageReqDto.getStatus() != null) {
            wrapper.eq(WmNews::getStatus, wmNewsPageReqDto.getStatus());
        }
        //判断关键字
        if (wmNewsPageReqDto.getKeyword() != null && !wmNewsPageReqDto.getKeyword().equals("")) {
            wrapper.like(WmNews::getLabels, wmNewsPageReqDto.getKeyword());
        }
        //判断频道
        if (wmNewsPageReqDto.getChannelId() != null) {
            wrapper.eq(WmNews::getChannelId, wmNewsPageReqDto.getChannelId());
        }
        //判断时间
        if (wmNewsPageReqDto.getBeginPubDate() != null && wmNewsPageReqDto.getEndPubDate() != null) {
            //between 判断时间区间
            wrapper.between(WmNews::getPublishTime, wmNewsPageReqDto.getBeginPubDate(), wmNewsPageReqDto.getEndPubDate());
        }
        Page<WmNews> wmNewPage = new Page<>(wmNewsPageReqDto.getPage(), wmNewsPageReqDto.getSize());
        IPage<WmNews> wmNewsPage = wmNewsMapper.selectPage(wmNewPage, wrapper);
        List<WmNews> wmNewsList = wmNewsPage.getRecords();
        if (CollectionUtils.isEmpty(wmNewsList)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // return ResponseResult.okResult(wmNewsList);
        //给出分页信息
        return PageResponseResult.okResult(wmNewPage);
    }

    @Override
    public WmNews addWmNews(WmNewsDto wmNewsDto) {
        if (wmNewsDto == null) {
            log.info("内容不合法");
            return null;
        }
        WmNews wmNews = new WmNews();
        wmNews.setTitle(wmNewsDto.getTitle());
        wmNews.setCreatedTime(new Date());
        //多图 非空判断
        String urls = "";

        if (wmNewsDto.getImages() != null) {

            if (wmNewsDto.getImages().size() > 0) {
                for (String url : wmNewsDto.getImages()) {
                    //去掉前缀
                    String imgUrl = url.replaceAll(fileServerUrl, "");
                    urls += imgUrl + ",";
                }
            }
            //去掉最后逗号
            String images = urls.substring(0, urls.length() - 1);
            log.info(images);
            wmNews.setImages(images);
        }
        if (wmNewsDto.getContentDtoList() == null) {
            log.info("参数为空");
            return null;
        }
        String content = JSON.toJSONString(wmNewsDto.getContentDtoList());
        wmNews.setContent(content);
        wmNews.setEnable(false);
        wmNews.setSubmitedTime(new Date());
        wmNews.setPublishTime(wmNewsDto.getPublishTime());
        wmNews.setChannelId(wmNewsDto.getChannelId());
        wmNews.setLabels(wmNewsDto.getLabels());
        wmNews.setStatus(wmNewsDto.getStatus());
        wmNews.setType(wmNewsDto.getType());
        wmNews.setUserId(1);//TODO

        int insert = wmNewsMapper.insert(wmNews);
        //保存到素材库
        List<ContentDto> contentDtoList = wmNewsDto.getContentDtoList();
        //获取内容图片
        ArrayList<String> imgList = new ArrayList<>();
        for (ContentDto contentDto : contentDtoList) {
            if (contentDto.getType().equals("img")) {
                imgList.add(contentDto.getValue());
            }
        }
        /* for (ContentDto contentDto : dtoList) {*/
        /*for (int i = 0; i < contentDto.size(); i++) {

            if (contentDto.get(i).getType().equals("img")) {
                imgList.add(contentDto.get(i).getValue());
                // list.add(contentDto.getValue());
                *//*WmMaterial wmMaterial = new WmMaterial();
                String url = contentDto.get(i).getValue().replaceAll(fileServerUrl, "");
                wmMaterial.setUrl(url);
                wmMaterial.setCreatedTime(new Date());
                wmMaterial.setType(0);
                wmMaterial.setIsCollection(false);
                wmMaterielMapper.insert(wmMaterial);
                log.info("wmMaterial:{}", wmMaterial);
                //保存到中间表
                WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
                wmNewsMaterial.setNewsId(wmNews.getId());
                wmNewsMaterial.setType(0);
                wmNewsMaterial.setMaterialId(wmMaterial.getId());
                wmNewsMaterial.setOrd(i);//排序
                wmNewsMaterielMapper.insert(wmNewsMaterial);*//*
            }
        }*/
        //保存文章内容图片
        saveNewsMaterial(imgList, wmNews.getId(), 0);
        List<String> images = wmNewsDto.getImages();
        //保存封面图片
        saveNewsMaterial(images, wmNews.getId(), 1);

        if (insert > 0) {
            return wmNews;
        }
        return null;
    }

    @Override
    public WmNews queryWnNewsById(Integer id) {
        return wmNewsMapper.selectById(id);
    }

    @Override
    //图片不可以用同一个地址的，否则报500。
    //com.github.tobato.fastdfs.exception.FdfsServerException: 错误码：2，错误信息：找不到节点或文件
    public ResponseResult deleteWnNewsById(Integer id) {
        if (id != null) {
            log.info("id不可以为空");
        }
        //逻辑删除文章表
        WmNews wmNews = wmNewsMapper.selectById(id);
        wmNews.setIsDelete(true);
        wmNewsMapper.updateById(wmNews);
        //查询中间表
        LambdaQueryWrapper<WmNewsMaterial> wmNewsMaterialLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wmNewsMaterialLambdaQueryWrapper.eq(WmNewsMaterial::getNewsId, wmNews.getId());
        //根据文章id在中间表查询出一个集合
        List<WmNewsMaterial> wmNewsMaterialsList = wmNewsMaterielMapper.selectList(wmNewsMaterialLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(wmNewsMaterialsList)) {
            //在中间表循环出每一个文章id
            for (WmNewsMaterial wmNewsMaterial : wmNewsMaterialsList) {
                //删除素材表(如果一个素材被多个文章引用不可以删除)
                Integer materialId = wmNewsMaterial.getMaterialId();
                //在中间表中查询一个素材表对应的文章表id个数
                LambdaQueryWrapper<WmNewsMaterial> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(WmNewsMaterial::getMaterialId, materialId);
                //一个素材表对应的文章集合
                List<WmNewsMaterial> materialList = wmNewsMaterielMapper.selectList(queryWrapper);
                if (materialList.size() > 1) {
                    log.info("别的文章引用此素材不可以删除");
                } else {
                    //素材fastdfs中素材
                    WmMaterial wmMaterial = wmMaterielMapper.selectById(materialId);
                    if (wmMaterial.getUrl() == null || wmMaterial.getUrl().equals("")) {
                        log.info("素材路径不可以为空");
                    } else {
                        fastDFSClientUtil.delFile(wmMaterial.getUrl());
                    }
                    //只有一个文章引用素材可以删除
                    wmMaterielMapper.deleteById(wmNewsMaterial.getMaterialId());
                }
                //删除中间表
                wmNewsMaterielMapper.deleteById(wmNewsMaterial.getId());
            }
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
    }

    @Override
    public Boolean updateWnNewsById(Integer id, Integer status) {
        if (id == null || status == null) {
            log.info("缺少参数:{},:{}", id, status);
            return false;
        }
        WmNews wmNews = wmNewsMapper.selectById(id);
        wmNews.setStatus(status);
        int i = wmNewsMapper.updateById(wmNews);
        if (i > 0) {
            return true;
        }
        return false;
    }


    /**
     * 封面图片保存在素材表和中间表
     */
    public void saveNewsMaterial(List<String> imageUrlList, Integer newsId, Integer type) {
        for (int i = 0; i < imageUrlList.size(); i++) {
            WmMaterial wmMaterial = new WmMaterial();
            String url = imageUrlList.get(i).replaceAll(fileServerUrl, "");
            wmMaterial.setUrl(url);
            wmMaterial.setCreatedTime(new Date());
            wmMaterial.setType(0);
            wmMaterial.setIsCollection(false);
            wmMaterielMapper.insert(wmMaterial);
            log.info("wmMaterial:{}", wmMaterial);
            //保存到中间表
            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
            wmNewsMaterial.setNewsId(newsId);
            wmNewsMaterial.setType(type);
            wmNewsMaterial.setMaterialId(wmMaterial.getId());
            wmNewsMaterial.setOrd(i);//排序
            wmNewsMaterielMapper.insert(wmNewsMaterial);
        }
    }
}
