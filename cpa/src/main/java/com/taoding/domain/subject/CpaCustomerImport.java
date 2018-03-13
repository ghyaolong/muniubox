package com.taoding.domain.subject;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CpaCustomerImport {

	
	// 科目编号
	private String subjectNo;
	
	//父节点编号
	private String parent;
	
	//子节点列表
	private List<CpaCustomerImport> subCustomerSubject;
	
	// 科目名称
	private String subjectName;
	
	// 本年累计借方
	private BigDecimal currentYearDebit;
	
	// 本年累计贷方
	private BigDecimal currentYearCredit;
	
	// 期末余额借方
	private BigDecimal endBalancesDebit;
	
	// 期末余额贷方
	private BigDecimal endBalancesCredit;
}
