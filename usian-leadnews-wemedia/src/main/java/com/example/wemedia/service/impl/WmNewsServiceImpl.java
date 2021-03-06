package com.example.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wemedia.mapper.*;
import com.example.wemedia.service.WmNewsService;
import com.example.wemedia.utils.FastDFSClientUtil;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.ContentDto;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.dtos.WmNewsTitleStatusPageDto;
import com.usian.model.media.pojos.WmMaterial;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.pojos.WmNewsMaterial;
import com.usian.model.media.pojos.WmUser;
import com.usian.model.media.vos.WmNewsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    private WmUserMapper wmUserMapper;
    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private WmMaterielMapper wmMaterielMapper;
    @Autowired
    private WmNewsMaterielMapper wmNewsMaterielMapper;
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;

    @Override
    public ResponseResult queryWmNewsList(WmNewsPageReqDto wmNewsPageReqDto) {
        LambdaQueryWrapper<WmNews> wrapper = new LambdaQueryWrapper<>();
        //????????????
        if (wmNewsPageReqDto.getStatus() != null) {
            wrapper.eq(WmNews::getStatus, wmNewsPageReqDto.getStatus());
        }
        //???????????????
        if (wmNewsPageReqDto.getKeyword() != null && !wmNewsPageReqDto.getKeyword().equals("")) {
            wrapper.like(WmNews::getLabels, wmNewsPageReqDto.getKeyword());
        }
        //????????????
        if (wmNewsPageReqDto.getChannelId() != null) {
            wrapper.eq(WmNews::getChannelId, wmNewsPageReqDto.getChannelId());
        }
        //????????????
        if (wmNewsPageReqDto.getBeginPubDate() != null && wmNewsPageReqDto.getEndPubDate() != null) {
            //between ??????????????????
            wrapper.between(WmNews::getPublishTime, wmNewsPageReqDto.getBeginPubDate(), wmNewsPageReqDto.getEndPubDate());
        }
        Page<WmNews> wmNewPage = new Page<>(wmNewsPageReqDto.getPage(), wmNewsPageReqDto.getSize());
        IPage<WmNews> wmNewsPage = wmNewsMapper.selectPage(wmNewPage, wrapper);
        List<WmNews> wmNewsList = wmNewsPage.getRecords();
        if (CollectionUtils.isEmpty(wmNewsList)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // return ResponseResult.okResult(wmNewsList);
        //??????????????????
        return PageResponseResult.okResult(wmNewPage);
    }

    @Override
    public WmNews addWmNews(WmNewsDto wmNewsDto) {
        if (wmNewsDto == null) {
            log.info("???????????????");
            return null;
        }
        WmNews wmNews = new WmNews();
        wmNews.setTitle(wmNewsDto.getTitle());
        wmNews.setCreatedTime(new Date());
        //?????? ????????????
        String urls = "";

        if (wmNewsDto.getImages() != null) {

            if (wmNewsDto.getImages().size() > 0) {
                for (String url : wmNewsDto.getImages()) {
                    //????????????
                    String imgUrl = url.replaceAll(fileServerUrl, "");
                    urls += imgUrl + ",";
                }
            }
            //??????????????????
            String images = urls.substring(0, urls.length() - 1);
            log.info(images);
            wmNews.setImages(images);
        }
        if (wmNewsDto.getContentDtoList() == null) {
            log.info("????????????");
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
        //??????????????????
        List<ContentDto> contentDtoList = wmNewsDto.getContentDtoList();
        //??????????????????
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
                //??????????????????
                WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
                wmNewsMaterial.setNewsId(wmNews.getId());
                wmNewsMaterial.setType(0);
                wmNewsMaterial.setMaterialId(wmMaterial.getId());
                wmNewsMaterial.setOrd(i);//??????
                wmNewsMaterielMapper.insert(wmNewsMaterial);*//*
            }
        }*/
        //????????????????????????
        saveNewsMaterial(imgList, wmNews.getId(), 0);
        List<String> images = wmNewsDto.getImages();
        //??????????????????
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
    //????????????????????????????????????????????????500???
    //com.github.tobato.fastdfs.exception.FdfsServerException: ????????????2??????????????????????????????????????????
    public ResponseResult deleteWnNewsById(Integer id) {
        if (id != null) {
            log.info("id???????????????");
        }
        //?????????????????????
        WmNews wmNews = wmNewsMapper.selectById(id);
        wmNews.setIsDelete(true);
        wmNewsMapper.updateById(wmNews);
        //???????????????
        LambdaQueryWrapper<WmNewsMaterial> wmNewsMaterialLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wmNewsMaterialLambdaQueryWrapper.eq(WmNewsMaterial::getNewsId, wmNews.getId());
        //????????????id?????????????????????????????????
        List<WmNewsMaterial> wmNewsMaterialsList = wmNewsMaterielMapper.selectList(wmNewsMaterialLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(wmNewsMaterialsList)) {
            //????????????????????????????????????id
            for (WmNewsMaterial wmNewsMaterial : wmNewsMaterialsList) {
                //???????????????(??????????????????????????????????????????????????????)
                Integer materialId = wmNewsMaterial.getMaterialId();
                //??????????????????????????????????????????????????????id??????
                LambdaQueryWrapper<WmNewsMaterial> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(WmNewsMaterial::getMaterialId, materialId);
                //????????????????????????????????????
                List<WmNewsMaterial> materialList = wmNewsMaterielMapper.selectList(queryWrapper);
                if (materialList.size() > 1) {
                    log.info("??????????????????????????????????????????");
                } else {
                    //??????fastdfs?????????
                    WmMaterial wmMaterial = wmMaterielMapper.selectById(materialId);
                    if (wmMaterial.getUrl() == null || wmMaterial.getUrl().equals("")) {
                        log.info("???????????????????????????");
                    } else {
                        fastDFSClientUtil.delFile(wmMaterial.getUrl());
                    }
                    //??????????????????????????????????????????
                    wmMaterielMapper.deleteById(wmNewsMaterial.getMaterialId());
                }
                //???????????????
                wmNewsMaterielMapper.deleteById(wmNewsMaterial.getId());
            }
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.FALL);
    }

    @Override
    public Boolean updateWnNewsById(Integer id, Integer status) {
        if (id == null || status == null) {
            log.info("????????????:{},:{}", id, status);
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

    @Override
    public List<WmNews> queryWnNewsByParam(WmNewsTitleStatusPageDto wmNewsTitleStatusPageDto) {
        if (wmNewsTitleStatusPageDto.getStatus() == null) {
            log.info("?????????????????????");
        }
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        if (wmNewsTitleStatusPageDto.getTitle() != null && !wmNewsTitleStatusPageDto.getTitle().equals("")) {
            queryWrapper.eq(WmNews::getTitle, wmNewsTitleStatusPageDto.getTitle());
        }
        //???????????????????????????
        queryWrapper.eq(WmNews::getStatus, 3).or().eq(WmNews::getStatus, 2);
        //queryWrapper.eq(WmNews::getStatus, wmNewsTitleStatusPageDto.getStatus());
        IPage<WmNews> page = new Page<>(wmNewsTitleStatusPageDto.getPage(), wmNewsTitleStatusPageDto.getSize());
        List<WmNews> wmNewsIPage = wmNewsMapper.selectPage(page, queryWrapper).getRecords();
        return wmNewsIPage;
    }

    @Override
    public WmNewsVo queryWnNewsVoById(Integer id) {
      /*  WmNewsVo wmNewsVo = new WmNewsVo();
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            return null;
        }
        BeanUtils.copyProperties(wmNews, wmNewsVo);
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if (wmUser != null) {
            wmNewsVo.setName(wmUser.getName());
        }*/
        return newsMapper.queryWnNewsVoById(id);
        //    return wmNewsVo;
    }

    @Override
    public int updateArticleIdById(Integer newsId, Long articleId) {
        WmNews wmNews = wmNewsMapper.selectById(newsId);
        wmNews.setArticleId(articleId);
        return wmNewsMapper.updateById(wmNews);
    }


    /**
     * ??????????????????????????????????????????
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
            //??????????????????
            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
            wmNewsMaterial.setNewsId(newsId);
            wmNewsMaterial.setType(type);
            wmNewsMaterial.setMaterialId(wmMaterial.getId());
            wmNewsMaterial.setOrd(i);//??????
            wmNewsMaterielMapper.insert(wmNewsMaterial);
        }
    }
}
