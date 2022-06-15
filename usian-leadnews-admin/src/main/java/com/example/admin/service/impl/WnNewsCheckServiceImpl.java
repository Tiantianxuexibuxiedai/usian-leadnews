package com.example.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.admin.constants.CommonConstant;
import com.example.admin.feign.WmNewsFeign;
import com.example.admin.mapper.AdSensitiveMapper;
import com.example.admin.service.WnNewsCheckService;
import com.example.admin.utils.FastDFSClientUtil;
import com.usian.common.aliyun.AliyunImageScanRequest;
import com.usian.common.aliyun.AliyunTextScanRequest;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.media.dtos.ContentDto;
import com.usian.model.media.pojos.WmNews;
import com.usian.utils.common.SensitiveWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WnNewsCheckServiceImpl implements WnNewsCheckService {
    @Value("${file.fileServerUrl}")
    private String fileServerUrl;
    @Autowired
    private WmNewsFeign wmNewsFeign;
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
                if (textResult.equals(CommonConstant.ALIYUN_CHECK_PASS)) {
                    //审核通过 进行图片审核
                    //获取需要校验的图片
                    List<String> checkImg = getCheckImg(wmNews);
                    log.info("需要校验的图片列表:{}", checkImg);
                    //下载图片
                    List<byte[]> img = downloadImg(checkImg);
                    //发送阿里云审核
                    String imgResult = aliyunImageScanRequest.imageScan(img);
                    log.info(imgResult);
                    if (imgResult.equals(CommonConstant.ALIYUN_CHECK_PASS)) {
                        //图片审核通过 修改状态通过
                        wmNewsFeign.updateWnNewsById(wmNews.getId(),8);

                    } else if (imgResult.equals(CommonConstant.ALIYUN_CHECK_BLOCK)) {
                        //图片审核不通过 修改状态
                        wmNewsFeign.updateWnNewsById(wmNews.getId(), 2);
                    }
                    if (imgResult.equals(CommonConstant.ALIYUN_CHECK_REVIEW)) {
                        //图片审核不确定 修改状态 需要人工审核
                        wmNewsFeign.updateWnNewsById(wmNews.getId(), 3);
                    }

                } else if (textResult.equals(CommonConstant.ALIYUN_CHECK_BLOCK)) {
                    //审核不通过 修改状态
                    wmNewsFeign.updateWnNewsById(wmNews.getId(), 2);

                } else if (textResult.equals(CommonConstant.ALIYUN_CHECK_REVIEW)) {
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
//imgUrlList:["/group1/M00/00/pic.jpg","/group1/M00/00/pic.jpg"]
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

