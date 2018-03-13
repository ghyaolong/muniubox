package com.taoding.domain.settleaccount;


public enum EnumFinalLiquidationCode {

	JZ_SALE_COST("JZ_SALE_COST","结转销售成本"),// 1、结转销售成本
	JT_SALARY("JT_SALARY","计提工资") ,//	2、计提工资
	DK_SOCIAL_SECURITY("DK_SOCIAL_SECURITY","代扣五险一金"),//	3、代扣五险一金
	DK_INCOME_TAX("DK_INCOME_TAX","代扣个人所得税"),//	4、代扣个人所得税
	JT_COMPANY_SOCIAL_SECURITY("JT_COMPANY_SOCIAL_SECURITY","计提单位承担五险一金"),//5、计提单位承担五险一金
	GZ_CASH("GZ_CASH","现金发放工资"),//	6、现金发放工资
	GZ_BANK("GZ_BANK","银行发放工资"),	//7、银行发放工资
	JT_DEPRECIATION("JT_DEPRECIATION","计提折旧"),//	8、计提折旧
	TX_INTANGIBLE_ASSETS("TX_INTANGIBLE_ASSETS","无形资产摊销"),//9、无形资产摊销
	TX_LONG_APPORTIONED	("TX_LONG_APPORTIONED","长期待摊费用摊销"),//10、长期待摊费用摊销
	JZ_CURRENT_PROFIT_REVENUE("JZ_CURRENT_PROFIT_REVENUE","结转本期损益（收入）"),//11、结转本期损益（收入）
	JZ_CURRENT_PROFIT_COST("JZ_CURRENT_PROFIT_COST","结转本期损益（成本费用）"),//	12、结转本期损益（成本费用）
	JT_TAX_ADDITIONAL("JT_TAX_ADDITIONAL","计提税金及附加"),//	13、计提税金及附加
	JZ_VAT("JZ_VAT","结转增值税"),//14、结转增值税
	ZC_UNPAID_VAT("ZC_UNPAID_VAT","结转增值税"),	//15、转出未交增值税
	JM_TAX_REVENUE("JM_TAX_REVENUE","减免税款收入"),	//16、减免税款收入
	JM_TAX_REVENUE_SMALL("JM_TAX_REVENUE_SMALL","减免税款收入"),//	17、减免税款收入
	JT_INCOME_TAX("JT_INCOME_TAX","计提所得税"),	//18、计提所得税
	JZ_PROFIT("JZ_PROFIT","结转本年利润"),	//19、结转本年利润
	FAIR_VALUE_PROFIT("FAIR_VALUE_PROFIT","公允价值变动损益");	//20、公允价值变动损益
	
	private String code;
	
	private String name;
	
	private EnumFinalLiquidationCode(String code,String name) {
        this.code = code;
        this.name = name;
    }
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	}
