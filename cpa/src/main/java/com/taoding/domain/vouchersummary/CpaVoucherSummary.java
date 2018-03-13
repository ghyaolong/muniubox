package com.taoding.domain.vouchersummary;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.entity.DataEntity;
/**
 * 科目汇总表
 * @author czy
 * 2017年12月12日 下午3:07:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CpaVoucherSummary extends DataEntity<CpaVoucherSummary> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6489978253760294846L;
	
	// 当前账期 
	public static final int CURRENT_DATA = 1 ;
	// 当前账期+当前账期之前账期
	public static final int CURRENT_FRONT_DATA = 2 ;
	
	//月度z总额
	public static final int MONTH_BALANCE = 1 ;
	//季度总额
	public static final int QUARTER_BALANCE = 2 ;
	//半年
	public static final int HALF_YEAR_BALANCE = 3 ;
	//年度
	public static final int YEAR_BALANCE = 4 ;
	
	// 账簿ID
	private String bookId;
	// 客户ID
	private String customerId;
	// 对应科目类型的ID
	private String catalogId;
	// 账期
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date voucherPeriod;
	// 科目编号
	private String subjectNo;
	// 科目名称
	private String subjectName;
	// 1：借 0：贷
	private boolean direction;
	// 一级科目的父科目是0
	private String parent;
	// 0: 非辅助核算项 1: 是辅助核算项
	private boolean assisting;
	// 是否是科目，如果是科目
	private boolean subject;
	// 本期累计借方
	private BigDecimal currentPeriodDebit;
	// 本期累计贷方
	private BigDecimal currentPeriodCredit;
	
	/**
	 *  扩充字段
	 */
	// 期初余额
	private BigDecimal beginningBalances;
	// 新加字段 余额
	private BigDecimal balance;
	
}
