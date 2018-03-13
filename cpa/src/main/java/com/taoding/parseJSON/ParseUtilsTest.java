package com.taoding.parseJSON;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.taoding.common.exception.BRecognitionException;
import com.taoding.ocr.baidu.RecognitionTicketService;
import com.taoding.ocr.module.GeneralTicket;
import com.taoding.ocr.module.WordsResult;

//测试工具类
public class ParseUtilsTest {
	
	public static List<WordsResult> parse(String imgPath) throws BRecognitionException{
		String generalRecognition = RecognitionTicketService.generalRecognition(imgPath);
		GeneralTicket parseObject = JSON.parseObject(generalRecognition, GeneralTicket.class);
		return parseObject.getWords_result();
	}
}
