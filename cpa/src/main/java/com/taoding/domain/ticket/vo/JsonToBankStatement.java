package com.taoding.domain.ticket.vo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 
 * @author 刘鑫
 * 对账单 json 数据格式	
 */
@Data
public class JsonToBankStatement {
	
	/**
	 * 行号
	 */
	private String lineNo;
	/**
	 * 日期
	 */
	private String statementDate;
	/**
	 * 资金方向（0为支出，1为收入）
	 */
	private Byte direction;
	/**
	 * 金额
	 */
	private BigDecimal amount;
	
}
