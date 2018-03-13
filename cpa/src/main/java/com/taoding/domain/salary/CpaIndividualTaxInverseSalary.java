package com.taoding.domain.salary;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

/**
 * 个税反算工资(仅仅在页面计算工资)
 * @author csl
 * @date 2017-12-21 19:31:23
 */
@Data
@ToString
@ValidationBean
public class CpaIndividualTaxInverseSalary extends DataEntity<CpaIndividualTaxInverseSalary> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=64, message="个人所得税长度必须介于 1 和 64 之间")
	private BigDecimal individualTax;   //个人所得税
	
	@Length(min=1, max=64, message="五险一金长度必须介于 1 和 64 之间")
	private BigDecimal socialInsurance;   //五险一金
	
	@Length(min=1, max=64, message="减除费用长度必须介于 1 和 64 之间")
	private BigDecimal individualTaxLevy;  //减除费用
	
	@Length(min=1, max=64, message="税前工资长度必须介于 1 和 64 之间")
	private BigDecimal salaryBeforeTax;   //税前工资
	
	@Length(min=1, max=64, message="实发工资长度必须介于 1 和 64 之间")
	private BigDecimal takeHomeSalary;   //实发工资
	
	@Length(min=1, max=64, message="税率长度必须介于 1 和 64 之间")
	private BigDecimal salaryRate;   //税率
	
	@Length(min=1, max=64, message="扣减数长度必须介于 1 和 64 之间")
	private BigDecimal quickDeduction;   //扣减数
}
