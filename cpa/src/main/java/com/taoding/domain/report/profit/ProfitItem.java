package com.taoding.domain.report.profit;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;


/**
 * 报表-利润表-项目
 * @author fc
 *
 */
@Data
@ToString
@ValidationBean
public class ProfitItem extends DataEntity<ProfitItem> {

	private String name; //项目名称
	
	private Integer lineOrder; //行号
	
	private Integer level; //级别
	
	private Integer parentId; //父节点id
	
	private Integer isAggregation; //是否汇总项  '0为非，1为是'
	
	private Integer operation; //操作符  '-1为减，1为加，0无符号',
	
	private Integer accountingRuleType; //会计准则 '小企业会计准则值为1，企业会计准则值为2'
	
	private Integer showRule; //展示规则  层级展示1，序号展示2
	
	private BigDecimal lastMoney;//上期金额
	
	private BigDecimal currentMoney;//本期金额
	
	private BigDecimal yearMoney;//本年累计金额
	
	@JsonIgnore
	private List<ProfileCustomerFomula> profileCustomerFomulaList; //对应公式
	
	private List<ProfitItem> subProfitItems;
	
	public static final Integer SHOW_RULE_GENERAL = 1;  //层级展示1
	public static final Integer SHOW_RULE_NUMBER = 2;   //序号展示2
	
}
