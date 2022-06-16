package com.example.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.admin.constants.CommonConstant;
import com.example.admin.feign.ApArticleFeign;
import com.example.admin.feign.WmNewsFeign;
import com.example.admin.mapper.AdSensitiveMapper;
import com.example.admin.mapper.ChannelMapper;
import com.example.admin.service.WnNewsCheckService;
import com.example.admin.utils.FastDFSClientUtil;
import com.usian.common.aliyun.AliyunImageScanRequest;
import com.usian.common.aliyun.AliyunTextScanRequest;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.article.dtos.ApArticleAddDto;
import com.usian.model.media.dtos.ContentDto;
import com.usian.model.media.pojos.WmNews;
import com.usian.utils.common.SensitiveWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WnNewsCheckServiceImpl implements WnNewsCheckService {
    @Value("${file.fileServerUrl}")
    private String fileServerUrl;
    @Autowired
    private WmNewsFeign wmNewsFeign;
    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private ApArticleFeign apArticleFeign;
    @Autowired
    private AdSensitiveMapper adSensitiveMapper;
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;
    @Autowired
    private AliyunTextScanRequest aliyunTextScanRequest;
    @Autowired
    private AliyunImageScanRequest aliyunImageScanRequest;

    /**
     * 根据文章id查询文章内容
     *
     * @param id
     */
    @Override
    public Boolean check(Integer id) {
        WmNews wmNews = wmNewsFeign.queryWnNewsById(id);
        log.info("文章信息:{}", wmNews);
        if (wmNews == null) {
            log.info("查询文章信息出错");
            return false;
        }
        //状态为1的待审核 进行审核
        if (wmNews.getStatus() == 1) {
            //获取文本信息做敏感词审查
            String contentAndTitle = getCheckContentAndTitle(wmNews);
            Boolean result = checkSensitive(contentAndTitle);
            if (!result) {
                log.info("敏感词校验失败");
                return false;
            }
            //阿里云文本审核
            try {
                //获取审核结果
                String textResult = aliyunTextScanRequest.textScanRequest(contentAndTitle);
                log.info("文本审核结果:{}", textResult);
                if (textResult.equals(CommonConstant.ALIYUN_CHECK_PASS)) {
                    //审核通过 进行图片审核
                    //获取需要校验的图片
                    List<String> checkImg = getCheckImg(wmNews);
                    log.info("需要校验的图片列表:{}", checkImg);
                    //下载图片
                    List<byte[]> img = downloadImg(checkImg);
                    //发送阿里云审核
                    String imgResult = aliyunImageScanRequest.imageScan(img);
                    log.info("图片审核结果:{}", imgResult);
                    //此处审核是违规图片在素材出会审核失误
                    switch (imgResult) {
                        case CommonConstant.ALIYUN_CHECK_PASS:
                            //发布文章 修改状态 保存文章
                            releaseWmNews(wmNews);
                            break;
                        case CommonConstant.ALIYUN_CHECK_BLOCK:
                            //图片审核不通过 修改状态
                            wmNewsFeign.updateWnNewsById(wmNews.getId(), 2);
                            break;
                        case CommonConstant.ALIYUN_CHECK_REVIEW:
                            //图片审核不确定 修改状态 需要人工审核
                            wmNewsFeign.updateWnNewsById(wmNews.getId(), 3);
                            break;
                    }
                   /* if (imgResult.equals(CommonConstant.ALIYUN_CHECK_PASS)) {
                        //图片审核通过 修改状态通过
                        wmNewsFeign.updateWnNewsById(wmNews.getId(), 8);
                    }
                    if (imgResult.equals(CommonConstant.ALIYUN_CHECK_BLOCK)) {
                        //图片审核不通过 修改状态
                        wmNewsFeign.updateWnNewsById(wmNews.getId(), 2);
                    }
                    if (imgResult.equals(CommonConstant.ALIYUN_CHECK_REVIEW)) {
                        //图片审核不确定 修改状态 需要人工审核
                        wmNewsFeign.updateWnNewsById(wmNews.getId(), 3);
                    }*/

                }
                if (textResult.equals(CommonConstant.ALIYUN_CHECK_BLOCK)) {
                    //审核不通过 修改状态
                    wmNewsFeign.updateWnNewsById(wmNews.getId(), 2);
                }
                if (textResult.equals(CommonConstant.ALIYUN_CHECK_REVIEW)) {
                    //审核不确定 修改状态 需要人工审核
                    wmNewsFeign.updateWnNewsById(wmNews.getId(), 3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 文章发布
     *
     * @param wmNews
     */
    private void releaseWmNews(WmNews wmNews) {
        //转为时间戳比较
        long publishTime = wmNews.getPublishTime().getTime();
        long nowTime = new Date().getTime();
        //如果当前时间大于发布时间
        if (nowTime >= publishTime) {
            //立即发布 修改状态已发布
            wmNewsFeign.updateWnNewsById(wmNews.getId(), 9);
            ApArticleAddDto apArticleAddDto = new ApArticleAddDto();
            AdChannel adChannel = channelMapper.selectById(wmNews.getChannelId());
            //把wmNews内容复制到apArticleAddDto
            BeanUtils.copyProperties(wmNews, apArticleAddDto);
            if (adChannel != null) {
                apArticleAddDto.setChannelName(adChannel.getName());
            }
            apArticleAddDto.setAuthorId(wmNews.getUserId());
            apArticleAddDto.setLayout(wmNews.getType());

            //保存到article文章表
            Boolean result = apArticleFeign.addArticle(apArticleAddDto);
            if (!result) {
                log.info("调用微服务article失败");
            }
        } else {
            //图片审核通过 修改状态通过待发布
            wmNewsFeign.updateWnNewsById(wmNews.getId(), 8);
        }


    }

    /**
     * 敏感词校验
     *
     * @param content
     * @return
     */
    public Boolean checkSensitive(String content) {
        List<AdSensitive> sensitivesList = adSensitiveMapper.selectList(null);
        if (!CollectionUtils.isEmpty(sensitivesList)) {
            //stream流遍历取出敏感词字段数据
            List<String> sensitives = sensitivesList.stream().map(AdSensitive::getSensitives).collect(Collectors.toList());
            //初始化字典
            SensitiveWordUtil.initMap(sensitives);
            //匹配敏感词
            Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
            if (map.isEmpty()) {
                //不存在敏感词
                return true;
            }
        }
        //存在敏感词
        return false;
    }

    /**
     * 获取需要校验的文字
     *
     * @param wmNews
     * @return
     */
    public String getCheckContentAndTitle(WmNews wmNews) {
        //获取标题
        String title = wmNews.getTitle();
        //获取图文内容
        String content = wmNews.getContent();
        StringBuffer stringBuffer = new StringBuffer(title);
        //json转对象
        List<ContentDto> contentDtoList = JSONObject.parseArray(content, ContentDto.class);
        for (ContentDto contentDto : contentDtoList) {
            //查询文本
            if (contentDto.getType().equals(CommonConstant.WMNEWS_CONTENT_TYPE_TEXT)) {
                stringBuffer.append(contentDto.getValue());
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 获取需要校验的图片
     *
     * @param wmNews
     * @return
     */
    public List<String> getCheckImg(WmNews wmNews) {
        //获取图文内容
        String content = wmNews.getContent();
        ArrayList<String> imgUrlList = new ArrayList<>();
        //json转对象
        List<ContentDto> contentDtoList = JSONObject.parseArray(content, ContentDto.class);
        //获取图文内容图片
        for (ContentDto contentDto : contentDtoList) {
            //查询文本
            if (contentDto.getType().equals(CommonConstant.WMNEWS_CONTENT_TYPE_IMG)) {
                String url = contentDto.getValue();
                String imgUrl = url.replaceAll(fileServerUrl, "");
                imgUrlList.add(imgUrl);
            }
        }
        //获取封面图片
        String images = wmNews.getImages();
        String[] split = images.split(",");
        List<String> coverImgs = Arrays.asList(split);
        //两个list合并成一个
        imgUrlList.addAll(coverImgs);
        return imgUrlList;
    }

    /**
     * 下载图片
     *
     * @param imgUrlList
     * @return
     */
    public List<byte[]> downloadImg(List<String> imgUrlList) {
        List<byte[]> list = new ArrayList<>();
//imgUrlList:["group1/M00/00/pic.jpg","group1/M00/00/pic.jpg"]
        for (String imgUrl : imgUrlList) {
            int i = imgUrl.indexOf("/");
            String group = imgUrl.substring(0, i);
            //下载不要"/"
            String path = imgUrl.substring(i + 1, imgUrl.length());
            try {
                byte[] download = fastDFSClientUtil.download(group, path);
                list.add(download);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}

