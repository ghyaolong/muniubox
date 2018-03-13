package com.taoding.domain.ticket.vo;

import lombok.Data;

/**
 * 关联的科目
 * @author 刘鑫
 *
 */
@Data
public class Subject {
	
	/**
	 * 借方类型
	 */
	public final static String DEBIT = "debit";
	/**
	 * 贷方类型
	 */
	public final static String CREDIT = "credit";
	
	private String name;	// 科目名称
	private String no;		// 科目编号
	private String type;	// 借贷类型
}
