package com.taoding.domain.report.profit;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;


/**
 * 报表-利润表-项目-计算公式-模板
 * @author fc
 *
 */
@Data
@ToString
@ValidationBean
public class ProfileFomulaTemplate extends DataEntity<ProfileFomulaTemplate> {

	private String subjectId;  //对应科目编号
	
	private Integer operation;  //操作符
	
	private Integer operandSource; //取数规则
	
	private Integer itemId; //利润表项目编号	
}
