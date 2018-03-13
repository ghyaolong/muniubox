package com.taoding.domain.settleaccount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.taoding.common.entity.DataEntity;


/**
 * 
 * @author czy
 * 2017年12月27日 上午11:11:04
 */
@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class CpaSettleAccount extends DataEntity<CpaSettleAccount>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6018798680533506989L;

	private String bookId;
	
	private String customerId;
	
	// 账期
	private String currentPeriod;
	
	// 默认为 true
	private boolean settleAccounts;
}
