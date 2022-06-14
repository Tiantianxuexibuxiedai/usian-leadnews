package com.usian.utils.common;

import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//today i learned Distributed Transactional

public class FileUtils {

    /**
     * 重资源流中读取第一行内容
     * @param in
     * @return
     * @throws IOException
     */
    public static String readFirstLineFormResource(InputStream in) throws IOException{
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        return br.readLine();
    }


}
