
package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算模块的存货类型Entity
 * @author csl
 * @version 2017-11-20
 */
@Data
@ToString
@ValidationBean
public class CpaAssistingGoodsType extends DataEntity<CpaAssistingGoodsType> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=60, message="存货类型名称长度必须介于 1 和 60 之间")
	private String goodsTypeName;		// 存货类型名称
	
	@Length(min=1, max=64, message="账薄id长度必须介于 1 和 64 之间")
	private String accountId;		// 账薄id
	
	//以下数据用来存储数据
	private String name;
}