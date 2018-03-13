package com.taoding.domain.voucher;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 凭证模板科目
 * 
 * @author czy 2017年11月28日 上午11:34:22
 */
@Data
@ToString
@ValidationBean
public class CpaVoucherTempleteSubject extends DataEntity<CpaVoucherTempleteSubject> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6628320903579391061L;

	// 模板ID
	private String templeteId;
	
	// 账簿ID
	private String bookId;

	// 客户ID
	private String customerId;
	
	// 摘要
	private String abstracts;

	// 科目ID
	private String subjectId;

	// 借方金额
	private BigDecimal amountDebit;

	// 贷方金额
	private BigDecimal amountCredit;

	// 排序
	private Integer sort;
	
	/**
	 * 以下为扩充字段，数据库不保存该字段数值，供凭证页面显示使用
	 */
	//该科目所有借方金额合计
	@JsonIgnore
	private BigDecimal sumDebit;
	//该科目所有贷方金额合计
	@JsonIgnore
	private BigDecimal sumCredit;
	//科目编号
	private String subjectNo;
	//科目名称
	private String subjectName;
	//借贷方向
	@JsonIgnore
	private boolean direction;
	//科目期初余额
	@JsonIgnore
	private BigDecimal beginningBalances;
	//剩余金额
	private BigDecimal surplusAmount ;

}
