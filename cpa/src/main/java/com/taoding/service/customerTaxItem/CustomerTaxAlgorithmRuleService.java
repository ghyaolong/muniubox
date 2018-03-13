package com.taoding.service.customerTaxItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;




/**
 * 客户税计算逻辑Service
 * 
 * @author mhb
 * @Date 2017-12-19
 */
public interface CustomerTaxAlgorithmRuleService {
	
	/**
	 * 获取计提税金及附加总税额(小规模纳税人)
	 * 如果印花税小于1元则减去印花税税额
	 * 加收入税额
	 * @param accountingId 账薄id
	 * @param ids 收入ids(主营业务收入、其他业务收入、投资收益、营业外收入)
	 * @return BigDecimal
	 * @author mhb
	 * @date 2017-12-26 15:05:27
	 */
	public BigDecimal suMSmallScale(String accountingId,List<String> ids);
	
	/**
	 * 获取计提税金及附加总税额(一般纳税人)
	 * 如果印花税小于1元则减去印花税税额
	 * 加收入税额
	 * @param accountingId 账薄id
	 * @param incomeTaxId 进项id
	 * @param outputTaxId 销项id
	 * @param ids 收入ids(主营业务收入、其他业务收入、投资收益、营业外收入)
	 * @return BigDecimal
	 * @author mhb
	 * @date 2017-12-26 15:06:01
	 */
	public BigDecimal suMGeneral(String accountingId,String incomeTaxId,String outputTaxId,List<String> ids);
	
	/**
	 * 获取计提税金及附加总税额(小规模纳税人)
	 * 如果有印花税包含印花税
	 * 不包含收入税额
	 * @param accountingId 账薄id
	 * @return BigDecimal
	 * @author mhb
	 * @date 2017-12-26 15:05:27
	 */
	public BigDecimal suMcustomerAccruedTaxAdditiveSmallScale(String accountingId,List<String> ids);
	
	/**
	 * 获取计提税金及附加总税额(一般纳税人)
	 * 如果有印花税包含印花税
	 * 不包含收入税额
	 * @param accountingId 账薄id
	 * @param incomeTaxId 进项id
	 * @param outputTaxId 销项id
	 * @return BigDecimal
	 * @author mhb
	 * @date 2017-12-26 15:06:01
	 */
	public BigDecimal suMcustomerAccruedTaxAdditiveGeneral(String accountingId,String incomeTaxId,String outputTaxId);
	/**
	 * 获取计提税金及附加明细税额(小规模纳税人) 13
	 * @param accountingId 账薄id
	 * @return Map<String,BigDecimal>
	 * @author mhb
	 * @date 2017-12-26 15:06:01
	 */
	public Map<String,BigDecimal>  customerAccruedTaxAdditiveSmallScale(String accountingId,List<String> ids); 
	/**
	 * 获取计提税金及附加明细税额(一般纳税人) 13
	 * @param accountingId 账薄id
	 * @param incomeTaxId  进项销额Id
	 * @param outputTaxId  销项销额Id
	 * @return Map<String,BigDecimal>
	 * @author mhb
	 * @date 2017-12-26 15:06:01
	 */
	public Map<String,BigDecimal>  customerAccruedTaxAdditiveGeneral(String accountingId,String incomeTaxId,String outputTaxId);
	/**
	 * 减免税款收入(计算收入税额) 17
	 * @param accountingId 账薄id
	 * @param incomeMoney 收入ids
	 * @return BigDecimal
	 * @author mhb
	 * @date 2017-12-26 15:06:01
	 */
	public BigDecimal reductionTaxIncome(String accountingId ,List<String> incomeMoneyIds);
	
	/**
	 * 减免税款收入(减免附加税免税政策税额计算) 16
	 * @param accountingId 账薄id
	 * @param incomeMoney 收入ids 
	 * @return BigDecimal
	 * @author mhb
	 * @date 2017-12-26 15:06:01
	 */
	public BigDecimal sumReductionTaxIncomeAdded(String accountingId ,List<String> incomeMoneyIds);
	
	/**
	 * 减免税款收入(减免附加税免税政策税额计算明细) 16
	 * @param accountingId 账薄id
	 * @param incomeMoney 收入ids 
	 * @return BigDecimal
	 * @author mhb
	 * @date 2017-12-26 15:06:01
	 */
	public Map<String, BigDecimal> reductionTaxIncomeAdded(String accountingId ,List<String> incomeMoneyIds);

}
