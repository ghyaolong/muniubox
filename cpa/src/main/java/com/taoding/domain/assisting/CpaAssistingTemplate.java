
package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算类型模板Entity
 * @author CSL
 * @version 2017-11-17
 */
@Data
@ToString
@ValidationBean
public class CpaAssistingTemplate extends DataEntity<CpaAssistingTemplate> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=40, message="名称长度必须介于 1 和 40 之间")
	private String catalogName;		// 名称
	
	@Length(min=0, max=11, message="排序长度必须介于 0 和 11 之间")
	private String sort;		// 排序
	
	@Length(min=0, max=11, message="辅助核算类型标识必须介于1 和 1 之间")
	private String assistingInfoType;     //辅助核算类型标识
	
	
}