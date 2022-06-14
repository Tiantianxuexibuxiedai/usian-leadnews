package com.example.admin.utils;

import java.io.File;
import java.io.FileOutputStream;

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
}
