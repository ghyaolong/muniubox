package com.taoding.common.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**   
 * @ClassName:  QuotaInvoiceUtils   
 * @Description:定额发票工具类，用于将定额发票的面值金额转换成数字
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月25日 上午10:08:46   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
public class QuotaInvoiceUtils {
	
	
	
	private final static Map<String,BigDecimal> denominationMap = new HashMap<String,BigDecimal>();
	static {
		denominationMap.put("壹元", new BigDecimal(1.0));
		denominationMap.put("贰元", new BigDecimal(2.0));
		denominationMap.put("叁元", new BigDecimal(3.0));
		denominationMap.put("肆元", new BigDecimal(4.0));
		denominationMap.put("伍元", new BigDecimal(5.0));
		denominationMap.put("拾元", new BigDecimal(10.0));
		denominationMap.put("壹拾元", new BigDecimal(10.0));
		denominationMap.put("贰拾元", new BigDecimal(20.0));
		denominationMap.put("叁拾元", new BigDecimal(30.0));
		denominationMap.put("肆拾元", new BigDecimal(40.0));
		denominationMap.put("伍拾元", new BigDecimal(50.0));
		denominationMap.put("壹佰元", new BigDecimal(100.0));
		denominationMap.put("贰佰元", new BigDecimal(200.0));
		denominationMap.put("伍佰元", new BigDecimal(500.0));
		denominationMap.put("壹仟元", new BigDecimal(1000.0));
		denominationMap.put("贰仟元", new BigDecimal(2000.0));
		denominationMap.put("伍仟元", new BigDecimal(5000.0));
		denominationMap.put("壹万元", new BigDecimal(10000.0));
	}
	
	/**
	 * 将大写金额转换成小写数字
	 * @param: amountInWords 大写金额
	 * @return：小写金额
	 */
	public static BigDecimal getDenomination(String amountInWords) {
		if(StringUtils.isEmpty(amountInWords)) {
			throw new NullPointerException("amountInWords param is not null");
		}
		amountInWords = amountInWords.replace("整","");
		Iterator<String> iterator = denominationMap.keySet().iterator();
		while(iterator.hasNext()) {
			String value = iterator.next();
			if(value.equals(amountInWords)) {
				return denominationMap.get(value);
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(QuotaInvoiceUtils.getDenomination("叁拾元整"));
	}
}
