package com.taoding.parseJSON;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.taoding.common.exception.BRecognitionException;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.ticket.vo.Summary;
import com.taoding.invoiceTemp.PaymentReceiptBill;
import com.taoding.ocr.module.WordsResult;
import com.taoding.service.ocr.OCRService;

import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName:  PaymentReceiptBillOcrParseService   
 * @Description:银行收付款回单，目前支持招商银行和中国银行，其他银行的其他票据类型目前不支持。
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月25日 下午3:21:59   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
@Slf4j
public class PaymentReceiptBillOcrParseService implements OCRService<PaymentReceiptBill> {
	
	

	@Override
	public PaymentReceiptBill parse(List<WordsResult> words_result) throws BRecognitionException {
		PaymentReceiptBill bill = new PaymentReceiptBill();
		int accountCount = 0;
		int bankAccountCount = 0;
		List<Summary> summaryList = new ArrayList<Summary>();
		for (int i = 0; i < words_result.size(); i++) {
			WordsResult wordsResult = words_result.get(i);
			String value = wordsResult.getWords();
			if (StringUtils.isNotEmpty(value)) {
				
				//日期
				if(value.indexOf("日期")!=-1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setDealDate(result);
				}

				if (value.indexOf("收款回单") != -1 || value.indexOf("付款回单") != -1) {
					bill.setTitle(value);
				}
				
				if(value.indexOf("流水号")!=-1) {
					String result = value.substring(value.indexOf(":")+1).replace("经办:", "");
					bill.setSerialNumber(result);
				}

				if (value.indexOf("户名") != -1) {
					String result = value.substring(value.indexOf(":")+1);
					if (accountCount == 0) {
						bill.setPayeeAccount(result);
						accountCount++;
					} else {
						bill.setPayerAccount(result);
					}
				}
				
				//收付款开户行
				if (value.indexOf("开户行") != -1) {
					String result = value.substring(value.indexOf(":")+1);
					if (bankAccountCount == 0) {
						bill.setPayeeBank(result);
					}
					bankAccountCount++;
				}
				
				

				if (value.indexOf("付款人账号") != -1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setPayeeAccount(result);
				}

				if (value.indexOf("收款账号") != -1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setPayeeAccount(result);
				}
				
				

				// 金额大写
				if (value.indexOf("金额(大写") != -1) {
					String result = value.substring(value.indexOf(":")+1);
					if(!result.contains("整")) {
						WordsResult wordsResult2 = words_result.get(i + 2);
						String nextValue = wordsResult2.getWords();
						if (StringUtils.isNotEmpty(nextValue)) {
							if (nextValue.indexOf("小写") != -1) {
								result = result + words_result.get(i + 1).getWords();
							}
						}						
					}
					bill.setAmountUpper(result);
				}

				if (value.indexOf("小写") != -1) {
					String result = value.substring(value.indexOf(":") + 4);
					if (StringUtils.isNotEmpty(result)) {
						result = result.replace(",", "");
					}
					try {
						BigDecimal b = new BigDecimal(result);
						bill.setAmountLower(b);
					} catch (Exception e) {
						log.error("获取金额异常，金额："+result);
					}
					//bill.setAmountLower(result);
				}
				
				if(value.indexOf("摘要")!=-1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setSummaryKey(result);
				}

				/////////////////////中国银行//////////////////////////
				if (value.indexOf("收款人账号") != -1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setPayeeAccountName(result);
				}
				
				if(value.indexOf("收款人名称")!=-1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setPayeeAccountName(result);
				}
				if(value.indexOf("付款人开户行")!=-1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setPayerBank(result);
				}
				if(value.indexOf("收款人开户行")!=-1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setPayeeBank(result);
				}
				if (value.indexOf("金额") != -1 && value.indexOf("实缴")==-1) {
					String result = value.substring(value.indexOf(":") + 4);
					if (StringUtils.isNotEmpty(result)) {
						result = result.replace(",", "");
					}
					//bill.setAmountLower(result);
					
					try {
						BigDecimal b = new BigDecimal(result);
						bill.setAmountLower(b);
					} catch (Exception e) {
						log.error("获取金额异常，金额："+result);
					}

					WordsResult wordsResult2 = words_result.get(i + 1);
					String nextValue = wordsResult2.getWords();
					if(nextValue.indexOf("人民币")!=-1) {
						bill.setAmountUpper(nextValue);
					}
				}
				
				if(value.indexOf("附言")!=-1) {
					String result = value.substring(value.indexOf(":")+1);
					bill.setSummaryKey(result);
				}
				
				if(value.indexOf("用途")!=-1) {
					String result = value.substring(value.indexOf(":")+1);
					if(StringUtils.isNotEmpty(result)) {
						bill.setSummaryKey(result);
					}
				}
				
				//获取摘要详情[多摘要情况]
				String reg ="\\d{4}/\\d{1,2}/\\d{2}CNY*";
				Pattern ticketNumberR = Pattern.compile(reg);
				Matcher ticketNumberM = ticketNumberR.matcher(value);
				while (ticketNumberM.find()) {
					Summary s = new Summary();
					String summaryName = words_result.get(i-1).getWords();
					String cost = value.substring(value.indexOf("CNY")+3);
					s.setName(summaryName);
					try {
						s.setAmount(new BigDecimal(cost));
					} catch (Exception e) {
						log.error("获取摘要金额["+cost+"]失败:", e);
					}
					
					summaryList.add(s);
				}
			}
		}
		bill.setSummaryList(summaryList);
		return bill;
	}
	
	public static void main(String[] args) throws BRecognitionException {
		//List<WordsResult> parse = ParseUtilsTest.parse("1_yundou.jpg");
		List<WordsResult> parse = ParseUtilsTest.parse("zgyh_dzhd_dzy.jpg");
		PaymentReceiptBillOcrParseService p = new PaymentReceiptBillOcrParseService();
		PaymentReceiptBill bill = p.parse(parse);
		System.out.println(JSON.toJSON(bill));
	}

}
