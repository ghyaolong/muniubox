package com.taoding.domain.customerTaxItem;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.subject.CpaSubject;

import lombok.Data;

/**
 * 税项模板表Entity
 * @author mhb
 * @version 2017-11-22
 */
@ValidationBean
@Data
public class TaxTemplate extends DataEntity<TaxTemplate> {
	
	public static final boolean  DEL_EABLE_NORMAL=true;
	public static final Boolean TRUE = true;   //启用标示
	public static final Boolean FALSE = false; //禁用标示
	@NotEmpty
	@Length(min=1, max=200, message="科目名长度必须介于 1 和 200 之间")
	private String name;//税项名称
	private Double rate; //税率
	private boolean  enable; //启用状态
	private String province;//省
	private String city;//市
	@NotEmpty
	@Length(min=1, max=64, message="科目名长度必须介于 1 和 64 之间")
	private String subjectId; //关联科目id
	@NotEmpty
	private List<CpaFormula> cpaFormulaList;
	
	
	

}
