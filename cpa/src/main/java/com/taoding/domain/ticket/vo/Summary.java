package com.taoding.domain.ticket.vo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 摘要
 * @author 刘鑫
 *
 */
@Data
public class Summary {
	
	private String name;		// 摘要名称
	private String spec;		// 规格型号
	private String unit;		// 单位
	private Integer count;		// 数量
	private BigDecimal price;		// 单价
	private BigDecimal amount;		// 金额
	private BigDecimal taxRate;		// 税率
	private BigDecimal taxAmount;	// 税额

}
