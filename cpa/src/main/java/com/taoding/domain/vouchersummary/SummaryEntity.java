package com.taoding.domain.vouchersummary;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 科目汇总表
 * 
 * @author czy
 * @SuppressWarnings("serial") 2017年12月12日 下午3:07:39
 */
@Data
public class SummaryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2557825993064288141L;
	
	// 科目ID
	private String subjectId;
	
	// 借方金额
	private BigDecimal amountDebit;
	
	// 贷方金额
	private BigDecimal amountCredit;

}
