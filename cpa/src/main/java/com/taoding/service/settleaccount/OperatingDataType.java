package com.taoding.service.settleaccount;

public enum OperatingDataType {
	
	/**
	 * 账期内报税的税值
	 */
	TAX_VALUE(""),
	/**
	 * 累计收入
	 */
	CUMULATIVE_INCOME("小规模转一般纳税人预警金额"),
	/**
	 * 资产负债率
	 */
	ASSET_LIABILITY_RATIO("资产负债率范围"),
	/**
	 * 增值税税负率
	 */
	VAT_TAX_RATE("增值税税负范围"),
	/**
	 * 所得税税负率
	 */
	INCOME_TAX_RATE("所得税税负范围"),
	/**
	 * 毛利率
	 */
	GROSS_INTEREST_RATE("毛利率");
	
	
	private String name;
	
	private OperatingDataType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
