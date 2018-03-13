package com.taoding.domain.customerTaxItem;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.subject.CpaSubject;

/**
 * 公式模板Entity
 * @author mhb
 * @version 2017-11-22
 */
@ValidationBean
@Data
public class CpaFormula extends DataEntity<CpaFormula>{
	 
	private String taxTemplateId;//税项模板id
	private  boolean operator;//运算符
	@NotNull(message="关联科目不能为空")
	@Length(min=1, max=64, message="登录名长度必须介于 1 和 64 之间")
	private String subjectId;  //关联科目id
	

}
