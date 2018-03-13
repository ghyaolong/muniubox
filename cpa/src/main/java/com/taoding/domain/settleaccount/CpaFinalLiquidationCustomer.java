package com.taoding.domain.settleaccount;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 企业期末结转基础配置
 * 
 * @author czy 2017年12月19日 上午9:20:39
 */
@Data
@ToString
@ValidationBean
public class CpaFinalLiquidationCustomer extends DataEntity<CpaFinalLiquidationCustomer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1992727817664712418L;
	
	// 账簿ID
	private String bookId;
	
	// 客户ID
	private String customerId;
	
	// 期末结转分项键
	private String subKey;
	
	// 期末结转分项名称
	private String subName;
	
	// true 启用 false 禁用
	private boolean enable;

	
}
