package com.taoding.invoiceTemp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇总凭证
 * @ClassName:  SummaryDocument   
 * @Description:TODO
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月28日 上午11:09:32   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved.
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class SummaryDocument extends BaseInvoice{
	
	/**交易流水号*/
	private String serialNo;
	
	/**行号*/
	private String payeeBankNo;
	/**行号*/
	private String payerBankNo;
	
	/**业务类型*/
	private String bussinessType;
	
	/**交易类型*/
	private String transactionType;
	
	
}
