package com.taoding.parseJSON;

import java.math.BigDecimal;
import java.util.List;

import com.taoding.common.exception.BRecognitionException;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.invoiceTemp.SummaryDocument;
import com.taoding.ocr.module.WordsResult;
import com.taoding.service.ocr.OCRService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  SummaryDocumentOcrParseService   
 * @Description:汇总凭证解析器
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月28日 上午11:32:04   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved.
 */
@Slf4j
public class SummaryDocumentOcrParseService implements OCRService<SummaryDocument> {

	@Override
	public SummaryDocument parse(List<WordsResult> words_result) throws BRecognitionException {
		SummaryDocument sd = new SummaryDocument();
		
		Integer accountCount = 0;
		Integer bankCount = 0;
		
		Integer bankNoCount = 0;
		for (WordsResult wordsResult : words_result) {
			String value = wordsResult.getWords();
			if (StringUtils.isNotEmpty(value)) {
				if (value.indexOf("汇兑凭证") != -1) {
					sd.setTitle(value);
				}
			}

			if (value.indexOf("交易流水号") != -1) {
				value = value.substring(value.indexOf(":") + 1);
				sd.setSerialNo(value);
			}

			if (value.indexOf("记账日期") != -1) {
				value = value.substring(value.indexOf(":") + 1);
				sd.setAccountDate(DateUtils.parseDate(value));
			}

			if (value.indexOf("账号") != -1) {
				value = value.substring(value.indexOf("账号")+3).replace("|", "");
				if(accountCount==0) {
					sd.setPayeeAccount(value);
					accountCount++;
				}else {
					sd.setPayerAccount(value);
				}
			}

			if (value.indexOf("开户银行") != -1) {
				value = value.substring(value.indexOf("开户银行")+4).replace("|", ""); 
				if(bankCount==0) {
					sd.setPayeeBank(value);
				}else {
					sd.setPayerBank(value);
				}
			}
			if (value.indexOf("行号") != -1) {
				value = value.substring(value.indexOf("行号")+2).replace("|", ""); 
				if(bankCount==0) {
					sd.setPayeeBankNo(value);
					bankNoCount++;
				}else {
					sd.setPayerBankNo(value);
				}
			}
			if (value.indexOf("金额(大写)") != -1) {
				value = value.substring(value.indexOf("大写")+3).replace("|", ""); 
				sd.setAmountUpperCase(value);
			}
			if (value.indexOf("￥") != -1) {
				value = value.substring(value.indexOf("￥")+1).replace(",", "");
				try {
					sd.setAmountLowerCase(new BigDecimal(value));					
				} catch (Exception e) {
					log.error("金额["+value+"]异常："+e);
				}
			}
			if (value.indexOf("摘要") != -1) {
				value = value.substring(value.indexOf("摘要")+2).replace(",", "");
				sd.setSummary(value);
			}
			if (value.indexOf("业务类型") != -1) {
				value = value.substring(value.indexOf("业务类型")+4).replace(":", "");
				sd.setBussinessType(value);
			}
			if (value.indexOf("交易类型") != -1) {
				value = value.substring(value.indexOf("交易类型")+4).replace(":", "");
				sd.setTransactionType(value);
			}
		}
		return sd;
	}

}
