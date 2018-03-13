
package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算Entity
 * @author csl
 * @version 2017-11-16
 */
@Data
@ToString
@ValidationBean
public class CpaCustomerAssistingInfo extends DataEntity<CpaCustomerAssistingInfo> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="辅助核算ID长度必须介于 0 和 64 之间")
	private String catalogId;		// 辅助核算ID
	
	@Length(min=1, max=60, message="编号长度必须介于 1 和 60 之间")
	private String no;		// 编号
	
	@Length(min=1, max=40, message="名称长度必须介于 1 和 40 之间")
	private String name;		// 名称
	
	private CpaCustomerAssisting cpaCustomerAssisting;
	
}