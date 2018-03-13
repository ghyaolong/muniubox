package com.taoding.invoiceTemp;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @ClassName:  BaseInvoice   
 * @Description:票据基础信息
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月28日 上午11:01:11   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved.
 */
@Data
public class BaseInvoice {
	
	/**票据抬头*/
	private String title;
	
	/**记账日期，一般指业务办理时间*/
	private Date accountDate;
	
	///----------------收款人---------------//
	
	/**收款人名称*/
	private String payeeName;
	
	/**收款人账号*/
	private String payeeAccount;
	
	/**收款人开户行*/
	private String payeeBank;
	
	
	//----------------付款人---------------//
	/**付款人名称*/
	private String payerName;
	
	/**付款人账号*/
	private String payerAccount;
	
	/**付款人开户行*/
	private String payerBank;
	
	/**大写金额*/
	private String amountUpperCase;
	
	/**小写*/
	private BigDecimal amountLowerCase;
	
	/**摘要*/
	private String summary;
	
}
