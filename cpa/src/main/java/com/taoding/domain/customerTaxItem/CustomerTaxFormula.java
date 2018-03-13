package com.taoding.domain.customerTaxItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.accountingbook.AccountingBook;
/**
 * 客户税项公式设置Entity
 * @author mhb
 * @version 2017-11-24
 */
@ValidationBean
@Data
public class CustomerTaxFormula extends DataEntity<CustomerTaxFormula> {
	@NotNull(message="客户税项信息不能为空")
	private String customerTaxId;//客户税项id
	private String subjectId;//关联科目
	private boolean operator; //运算符   
	private String subjectName; //科目名称
	private List<CustomerTaxFormula> customerTaxFormulaList= new ArrayList<CustomerTaxFormula>();
	

}
