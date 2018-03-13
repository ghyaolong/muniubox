package com.taoding.domain.subject;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;


/**
 * 科目 辅助核算项关系
 * @author czy
 *
 */
@Data
@ToString
@ValidationBean
public class CpaSubjectAssisting extends DataEntity<CpaSubjectAssisting>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6067071715824384874L;
	
	//科目ID
	private String subjectId ;
	
	// 辅助核算项ID	
	private String assistantId ; 
	
}
