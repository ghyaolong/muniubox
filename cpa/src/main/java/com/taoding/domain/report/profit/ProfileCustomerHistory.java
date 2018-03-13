package com.taoding.domain.report.profit;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;


/**
 * 报表-利润表-客户-历史
 * @author fc
 *
 */
@Data
@ToString
@ValidationBean
public class ProfileCustomerHistory extends DataEntity<ProfileCustomerHistory> {
	
	private String accountId;  //关联账簿ID
	
	private Date account_period; //账期
	
	private BigDecimal lastMoney;//上期金额
	
	private BigDecimal currentMoney;//本期金额
	
	private BigDecimal yearMoney;//本年累计金额
	
	private Integer itemId; //利润表项目编号
	
}
