package com.example.admin.utils;

public class StringUtils {
    public static void main(String[] args) {
        String imgUrl = "group1/M00/00/pic.jpg";
        int i = imgUrl.indexOf("/");
        String group = imgUrl.substring(0, i);
        //下载不要"/"
        String path = imgUrl.substring(i + 1, imgUrl.length());
        System.out.println(group + "," + path);
    }
}
