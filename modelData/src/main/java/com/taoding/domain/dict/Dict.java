package com.taoding.domain.dict;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

/**
 * <p>Description:字典数据 Entity </p>  
 * @author csl
 * @date 2017年11月7日
 */
@Data
@ToString
@ValidationBean
public class Dict extends DataEntity<Dict> {
	
	private static final long serialVersionUID = 1L;

	private String value;	// 数据值
	
	private String label;	// 标签名
	
	private String type;	// 类型
	
	private String description;// 描述
	
	private Integer sort;	// 排序
	
//	@NotNull(message="父id不能为空")
	private String parentId;//父Id


}
