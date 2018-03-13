package com.taoding.domain.subject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.assisting.CpaCustomerAssisting;

@Data
@ToString
@ValidationBean
@EqualsAndHashCode(callSuper=false)
public class CpaCustomerSubject extends DataEntity<CpaCustomerSubject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 232607019992527639L;

	// 客户ID
	private String customerId;

	//账簿ID
	private String bookId;
	
	// 对应科目类型的ID
	private String catalogId;

	// 科目编号
	private String subjectNo;

	// 科目名称
	private String subjectName;

	// 1：借 0：贷
	private boolean direction;

	// 一级科目的父科目是0
	private String parent;
	
	//0 基础科目 1非基础科目
	private boolean basicSubject ;
	
	// 级别
	private Integer level;

	// 0: 非辅助核算项 // 1: 是辅助核算项
	private boolean assisting;

	// 是否是科目，如果是科目
	private boolean subject;
	
	//是否是存货
	private boolean inventory;
	
	// 期初余额
	private BigDecimal beginningBalances;

	// 本年累计借方
	private BigDecimal currentYearDebit;

	// 本年累计贷方
	private BigDecimal currentYearCredit;

	// 年初余额
	private BigDecimal initBalance;

	// 实际损益发生额
	private BigDecimal profitAndLose;

	// 是否结账
	private boolean finish;

	//辅助核算项提交数据 ids
	private String [] assistingdata ;
	
	//辅助核算项基础数据
	private List<CpaCustomerAssisting> assistingLists = new ArrayList<CpaCustomerAssisting>();
	
	//所选辅助核算项数量
	private Integer assistingSize = 0;
	
	//是否有子节点 (辅助核算默认都没有子节点，业务需求)   0 没有有节点 1 有子节点
	private Boolean hashChild;
	
	/**
	 * 以下为扩充字段，数据库不保存该字段数值，供凭证页面显示使用
	 */
	
	//该科目所有借方金额合计
	@JsonIgnore
	private BigDecimal sumDebit;
	//该科目所有贷方金额合计
	@JsonIgnore
	private BigDecimal sumCredit;
	//剩余金额
	private BigDecimal surplusAmount ;
	//父科目名称
	private String parentName ;
	
}
