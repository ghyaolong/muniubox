package com.taoding.domain.report.profit;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;


/**
 * 报表-利润表-客户-项目-计算公式
 * @author fc
 *
 */
@Data
@ToString
@ValidationBean
public class ProfileCustomerFomula extends DataEntity<ProfileCustomerFomula> {


	private String subjectId;  //对应科目编号
	
	private String accountId;  //关联账簿ID
	
	private Integer operation;  //操作符
	
	private Integer operandSource; //取数规则
	
	private Integer itemId; //利润表项目编号
	
}
