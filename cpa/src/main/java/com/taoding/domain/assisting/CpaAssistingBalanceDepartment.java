
package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算部门Entity
 * @author csl
 * @version 2017-11-20
 */
@Data
@ToString
@ValidationBean
public class CpaAssistingBalanceDepartment extends DataEntity<CpaAssistingBalanceDepartment> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="部门编号长度必须介于 0 和 64 之间")
	private String departmentNo;		// 部门编号
	
	@Length(min=1, max=200, message="部门名称长度必须介于 1 和 200 之间")
	private String departmentName;		// 部门名称
	
	@Length(min=1, max=64, message="费用类型ID长度必须介于 1 和 64 之间")
	private String expenseTypeId;		// 费用类型ID
	
	@Length(min=1, max=64, message="费用类型ID长度必须介于 1 和 64 之间")
	private String accountId;		// 账薄id

	//以下数据用来存储数据
	private String name;
	private String no;
}