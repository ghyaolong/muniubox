package com.taoding.domain.settleaccount;


public enum EnumSettleAccountCode {

	//资产类
	ASSETS_CASH("ASSETS_CASH"),// 1、库存现金
	ASSETS_DEPOSIT("ASSETS_DEPOSIT"),// 2、银行存款
	ASSETS_RAW_MATERIAL("ASSETS_RAW_MATERIAL"),// 3、原材料
	ASSETS_INVENTORY_GOODS("ASSETS_INVENTORY_GOODS"),// 4、库存商品
	ASSETS_FIXED_ASSETS("ASSETS_FIXED_ASSETS"),// 5、固定资产
	ASSETS_DEPRECIATION("ASSETS_DEPRECIATION"),// 6、累计折旧
	ASSETS_INTANGIBLE_ASSETS("ASSETS_INTANGIBLE_ASSETS"),// 7、无形资产
	ASSETS_AMORTIZATION("ASSETS_AMORTIZATION"),// 8、累计摊销
	ASSETS_APPORTIONED_COST("ASSETS_APPORTIONED_COST"),// 9、长期待摊费用

	//费用明细
	COST_SMALL_ENTERPRISE_HOSPITALITY("COST_SMALL_ENTERPRISE_HOSPITALITY"),// 10、小企业 - 业务招待费
	COST_ENTERPRISE_HOSPITALITY("COST_ENTERPRISE_HOSPITALITY"),// 11、企业 - 业务招待费
	COST_SMALL_ENTERPRISE_PUBLICITY("COST_SMALL_ENTERPRISE_PUBLICITY"),// 12、小企业  - 广告和业务宣传费
	COST_ENTERPRISE_PUBLICITY("COST_ENTERPRISE_PUBLICITY"),// 13、企业  - 广告和业务宣传费
	COST_SMALL_ENTERPRISE_UNION_FUNDS("COST_SMALL_ENTERPRISE_UNION_FUNDS"),// 14、小企业  - 工会经费
	COST_ENTERPRISE_UNION_FUNDS("COST_ENTERPRISE_UNION_FUNDS"),// 15、企业  - 工会经费
	COST_SMALL_ENTERPRISE_STAFF_TRAINING("COST_SMALL_ENTERPRISE_STAFF_TRAINING"),// 16、小企业  - 职工教育经费
	COST_ENTERPRISE_STAFF_TRAINING("COST_ENTERPRISE_STAFF_TRAINING"),// 17、企业  - 职工教育经费
	
	//账龄分析
	AGING_ANALYSIS_RECEIVABLE("AGING_ANALYSIS_RECEIVABLE"),// 18、应收账款
	AGING_ANALYSIS_PAYABLE("AGING_ANALYSIS_PAYABLE"),// 19、应付账款
	AGING_ANALYSIS_READY_RECEIVABLE("AGING_ANALYSIS_READY_RECEIVABLE"),// 20、预收账款
	AGING_ANALYSIS_READY_PAY("AGING_ANALYSIS_READY_PAY"),// 21、预付账款
	AGING_ANALYSIS_OTHER_RECEIVABLES("AGING_ANALYSIS_OTHER_RECEIVABLES"),// 22、其他应收款
	AGING_ANALYSIS_OTHER_PAYMENT("AGING_ANALYSIS_OTHER_PAYMENT"),// 23、其他应付款
	
	OPERATING_DATA_UNPAID_VAT("OPERATING_DATA_UNPAID_VAT"),// 24、应交税费-未交增值税
	OPERATING_DATA_CORPORATE_INCOME_TAX("OPERATING_DATA_CORPORATE_INCOME_TAX"),// 25、应交税费-应交企业所得税
	OPERATING_DATA_PROFITS_YEAR("OPERATING_DATA_PROFITS_YEAR"),// 26、本年利润
	
	OTHER("OTHER");	//other
	
	private String code;
	
	private EnumSettleAccountCode(String code) {
        this.code = code;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
