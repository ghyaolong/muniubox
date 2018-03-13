package com.taoding.service.ocr;

import java.util.List;

import com.taoding.common.exception.BRecognitionException;
import com.taoding.ocr.module.WordsResult;

/**   
 * @ClassName:  OCRParseFactory   
 * @Description:OCR解析工厂工具类
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月21日 上午11:26:29
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
public class OCRParseFactory {
	/**
	 * 解析百度通用票据识别返回的json数据
	 * @param U 为解析json，将json实例化的javaBean对象
	 * 
	 */
	public static <U> U  prase(OCRService<U> t,List<WordsResult> words_result) throws BRecognitionException {
		return t.parse(words_result);
	}
}
