package com.taoding.domain.settleaccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 期末结转
 * 
 * @author czy 2017年12月19日 上午10:00:37
 */
@Data
@ToString
@ValidationBean
@EqualsAndHashCode(callSuper = false)
public class CpaFinalLiquidation extends DataEntity<CpaFinalLiquidation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1806097182340377019L;

	// 账簿ID
	private String bookId;
	// 客户ID
	private String customerId;
	// 所属账期
	private Date currentPeriod;
	// 是否期末结转
	private boolean settleAccounts;
	//需要提交的凭证lists
	private List<CpaFinalLiquidationVoucher> voucherLists = new ArrayList<CpaFinalLiquidationVoucher>() ; 

}
