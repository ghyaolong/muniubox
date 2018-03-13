package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算模块的项目类型Entity
 * @author csl
 * date 2017-12-11 13:17:44
 */
@Data
@ToString
@ValidationBean
public class CpaAssistingProject extends DataEntity<CpaAssistingProject>{
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="存货编号长度必须介于 0 和 64 之间")
	private String projectNo;		// 存货编号
	
	@Length(min=1, max=20, message="存货名称必须介于 1 和 40 之间")
	private String projectName;		// 存货名称
	
	@Length(min=1, max=20, message="费用id必须介于 1 和 60 之间")
	private String expenseTypeId;  //费用id
	
	@Length(min=1, max=64, message="账薄id长度必须介于 1 和 64 之间")
	private String accountId;		// 账薄id
	
	//以下数据用来存储数据
	private String name;
	private String no;
}
