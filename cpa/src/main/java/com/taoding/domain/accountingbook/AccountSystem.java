package com.taoding.domain.accountingbook;

import lombok.Data;

@Data
public class AccountSystem extends TaxpayerProperty {
	
	public static final String SMALL_ACCOUNTING_STANDARDS_NAME="小企业会计准则";
	public static final String ACCOUNTING_STANDARDS_NAME="企业会计准则";
	
	public static final String SMALL_ACCOUNTING_STANDARDS_ID="1";//小企业会计准则
	public static final String ACCOUNTING_STANDARDS_ID="2"; //企业会计准则
}
