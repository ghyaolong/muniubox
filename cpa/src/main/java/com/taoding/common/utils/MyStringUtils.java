package com.taoding.common.utils;

/**
 * Created by yaochenglong on 2017/11/27.
 *
 */
public class MyStringUtils {

    /**
     * 从字符串中提取最后一次出现的数字
     * @param baseStr
     * @return
     */
    public static String extractNumFromString(String baseStr){
        /*try {
            if (isEmtpy(baseStr)){
                throw new NullPointerException("param is not be null");
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/
        if (isEmtpy(baseStr)){
            return  baseStr;
        }
        String str = baseStr.replaceAll(".*[^\\d](?=(\\d+))","");
        return str;
    }

    /**
     * 剔除字符串中最后一次出现的数字
     * @param baseStr
     * @return
     */
    public static String evictNumFromString(String baseStr){
       /* try {
            if (isEmtpy(baseStr)){
                throw new NullPointerException("param is not be null");
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/
       if(isEmtpy(baseStr)){
           return baseStr;
       }
        String str = baseStr.replaceFirst("\\d+([^\\d]*?$)", "$1");
        return str;
    }

    public static boolean isEmtpy(String str){
        if(null == str || ""==str){
            return true;
        }
        return false;
    }

    public static String extractFlatOrDoubleFromString(String str){
        if(isEmtpy(str)){
            return str;
        }
        str =str.replaceAll("(?<!\\d)\\D", "").replaceAll("[\\u4E00-\\u9FA5\\\\s]","").replace("|","");
        return str;
    }
}
