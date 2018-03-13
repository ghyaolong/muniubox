package com.taoding.common.utils;

import org.apache.commons.lang3.ArrayUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 
* @ClassName: PinYinUtil 
* @Description: TODO(获取汉字的拼音) 
* @author lixc 
* @date 2017年12月6日 上午9:56:43 
*
 */
public class PinYinUtil {
    /** 
     * 获取汉字串拼音首字母，英文字符不变 
     * 
     * @param chinese 汉字串 
     * @param fristCount 获得前几位
     * @return 汉语拼音首字母 
     */ 
    public static String cn2FirstSpell(String chinese,int fristCount) { 
    	
    	if(StringUtils.isBlank(chinese)) return null;
    	
            StringBuffer pybf = new StringBuffer(); 
            char[] array = chinese.toCharArray();//拆分汉字为数组
            
            if(ArrayUtils.isNotEmpty(array) && array.length<fristCount){
            	fristCount = array.length;
            }
            
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat(); 
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE); //大小写
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            
            for (int i = 0; i < fristCount; i++) { 
                    if (array[i] > 128) {
                            try { 
                                    String[] _t = PinyinHelper.toHanyuPinyinStringArray(array[i], defaultFormat); 
                                    if (_t != null) { 
                                            pybf.append(_t[0].charAt(0)); 
                                    } 
                            } catch (BadHanyuPinyinOutputFormatCombination e) { 
                                    e.printStackTrace(); 
                            } 
                    } else { 
                            pybf.append(array[i]); 
                    } 
            } 
            return pybf.toString().replaceAll("\\W", "").trim(); 
    } 

    /** 
     * 获取汉字串拼音，英文字符不变 
     * 
     * @param chinese 汉字串 
     * @return 汉语拼音 
     */ 
    public static String cn2Spell(String chinese) { 
            StringBuffer pybf = new StringBuffer(); 
            char[] arr = chinese.toCharArray(); 
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat(); 
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE); 
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 
            for (int i = 0; i < arr.length; i++) { 
                    if (arr[i] > 128) { 
                            try { 
                                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]); 
                            } catch (BadHanyuPinyinOutputFormatCombination e) { 
                                    e.printStackTrace(); 
                            } 
                    } else { 
                            pybf.append(arr[i]); 
                    } 
            } 
            return pybf.toString(); 
    } 

}