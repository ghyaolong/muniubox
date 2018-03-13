package com.taoding.service.settleaccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.settleaccount.CpaFinalLiquidationBasic;
import com.taoding.domain.settleaccount.CpaFinalLiquidationCustomer;
import com.taoding.domain.settleaccount.CpaFinalLiquidationProportion;
import com.taoding.domain.settleaccount.EnumFinalLiquidationCode;
import com.taoding.domain.settleaccount.FinalLiquidationVo;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.customerTaxItem.CustomerTaxAlgorithmRuleService;
import com.taoding.service.fixedAsset.FixedAssetService;
import com.taoding.service.salary.CpaSalaryCompanyWelfareService;
import com.taoding.service.salary.CpaSalaryInfoService;
import com.taoding.service.voucher.CpaVoucherSubjectService;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

@Service
@Transactional(readOnly = true)
public class CpaDealFinalLIquidationServiceImp implements CpaDealFinalLIquidationService{
  
	@Autowired
	private CpaFinalLiquidationBasicService finalLiquidationBasicService;
	@Autowired
	private CpaFinalLiquidationProportionService finalLiquidationProportionService;
	@Autowired
	private CpaVoucherSummaryService voucherSummaryService;
	@Autowired
	private FixedAssetService fixedAssetService;
	@Autowired
	private CustomerTaxAlgorithmRuleService customerTaxAlgorithmRuleService;
	@Autowired
	private AccountingBookService accountingBookService;
	@Autowired
	private CpaFinalLiquidationCustomerService finalLiquidationCustomerService;
	@Autowired 
    private CpaFinalLiquidationService finalLiquidationService;
	@Autowired
	private CpaSalaryInfoService salaryInfoService;
	@Autowired
	private CpaSalaryCompanyWelfareService salaryCompanyWelfareService;
	@Autowired
	private CpaVoucherSubjectService voucherSubjectService;
	
