package com.taoding.invoiceTemp;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**   
 * @ClassName:  ElecReturnedBill   
 * @Description:电子回单
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月19日 下午5:23:49   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
@Data
public class ElecReturnedBill implements Serializable{
	
	private static final long serialVersionUID = 3494152386664334801L;
	
	// 票据抬头，这里是 "电子回单"
	private String title;
	// 回单编号
	private String billNo;
	// 页码
	private String pageNum;

	// 付款方账号
	private String payerAccount;
	// 付款方户名
	private String payerAccountName;
	// 付款方开户行
	private String payerBank;

	// 收款方账号
	private String payeeAccount;
	// 收款方户名
	private String payeeAccountName;
	// 收款方开户行
	private String payeeBank;
	
	//金额大写
	private String amountUpper;
	
	//金额小写
	private BigDecimal amountLower;
	
	//币种
	private String currency;
	
	//摘要
	private String summary;
	
	//交易时间
	private String dealDate;
	
	//会计日期
	private String accountDate;
	
	//附言
	private String remark;
	
	//打印日期
	private String printDate;

}
