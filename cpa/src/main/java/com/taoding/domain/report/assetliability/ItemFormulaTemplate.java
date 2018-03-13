package com.taoding.domain.report.assetliability;

import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 资产负债类报表项的计算公式模板
 * @author Yang Ji Qiang
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ItemFormulaTemplate extends DataEntity<ItemFormulaTemplate>{

	private static final long serialVersionUID = -1606856804271927008L;
	
	//资产负债报表项目Id
	private Integer itemId;
	
	//关联科目Id
	private String subjectId;
	
	//操作符
	private Integer operation;
	
	//数据来源
	private Integer operationSourceId;
	
}
