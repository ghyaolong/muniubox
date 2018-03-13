package com.taoding.domain.settleaccount;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

/**
 * 结转销售成本 企业 比例设置
 * @author admin
 */
@Data
@ToString
public class CpaFinalLiquidationProportion {
	
	private String id;
	
	private String bookId;
	
	private String customerId;
	
	// 比例
	private BigDecimal proportion;
}
