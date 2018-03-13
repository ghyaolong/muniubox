package com.taoding.domain.report.assetliability;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 资产负债报表中的统计项
 * @author Yang Ji Qiang
 *
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Item extends DataEntity<Item>{

	private static final long serialVersionUID = -3819143359698505196L;
	
	public static final Integer IS_PARENT_ITEM = -1;

	//项目名
	private String itemName;
	
	//行号
	@JsonIgnore
	private int lineNumber;
	
	//父项目
	@JsonIgnore
	private Integer parentId;
	
	//所属的大类型
	@JsonIgnore
	private Integer typeId; 
	
	//期末金额
	private BigDecimal endingBalanceOfFinance = BigDecimal.ZERO;
	
	//年初金额
	private BigDecimal beginningBalance = BigDecimal.ZERO;
	
	//包含的公式模板
	private List<ItemFormulaTemplate> formulaTemplats;
	
	//包含的公式
	private List<ItemFormula> formulas;
	
	//操作符
	@JsonIgnore
	private Integer operation;
	
	//汇聚方式
	private Integer accumulationType;
	
	//是否是汇聚项
	@JsonIgnore
	private boolean isAccumulation = false;
	
	@JsonIgnore
	//是否是子项目
	private List<Item> subItemList = new ArrayList<>();
	
	
	//如果parentId的值为-1就说明是父项目
	public boolean isParent() {
		return this.parentId.equals(Integer.valueOf(-1));
	}
	
}
