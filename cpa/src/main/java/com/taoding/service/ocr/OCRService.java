package com.taoding.service.ocr;

import java.util.List;

import com.taoding.common.exception.BRecognitionException;
import com.taoding.ocr.module.WordsResult;

/**
 * Created by yaochenglong on 2017/12-04
 */
public interface OCRService<U> {

	/**
	 * 票据解析方法<br>
	 * U:票据对象实体， 每种票据的解析服务对应一种票据对象。
	 * @param:  words_result
	 * @param: 
	 * @throws BRecognitionException
	 */
	U parse(List<WordsResult> words_result) throws BRecognitionException;
}
