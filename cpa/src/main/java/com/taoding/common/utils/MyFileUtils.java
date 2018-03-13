package com.taoding.common.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by yaochenglong on 2017/11/29.
 * 操作文件工具类
 */

public class MyFileUtils {

    private static Logger logger = Logger.getLogger(MyFileUtils.class);

    /**
     * 将文件转成byte[]
     *
     * @param imagePath
     * @return
     */
    public static byte[] readImageFile(String imagePath) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(imagePath));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 1.jpg
     * @return
     */
    public static String getExtensionName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot);
            }
        }
        return null;
    }

    /**
     * 判断当前的文件是不是图片
     *
     * @param imgExtension
     * @return
     */
    @SuppressWarnings("unchecked")
	public static boolean isImg(String imgExtension) {
        YamlMapFactoryBean ymfb = new YamlMapFactoryBean();
        ymfb.setResources(new ClassPathResource("application.yml"));
        Map<String, Object> map = ymfb.getObject();
        map = (Map<String, Object>) map.get("img");
        String imgExt = map.get("extension").toString();

        if (MyStringUtils.isEmtpy(imgExt)) {
            logger.error("yml file is not found img_extension or img_extension is null");
        }else{
            String[] split = imgExt.split(",");
            for (String s : split) {
                if(s.toLowerCase().equals(imgExtension.toLowerCase())){
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * 判断文件是否超出允许上传的最大限度
     * @param fileSize
     * @return
     */
    @SuppressWarnings("all")
    public static boolean isAllowSize(long fileSize){
        YamlMapFactoryBean ymfb = new YamlMapFactoryBean();
        ymfb.setResources(new ClassPathResource("application.yml"));
        Map<String, Object> map = ymfb.getObject();
        map = (Map<String, Object>) map.get("img");
        long imgSize = Long.parseLong(map.get("size").toString());
        return fileSize<=imgSize;
    }

    /**
     * 获取当前时间，精确到毫秒
     * 格式：2017120115390041
     * @return
     */
    public static String getNowString(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmsssss");
        Date dt = new Date();
        String now = sdf.format(dt);
        return now;
    }

    public static void main(String[] args) {
        boolean bool = MyFileUtils.isAllowSize(560450);
        System.out.println(bool);
    }
}
