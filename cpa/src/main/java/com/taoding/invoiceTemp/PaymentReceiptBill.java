package com.taoding.invoiceTemp;

import java.math.BigDecimal;
import java.util.List;

import com.taoding.domain.ticket.vo.Summary;

import lombok.Data;

/**   
 * @ClassName:  PaymentReceiptBill   
 * @Description:收付款回单，这里目前针对,招商银行的汇款单和中国银行的收付款回单和中国银行的其他单据
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月25日 下午3:05:13   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
@Data
public class PaymentReceiptBill {

	// 票据抬头
	private String title;
	// 流水号
	private String serialNumber;
	
	/**银行*/
	private String bankName;

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

	// 金额大写
	private String amountUpper;

	// 金额小写
	private BigDecimal amountLower;

	// 币种
	private String currency;

	// 摘要
	private String summaryKey;

	// 交易时间
	private String dealDate;

	// 附言
	private String remark;

	// 打印日期
	private String printDate;
	
	private List<Summary> summaryList;

}
