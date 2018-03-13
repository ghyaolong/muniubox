package com.taoding.domain.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 凭证模板
 * 
 * @author czy 
 * 2017年11月28日 上午11:27:56
 */

@Data
@ToString
@ValidationBean
public class CpaVoucherTemplete extends DataEntity<CpaVoucherTemplete> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5153542905693855168L;

	// 账簿ID
	@NotEmpty(message = "账簿ID不能为空")
	private String bookId;

	// 客户ID
	@NotEmpty(message = "科目ID不能为空")
	private String customerId;

	// 模板名称
	@NotEmpty(message = "模板名称不能为空")
	private String templeteName;

	//类型   type == true 模板  type  == false 草稿
	private boolean type; 
	
	// 是否有金额
	private boolean haveMoney;

	// 借方金额
	private BigDecimal amountDebit;

	// 贷方金额
	private BigDecimal amountCredit;

	// 金额大写
	private String accountCapital;

	//模板科目
	public List<CpaVoucherTempleteSubject> subjectLists = new ArrayList<CpaVoucherTempleteSubject>();

}
