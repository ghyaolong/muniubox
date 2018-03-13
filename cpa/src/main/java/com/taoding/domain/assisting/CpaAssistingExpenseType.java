
package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算模块的费用类型Entity
 * @author csl
 * @version 2017-11-20
 */
@Data
@ToString
@ValidationBean
public class CpaAssistingExpenseType extends DataEntity<CpaAssistingExpenseType> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=40, message="费用类型名称长度必须介于 1 和 40 之间")
	private String expenseTypeName;		// 费用类型名称
	
	private String accountId;   //账薄id
	
	//以下数据用来存储数据
	private String name;
}