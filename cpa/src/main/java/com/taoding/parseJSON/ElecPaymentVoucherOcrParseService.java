package com.taoding.parseJSON;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taoding.common.exception.BRecognitionException;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.invoiceTemp.ElecPaymentVoucher;
import com.taoding.ocr.module.WordsResult;
import com.taoding.service.ocr.OCRService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  ElecPaymentVoucherOcrParseService   
 * @Description:电子缴税凭证解析器
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月28日 下午3:53:27   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved.
 */
@Slf4j
public class ElecPaymentVoucherOcrParseService implements OCRService<ElecPaymentVoucher> {

	@Override
	public ElecPaymentVoucher parse(List<WordsResult> words_result) throws BRecognitionException {
		ElecPaymentVoucher epv = new ElecPaymentVoucher();
		for (int i = 0; i < words_result.size(); i++) {
			String value = words_result.get(i).getWords();

			if (value.indexOf("电子缴税付款凭证") != -1) {
				epv.setTitle(value);
			}

			if (value.indexOf("缴税日期") != -1) {
				value = value.substring(value.indexOf(":") + 1);
				System.out.println(DateUtils.parseDate(value));
				epv.setAccountDate(DateUtils.parseDate(value));
			}

			if (value.indexOf("付款人全称") != -1) {
				if ("付款人全称".equals(value.replace(":", ""))) {
					value = words_result.get(i + 1).getWords();
				}else {
					value = value.substring(value.indexOf(":")+1);
				}
				epv.setPayerName(value);
			}

			if (value.indexOf("付款人账号") != -1) {
				if ("付款人账号".equals(value.replace(":", ""))) {
					String val = words_result.get(i + 1).getWords();
					if(val.indexOf("征收机关名称")!=-1) {
						epv.setPayerAccount(null);
					}
				} else {
					value = value.substring(value.indexOf(":") + 1);
					epv.setPayerAccount(value);
				}
				
			}
			
			if(value.indexOf("征收机关名称")!=-1) {
				value = value.substring(value.indexOf(":")+1);
				if(StringUtils.isEmpty(value)) {
					value = words_result.get(i + 1).getWords();
				}
				epv.setPayeeName(value);
			}
			
			
			// 小写金额  小写(合计)金额:0.10元
			if (value.indexOf("小写") != -1) {
				// 需要考虑加税合计的小写部分被盖章遮盖的情况
				value = value.substring(value.indexOf(":")+1).replace("元","");
				try {
					epv.setAmountLowerCase(new BigDecimal(value));
				} catch (Exception e) {
					log.error("小写金额["+value+"]转换异常:", e);
				}
				/*String reg = "\\b[\\d.]+\\b";
				// String reg="\\d+(?:\\.\\d+)?%?";
				Pattern pattern = Pattern.compile(reg);
				Matcher m = pattern.matcher(value);
				while (m.find()) {
					String result = m.group(0);
					try {
						epv.setAmountLowerCase(new BigDecimal(value));
					} catch (Exception e) {
						log.error("小写金额["+value+"]转换异常:", e);
					}
				}*/
			}
			
			//摘要
			if(value.indexOf("实缴金额")!=-1) {
				String result = words_result.get(i + 1).getWords();
				String reg = "\\b[\\d.]+\\b";
				// String reg="\\d+(?:\\.\\d+)?%?";
				Pattern pattern = Pattern.compile(reg);
				Matcher m = pattern.matcher(value);
				if(m.find()) {
					result = words_result.get(i + 2).getWords();
				}
				epv.setSummary(result );
			}

			if (value.indexOf("转账日期") != -1) {
				value = value.substring(value.indexOf(":") + 1);
				epv.setAccountDate(DateUtils.parseDate(value));
			}

		}

		return epv;
	}
	
	/*public static void main(String[] args) throws BRecognitionException {
		List<WordsResult> list = ParseUtilsTest.parse("msyh_03.jpg");
		ElecPaymentVoucherOcrParseService epv = new ElecPaymentVoucherOcrParseService();
		ElecPaymentVoucher voucher = epv.parse(list);
		System.out.println(JSON.toJSON(voucher));
	}*/

}
