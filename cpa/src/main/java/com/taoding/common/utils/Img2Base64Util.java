package com.taoding.common.utils;


import java.io.*;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by Administrator on 2017/11/23.
 */
public class Img2Base64Util {
    public static void main(String[] args) {
        String imgFile = "1.jpg";//待处理的图片
        String imgbese = getImgStr(imgFile);
        System.out.println(imgbese.length());
      /*
        System.out.println(imgbese);
        String imgFilePath = "d:\\332.jpg";//新生成的图片
        generateImage(imgbese,imgFilePath);*/
    }

    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理


        InputStream in = null;
        byte[] data = null;
        Base64 base = new  Base64();
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(base.encode(data));
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr      图片数据
     * @param imgFilePath 保存图片全路径地址
     * @return
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        //
        if (imgStr == null) //图像数据为空
            return false;

        try {
            //Base64解码
        	 Base64 base = new  Base64();
            byte[] b = base.decode(imgStr);
            //Base64.decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
