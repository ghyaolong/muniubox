package com.taoding.domain.subject;

import lombok.Data;
import lombok.ToString;

import org.hibernate.validator.constraints.NotEmpty;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;


/**
 * 科目详情
 * @author czy
 *
 */
@Data
@ToString
@ValidationBean
public class CpaSubject extends DataEntity<CpaSubject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2407843457692581222L;

	//科目类型的ID
	private String catalogId ;
	
	//科目编号
	@NotEmpty(message = "科目编号不能为空")
	private String subjectNo ;
	
	//科目名称
	@NotEmpty(message = "科目名称不能为空")
	private String subjectName ;
	
	//1：借     0：贷
	private boolean direction ;
	
	//父节点编号
	private String parent ;
	
	//科目级别
	private Integer level ;
	
	//科目类型  0：企业会计准则  1、小企业会计准则
	private boolean type ;
	
	//是否存在子节点  0 没有有节点 1 有子节点
	private boolean hashChild;
	
}
