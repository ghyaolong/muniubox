package com.taoding.parseJSON;

import java.math.BigDecimal;
import java.util.List;

import com.taoding.common.exception.BRecognitionException;
import com.taoding.common.utils.QuotaInvoiceUtils;
import com.taoding.invoiceTemp.QuotaInvoice;
import com.taoding.ocr.module.WordsResult;
import com.taoding.service.ocr.OCRService;

public class QuotaOcrParseService implements OCRService<QuotaInvoice> {
	
	@Override
	public QuotaInvoice parse(List<WordsResult> words_result) throws BRecognitionException {

		QuotaInvoice qi = new QuotaInvoice();
		for (WordsResult wordsResult : words_result) {
			String value = wordsResult.getWords();
			
			if(value.indexOf("定额发票")!=-1) {
				qi.setTitle(value);
				if(value.indexOf("保险")!=-1) {
					qi.setSummaryKey("保险");
				}else if(value.indexOf("快递")!=-1 || value.indexOf("速递")!=-1||value.indexOf("物流")!=-1) {
					qi.setSummaryKey("物流");
				}else {
					qi.setSummaryKey("服务费");
				}
			}
			
			if(value.indexOf("元整")!=-1) {
				BigDecimal price = QuotaInvoiceUtils.getDenomination(value);
				qi.setDenomination(price);
			}
		}
		return qi;
	}
	
	/*public static void main(String[] args) throws BRecognitionException {
		List<WordsResult> parse = ParseUtilsTest.parse("1_yundou.jpg");
		QuotaOcrParseService qops = new QuotaOcrParseService();
		QuotaInvoice result = qops.parse(parse);
		System.out.println(JSON.toJSON(result));
	}*/

}
