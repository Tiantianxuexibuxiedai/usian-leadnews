package com.example.wemedia.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class BytesToFileUtil {
    /**
     * byte[] 转文件
     *
     * @param data
     * @param file
     */
    public static void genFile(byte[] data, File file) {
      /*  if (CheckUtils.isNull(data) || data.length < 3 || CheckUtils.isNull(file))
            return;*/
        try (FileOutputStream fio = new FileOutputStream(file)) {
            fio.write(data, 0, data.length);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String createFileName(String url) {
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        int i = url.indexOf(".");
        String type = url.substring(i, url.length());
        System.out.println(type);
        String filename = name + type;
        System.out.println(filename);
        return filename;
    }

    public static void main(String[] args) {
        BytesToFileUtil.createFileName("wKjIgl5rdHyAOYC4AACgYxIi_v0227.png");
    }
}
