package com.taoding.domain.report.assetliability;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 资产负债表中项目的大类型
 * @author Yang Ji Qiang
 *
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Type extends DataEntity<Type>{
	 
	private static final long serialVersionUID = 2326923370612918339L;

	private String name;
	
	private Integer type;
	
	@JsonIgnore
	private Integer accountingRuleType;
	
	private Integer catogary;
	
	@JsonIgnore
	private Integer order;
	
	private List<Item> items;
	
	//期末金额
	private BigDecimal endingBalanceOfFinance = BigDecimal.ZERO;
	
	//年初金额
	private BigDecimal beginningBalance = BigDecimal.ZERO;
}
