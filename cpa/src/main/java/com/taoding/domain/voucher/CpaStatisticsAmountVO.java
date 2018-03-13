package com.taoding.domain.voucher;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class CpaStatisticsAmountVO {
	
	// 科目 id
	private String subjectId;
	
	// 科目编号
	private String subjectNum;
	
	// 科目名称
	private String sublectName;
	
	// 借贷方向
	private String direction;
	
	// 借方金额
	private BigDecimal amountDebit;
		
	// 贷方金额
	private BigDecimal amountCredit;
	
	// 余额
	private BigDecimal balance;

}