	@Override
   public Object loadFinalLiquidationData(String bookId) {
		
		AccountingBook accountBook = accountingBookService.get(bookId);
		if(accountBook == null || StringUtils.isEmpty(accountBook.getId())){
			throw new LogicException("获取不到当前账簿信息");
		}
		//企业性质
		String accountingSystemId = accountBook.getAccountingSystemId() ;
		//纳税人性质
		String taxpayerPropertyId = accountBook.getTaxpayerPropertyId() ;
		
		//客户ID
		String customerId = accountBook.getCustomerInfoId();
		
		List<CpaFinalLiquidationCustomer> lists = finalLiquidationCustomerService.findListByBookId(bookId);
		
		List<FinalLiquidationVo> listFinalVos = new ArrayList<FinalLiquidationVo>();
		if(lists != null && lists.size() > 0){
			for (CpaFinalLiquidationCustomer liquidationCustomer : lists) {
				if(liquidationCustomer != null && StringUtils.isNotEmpty(liquidationCustomer.getSubKey())){
					String subKey = liquidationCustomer.getSubKey();
					String subName = liquidationCustomer.getSubName();
					BigDecimal amount = BigDecimal.ZERO;
					
					if(EnumFinalLiquidationCode.JZ_SALE_COST.toString().equals(subKey)){ // 1、结转销售成本
						amount = amount.add(deal_JZ_SALE_COST(bookId, accountingSystemId,subKey));
					}else if(EnumFinalLiquidationCode.JT_SALARY.toString().equals(subKey)){ //2、计提工资
						amount = amount.add(deal_JT_SALARY(bookId,customerId));
						
					}else if(EnumFinalLiquidationCode.DK_SOCIAL_SECURITY.toString().equals(subKey)){ //3、代扣五险一金
						
						amount = amount.add(deal_DK_SOCIAL_SECURITY(bookId,customerId));
						
					}else if(EnumFinalLiquidationCode.DK_INCOME_TAX.toString().equals(subKey)){ //4、代扣个人所得税
						
						amount = amount.add(deal_DK_INCOME_TAX(bookId,customerId));
						
					}else if(EnumFinalLiquidationCode.JT_COMPANY_SOCIAL_SECURITY.toString().equals(subKey)){ //5、计提单位承担五险一金
						
						amount = amount.add(deal_JT_COMPANY_SOCIAL_SECURITY(bookId,customerId));
						
					}else if(EnumFinalLiquidationCode.GZ_CASH.toString().equals(subKey)){ //6、现金发放工资
						
						amount = amount.add(deal_GZ_CASH(bookId,customerId));
						
					}else if(EnumFinalLiquidationCode.GZ_BANK.toString().equals(subKey)){ //7、银行发放工资
						
						amount = amount.add(deal_GZ_BANK(bookId,customerId));
						
					}else if(EnumFinalLiquidationCode.JT_DEPRECIATION.toString().equals(subKey)){//8、计提折旧
						
						amount = amount.add(deal_JT_DEPRECIATION(bookId));
						
					}else if(EnumFinalLiquidationCode.TX_INTANGIBLE_ASSETS.toString().equals(subKey)){ //9、无形资产摊销
						
						amount = amount.add(deal_TX_INTANGIBLE_ASSETS(bookId));
						
					}else if(EnumFinalLiquidationCode.TX_LONG_APPORTIONED.toString().equals(subKey)){ //10、长期待摊费用摊销
						
						amount = amount.add(deal_TX_LONG_APPORTIONED(bookId));
						
					}else if(EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString().equals(subKey)){ //11、结转本期损益（收入）
						
						amount = amount.add(deal_JZ_CURRENT_PROFIT_REVENUE(bookId));
						
					}else if(EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_COST.toString().equals(subKey)){ //12、结转本期损益（成本费用）
						
						amount = amount.add(deal_JZ_CURRENT_PROFIT_COST(bookId));
						
					}else if(EnumFinalLiquidationCode.JT_TAX_ADDITIONAL.toString().equals(subKey)){ //13、计提税金及附加 ma
						
						amount = amount.add(deal_JT_TAX_ADDITIONAL(bookId, accountingSystemId,taxpayerPropertyId,subKey));
						
					}else if(EnumFinalLiquidationCode.JZ_VAT.toString().equals(subKey)){ //14、结转增值税
						
						amount = amount.add(deal_JZ_VAT(bookId, accountingSystemId,taxpayerPropertyId,subKey));
						
					}else if(EnumFinalLiquidationCode.ZC_UNPAID_VAT.toString().equals(subKey)){  //15、转出未交增值税
						
						amount = amount.add(deal_ZC_UNPAID_VAT(bookId, accountingSystemId,taxpayerPropertyId,subKey));
						
					}else if(EnumFinalLiquidationCode.JM_TAX_REVENUE.toString().equals(subKey)){ //16、减免税款收入 ma
						
						amount = amount.add(deal_JM_TAX_REVENUE(bookId, accountingSystemId,taxpayerPropertyId,subKey));
						
					}else if(EnumFinalLiquidationCode.JM_TAX_REVENUE_SMALL.toString().equals(subKey)){ //17、减免税款收入 ma
						
						amount = amount.add(deal_JM_TAX_REVENUE_SMALL(bookId, accountingSystemId,taxpayerPropertyId,subKey));
						
					}else if(EnumFinalLiquidationCode.JT_INCOME_TAX.toString().equals(subKey)){ //18、计提所得税
						
						amount = amount.add(deal_JT_INCOME_TAX(bookId, accountingSystemId,taxpayerPropertyId,subKey));
						
					}else if(EnumFinalLiquidationCode.JZ_PROFIT.toString().equals(subKey)){ //19、结转本年利润
						
						amount = amount.add(deal_JZ_PROFIT(bookId,subKey));
					
					}else{
						
					}
					listFinalVos.add(new FinalLiquidationVo(subKey, subName, amount));
				}
			}
		}
		
		return listFinalVos;
	}
	
		/**
		 * 1、处理结转销售成本
		 * 2017年12月21日 下午5:46:15
		 * @param bookId
		 * @param taxpayerProperty
		 * @param subKey
		 * @return
		 */
		private  BigDecimal deal_JZ_SALE_COST(String bookId,String accountingSystemId,String subKey){
			List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId, subKey);
			List<String> subjectIds = new ArrayList<String>();
			if(lists != null && lists.size() > 0){
				for (CpaFinalLiquidationBasic liquidationBasic : lists) {
					if(liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()){
						subjectIds.add(liquidationBasic.getSubjectId());
					}	
				}
			}
			
