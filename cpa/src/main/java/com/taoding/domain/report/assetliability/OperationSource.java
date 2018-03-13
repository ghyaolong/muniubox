package com.taoding.domain.report.assetliability;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 资产负债类报表的公式的取数规则
 * @author Yang Ji Qiang
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OperationSource extends DataEntity<OperationSource>{
	
	private static final long serialVersionUID = -4450758595234892029L;
	
	//取数规则名称
	private String name;
	
	//模块编号
	@JsonIgnore
	private int moduleNo;
}
