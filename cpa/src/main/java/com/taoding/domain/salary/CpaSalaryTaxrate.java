package com.taoding.domain.salary;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

/**
 * 税率Entity
 * @author csl
 * @version 2017-12-13
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryTaxrate extends DataEntity<CpaSalaryTaxrate> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=10, message="级数长度必须介于 0 和 10 之间")
	private String series;		// 级数
	private BigDecimal taxMax;		// 含税级距(1、tax_max=1500，不超过1500；2、tax_max=4500，超过1500至4500之间；3、tax_max=9000，超过4500至9000之间；4、tax_max=35000，超过9000至35000；5、tax_max=55000，超过35000至55000之间；6、tax_max=80000，超过55000至80000之间。)
	private BigDecimal rate;		// 税率(%)
	private BigDecimal quickDeduction;		// 速算扣除数
	
	//个税起征点常量
	public static final BigDecimal PERSONAL_INCOME_TAX_EXEMPTION = new BigDecimal(3500);
}