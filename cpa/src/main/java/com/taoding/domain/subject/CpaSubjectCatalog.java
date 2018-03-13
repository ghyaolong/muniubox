package com.taoding.domain.subject;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;


/**
 * 科目类型
 * @author czy
 *
 */
@Data
@ToString
@ValidationBean
public class CpaSubjectCatalog extends DataEntity<CpaSubjectCatalog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8373120941012935017L;

	//名称
	private String name ; 
	
	//显示顺序
	private Integer sort ;
	
	
}
