package com.taoding.domain.report.assetliability;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 每个客户
 * @author Yang Ji Qiang
 *
 */
@ValidationBean
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ItemFormula extends DataEntity<ItemFormula>{
	
	private static final long serialVersionUID = -5403023739698248909L;
	
	//资产负债报表项目Id
	private Integer itemId;
	
	//关联科目Id
	private String subjectId;
	
	//操作符
	private Integer operation;
	
	//数据来源
	private Integer operationSourceId;

	//账簿Id
	private Integer accountingId;
	
	//期末金额
	private BigDecimal endingBalanceOfFinance = BigDecimal.ZERO;
	
	//年初金额
	private BigDecimal beginningBalanceOfYear = BigDecimal.ZERO;
	
	
	public static enum OperandSource{
		
		//余额
		BALANCE(1),
		
		//借方金额
		BALANCE_FROM_DEBIT(2),
		
		//贷方金额
		BALANCE_FROM_CREDI(3),
		
		//借方金额为正
		POSITIVE_BALANCE_FROM_DEBIT(4),
		
		//借方金额为负
		NEATIVE_BALANCE_FROM_DEBIT(5),
		
		//贷方金额为正
		POSITIVE_BALANCE_FROM_CREDI(6),
		
		//贷方金额为负
		NEATIVE_BALANCE_FROM_CREDI(7);
		
		private int value;
		
		private OperandSource(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
}
