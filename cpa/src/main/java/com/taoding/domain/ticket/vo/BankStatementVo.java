package com.taoding.domain.ticket.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 银行对账 VO
 * @author LX-PC
 *
 */
@Data
public class BankStatementVo {
	/**
	 * 对账记录ID
	 */
	private String statementId;
	/**
	 * 对账记录日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date statementDate;
	/**
	 * 对账记录收入金额
	 */
	private BigDecimal statementIncome;
	/**
	 * 对账记录成本金额
	 */
	private BigDecimal statementCost;
	/**
	 * 对应的银行票据ID
	 */
	private String ticketId;
	/**
	 * 对应的银行票据凭证ID
	 */
	private String voucherId;
	/**
	 * 对应的银行票据日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date ticketDate;
	/**
	 * 对应的银行票据收入金额
	 */
	private BigDecimal ticketIncome;
	/**
	 * 对应的银行票据成本金额
	 */
	private BigDecimal ticketCost;
	/**
	 * 是否已对账
	 */
	private Boolean statemented;
	
}
