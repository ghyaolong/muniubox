package com.taoding.common.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;


/**
 * Created by yaochenglong on 2017/11/23.
 * image to base64
 */
public class Base64ImageUtils {

    public static String GetImageStrFromPath(String path){
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        
        Base64 encoder = new Base64();
        return String.valueOf(encoder.encode(data));//返回Base64编码过的字节数组字符串
    }

    public static void main(String[] args) {
        String imgUrl = "C:\\Users\\Administrator\\Desktop\\淘丁\\photo1.jpg";
        String result = Base64ImageUtils.GetImageStrFromPath(imgUrl);
        System.out.println(result);
    }
}
