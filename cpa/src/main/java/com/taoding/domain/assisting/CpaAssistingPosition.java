
package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 职位Entity
 * @author csl
 * @version 2017-11-22
 */
@Data
@ToString
@ValidationBean
public class CpaAssistingPosition extends DataEntity<CpaAssistingPosition> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="职位长度必须介于 0 和 64 之间")
	private String positionName;		// 职位
	
	@Length(min=0, max=64, message="职位编号长度必须介于 0 和 64 之间")
	private String positionNo;		// 职位编号

	@Length(min=0, max=64, message="账薄ID长度必须介于 0 和 64 之间")
	private String accountId;  //账薄ID

	//以下数据用来存储数据
	private String name;
	private String no;
}