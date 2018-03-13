package com.taoding.domain.settleaccount;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 期末结转数据VO
 * 
 * @author czy 2017年12月19日 上午10:00:37
 */
@Data
public class FinalLiquidationVo {


	// 科目Key
	private String subjectKey;
	
	// 科目名称
	private String subjectName;

	// 金额
	private BigDecimal amount;

	
	public FinalLiquidationVo(String subjectKey, String subjectName,
			BigDecimal amount) {
		this.subjectKey = subjectKey;
		this.subjectName = subjectName;
		this.amount = amount;
	}

	
	
}
