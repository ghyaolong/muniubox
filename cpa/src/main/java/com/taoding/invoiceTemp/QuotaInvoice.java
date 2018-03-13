package com.taoding.invoiceTemp;

import java.math.BigDecimal;

import lombok.Data;

/**   
 * @ClassName:  QuotaInvoce   
 * @Description:定额发票
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月25日 上午10:37:04   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
@Data
public class QuotaInvoice {
	
	/**发票抬头*/
	private String title;
	
	/**定额发票号码*/
	private String quotaNo;
	
	/**定额发票代码*/
	private String quotaCode;
	
	/**面额*/
	private BigDecimal denomination;
	
	/**
	 * 发票类型,根据发票抬头判断该票据属于何种票据：差旅费[保险费，餐费，长途汽车票],服务费,邮费......
	 * 后期可根据实际需要使用该字段。<br>
	 * 目前只需要把获取到抬头关键字赋值给摘要关键字即可。
	 * */
	private String summaryKey;
	
}
