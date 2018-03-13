package com.taoding.domain.settleaccount;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 期末结转生成凭证数据
 * 
 * @author czy 2017年12月19日 上午9:20:39
 */
@Data
@ToString
@ValidationBean
public class CpaFinalLiquidationVoucher extends
		DataEntity<CpaFinalLiquidationVoucher> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3357201529136802090L;

	// 账簿ID
	private String bookId;
	// 客户ID
	private String customerId;
	// 所属账期
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date currentPeriod;
	// 期末结转分项键
	private String subKey;
	// 期末结转分项名称
	private String subName;
	// 金额
	private BigDecimal amount;
	// 凭证ID
	private String voucherId;

}