			CpaFinalLiquidationProportion liquidationProportion = finalLiquidationProportionService.findProportionByBookId(bookId);
			BigDecimal proportion = new BigDecimal(60);
			if(liquidationProportion != null && StringUtils.isNotEmpty(liquidationProportion.getId())){
				proportion = liquidationProportion.getProportion();
			}
			
			return voucherSummaryService.statisticsAmountBySummaryIds(subjectIds, bookId, CpaVoucherSummary.MONTH_BALANCE).multiply(proportion).divide(new BigDecimal(100));
		}
		
		/**
		 * 2、计提工资期末结转金额
		 * @param customerId 客户ID
		 * 2017年12月22日 下午2:07:41
		 * @return
		 */
		private BigDecimal deal_JT_SALARY(String bookId,String customerId){
			Double d = salaryInfoService.getAccruedSalary(bookId,customerId);
			if(null != d){
				return new BigDecimal(d);
			}
			return BigDecimal.ZERO;
		}
		
		/**
		 * 3、代扣五险一金结转金额
		 * @param customerId 客户Id
		 * 2017年12月22日 下午2:07:41
		 * @return
		 */
		private BigDecimal deal_DK_SOCIAL_SECURITY(String bookId,String customerId){
			Map<String, BigDecimal> map = salaryInfoService.getIndividualSocialSecurity(bookId,customerId);
			 if(null == map) return  BigDecimal.ZERO;
			 BigDecimal sum = BigDecimal.ZERO;
			 for (Iterator<BigDecimal> iterator = map.values().iterator(); iterator.hasNext();) {
				 sum = sum.add(iterator.next());
			}
			return sum;
		}
		
		/**
		 * 4、计提折旧期末结转金额
		 * 2017年12月22日 下午2:07:41
		 * @return
		 */
		private BigDecimal deal_JT_DEPRECIATION(String bookId){
			return fixedAssetService.sumThisMonthDepreciation(bookId, AssetType.FIXED_ASSET_TYPE);
		}
		
		/**
		 * 4、代扣个人所得税
		 * 2017年12月22日 下午2:28:50
		 * @param customerId 客户Id
		 * @param bookId
		 * @return
		 */
		private BigDecimal deal_DK_INCOME_TAX(String bookId ,String customerId){
			Double d = salaryInfoService.getIndividualTax(bookId,customerId);
			if(null != d){
				return new BigDecimal(d);
			}
			return BigDecimal.ZERO;
		}
		
	
		
		/**
		 * 5、计提单位承担五险一金
		 * 2017年12月22日 下午2:28:50
		 * @param customerId 客户ID
		 * @return
		 */
		private BigDecimal deal_JT_COMPANY_SOCIAL_SECURITY(String bookId,String customerId){
			Double d = salaryCompanyWelfareService.getCompanyTax(bookId,customerId);
			if(null != d){
				return new BigDecimal(d);
			}
			return BigDecimal.ZERO;
		}
		
		/**
		 * 6、现金发放工资(当月计提当月发)
		 * 2017年12月22日 下午2:31:50
		 * @param customerId 客户Id
		 * @return
		 */
		private BigDecimal deal_GZ_CASH(String bookId,String customerId){
			
			Double d = salaryInfoService.getCashSalary(bookId,customerId);
			if(null != d){
				return new BigDecimal(d);
			}
			
			return BigDecimal.ZERO;
		}
		
		/**
		 * 7、银行发放工资(当月计提下月发)
		 * 2017年12月22日 下午2:33:50
		 * @param customerId
		 * @return
		 */
		private BigDecimal deal_GZ_BANK(String bookId,String customerId){
			
			Double d = salaryInfoService.getBankSalary(bookId,customerId);
			if(null != d){
				return new BigDecimal(d);
			}
			
			return BigDecimal.ZERO;
		}
		
		/**
		 * 9、无形资产期末结转金额
		 * 2017年12月22日 下午1:59:39
		 * @param bookId
		 * @return
		 */
		private BigDecimal deal_TX_INTANGIBLE_ASSETS(String bookId){
			return fixedAssetService.sumThisMonthDepreciation(bookId, AssetType.APPORTIONED_ASSET_TYPE);
		}
		
		/**
		 * 10、长期待摊费用摊销期末结转金额
		 * 2017年12月22日 下午2:02:33
		 * @param bookId
		 * @return
		 */
		private BigDecimal deal_TX_LONG_APPORTIONED(String bookId){
			return fixedAssetService.sumThisMonthDepreciation(bookId, AssetType.INTANGIBLE_ASSET_TYPE);
		}
		
		/**
		 * 11、结转本期损益（收入） 期末结转金额
		 * 2017年12月22日 下午2:36:31
		 * @param bookId
		 * @param accountingSystemId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_JZ_CURRENT_PROFIT_REVENUE(String bookId){
			//查询收入类四项ID
			List<String> subjectIds = finalLiquidationService.get_JZ_CURRENT_PROFIT_REVENUE_List(bookId);
			BigDecimal balance  = voucherSummaryService.statisticsAmountBySummaryIds(subjectIds, bookId, CpaVoucherSummary.MONTH_BALANCE);
			if(balance != null){
				return balance ;
			}
			return BigDecimal.ZERO;
		}

		/**
		 * 12、结转本期损益（成本费用）
		 * 2017年12月22日 下午2:36:31
		 * @param bookId
		 * @param accountingSystemId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_JZ_CURRENT_PROFIT_COST(String bookId){
			//查询收入类四项ID
			List<String> subjectIds = finalLiquidationService.get_JZ_CURRENT_PROFIT_REVENUE_List(bookId);
			BigDecimal balance  = voucherSummaryService.statisticsAmountNotInSummaryIds(subjectIds, bookId);
			if(balance != null){
				return balance ;
			}
			return BigDecimal.ZERO;
		}
			
		/**
		 * 13、计提税金及附加 期末结转金额
		 * 2017年12月22日 下午2:42:11
		 * @param bookId
		 * @param accountingSystemId
		 * @param taxpayerPropertyId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_JT_TAX_ADDITIONAL(String bookId,String accountingSystemId,String taxpayerPropertyId,String subKey){
			
			if(taxpayerPropertyId.equals("1")){ //小规模纳税人
				
				//查询收入类四项ID
				List<String> subjectIds = finalLiquidationService.get_JZ_CURRENT_PROFIT_REVENUE_List(bookId);
				
				if(CollectionUtils.isNotEmpty(subjectIds)){
					return customerTaxAlgorithmRuleService.suMcustomerAccruedTaxAdditiveSmallScale(bookId, subjectIds);
				}
				
			}else{ //一般纳税人
				//销项税额ID
				String outputSubjectId = "";
				//进项项税额ID
				String inputSubjectId = "" ;
				
				List<CpaFinalLiquidationBasic> putLists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
						accountingSystemId,EnumFinalLiquidationCode.JZ_VAT.toString());
				if(putLists != null && putLists.size() > 0){
					for (CpaFinalLiquidationBasic liquidationBasic : putLists) {
						if(liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.getType() == 1){
							outputSubjectId = liquidationBasic.getSubjectId() ;
						}	
						if(liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.getType() == 2){
							inputSubjectId = liquidationBasic.getSubjectId() ;
						}	
					}
				}
				if(StringUtils.isNotBlank(outputSubjectId) && StringUtils.isNotBlank(inputSubjectId) ){
					return customerTaxAlgorithmRuleService.suMcustomerAccruedTaxAdditiveGeneral(bookId, inputSubjectId, outputSubjectId);
				}
			}
			
			return BigDecimal.ZERO;
		}
	
		/**
		 * 14、结转增值税 期末结转金额
		 * 2017年12月23日 上午10:18:49
		 * @param bookId
		 * @param accountingSystemId
		 * @param taxpayerPropertyId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_JZ_VAT(String bookId,String accountingSystemId,String taxpayerPropertyId,String subKey){
			if(taxpayerPropertyId.equals("2")){ //一般规模纳税人
				
				 BigDecimal  output = getDeal_JZ_VAT(bookId, accountingSystemId, taxpayerPropertyId, subKey, 1);//2221.002
				 BigDecimal  input = getDeal_JZ_VAT(bookId, accountingSystemId, taxpayerPropertyId, subKey, 2);//2221.001.002
				
				 if(null != output) {
					output = output.subtract(input);
				} else{
					output = BigDecimal.ZERO.subtract(input);
				}
				return output;
			}
		   return BigDecimal.ZERO;
		}
		
		
		private  BigDecimal getDeal_JZ_VAT(
				String bookId,String accountingSystemId,
				String StrCurrentPeriod,String subKey,Integer type){
			List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerPropertyAndKeyAndType(
					accountingSystemId, EnumFinalLiquidationCode.JZ_VAT.toString(),type);
			if(CollectionUtils.isEmpty(lists)) return BigDecimal.ZERO;

			List<String> summaryIds = lists.stream().map(a -> a.getSubjectId()).collect(Collectors.toList());
			return voucherSummaryService.statisticsAmountBySummaryIds(summaryIds, bookId, CpaVoucherSummary.MONTH_BALANCE);
		}
		
		/**
		 * 15、转出未交增值税 期末结转金额
		 * 2017年12月23日 上午10:20:49
		 * @param bookId
		 * @param accountingSystemId
		 * @param taxpayerPropertyId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_ZC_UNPAID_VAT(String bookId,String accountingSystemId,String taxpayerPropertyId,String subKey){
			
			if(taxpayerPropertyId.equals("2")){ //一般规模纳税人
				 BigDecimal  output = getDeal_JZ_VAT(bookId, accountingSystemId, taxpayerPropertyId, subKey, 3);//2221.002
			 if(null != output) {
				return output;
			  }
			}
		   return BigDecimal.ZERO;
		}
		
		/**
		 * 16、减免税款收入 期末结转金额
		 * 2017年12月23日 上午10:20:49
		 * @param bookId
		 * @param accountingSystemId
		 * @param taxpayerPropertyId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_JM_TAX_REVENUE(String bookId,String accountingSystemId,String taxpayerPropertyId,String subKey){
			
			List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
					taxpayerPropertyId, EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString());
			
			if(CollectionUtils.isEmpty(lists)) return BigDecimal.ZERO;
			
			List<String> idLists = lists.stream().filter(a -> a.isDirection()).map(a -> a.getId()).collect(Collectors.toList());
			
	    	return customerTaxAlgorithmRuleService.sumReductionTaxIncomeAdded(bookId, idLists);
		}
		
		/**
		 * 17、减免税款收入 期末结转金额
		 * 2017年12月23日 上午10:20:49
		 * @param bookId
		 * @param accountingSystemId
		 * @param taxpayerPropertyId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_JM_TAX_REVENUE_SMALL(String bookId,String accountingSystemId,String taxpayerPropertyId,String subKey){
			
			if("2".equals(taxpayerPropertyId)){//小规模
				List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
						taxpayerPropertyId, EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString());
				if(CollectionUtils.isEmpty(lists)) return BigDecimal.ZERO;
				List<String> idLists = lists.stream().filter(a -> a.isDirection()).map(a -> a.getSubjectId()).collect(Collectors.toList());
				
		    	return customerTaxAlgorithmRuleService.reductionTaxIncome(bookId, idLists);
			}
			
			return BigDecimal.ZERO;
		}
		
		/**
		 * 18、计提所得税 期末结转金额
		 * 2017年12月23日 上午10:20:49
		 * @param bookId
		 * @param accountingSystemId
		 * @param taxpayerPropertyId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_JT_INCOME_TAX(String bookId,String accountingSystemId,String taxpayerPropertyId,String subKey){
			return new BigDecimal("0");
		}
		
		/**
		 * 19、计提所得税  期末结转金额
		 * 2017年12月23日 上午10:20:49
		 * @param bookId
		 * @param subKey
		 * @return
		 */
		private BigDecimal deal_JZ_PROFIT(String bookId,String subKey){
			return new BigDecimal("0");
		}

}
