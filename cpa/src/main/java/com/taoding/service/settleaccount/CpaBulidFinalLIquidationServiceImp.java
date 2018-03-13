package com.taoding.service.settleaccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.NumberToCN;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.settleaccount.CpaFinalLiquidationBasic;
import com.taoding.domain.settleaccount.EnumFinalLiquidationCode;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.subject.CpaSubject;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.customerTaxItem.CustomerTaxAlgorithmRuleService;
import com.taoding.service.fixedAsset.FixedAssetService;
import com.taoding.service.salary.CpaSalaryCompanyWelfareService;
import com.taoding.service.salary.CpaSalaryInfoService;
import com.taoding.service.subject.CpaCustomerSubjectService;
import com.taoding.service.subject.CpaSubjectService;
import com.taoding.service.voucher.CpaVoucherSubjectService;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

@SuppressWarnings("all")
@Service
@Transactional(readOnly=true)
public class CpaBulidFinalLIquidationServiceImp implements CpaBulidFinalLIquidationService{
	 
		@Autowired
		private CpaFinalLiquidationCustomerService finalLiquidationCustomerService ;
		@Autowired
		private CpaFinalLiquidationBasicService finalLiquidationBasicService ;
		@Autowired
		private CpaVoucherSummaryService voucherSummaryService ;
		@Autowired
		private FixedAssetService fixedAssetService;//固定资产
		@Autowired
		private CustomerTaxAlgorithmRuleService customerTaxAlgorithmRuleService;
		@Autowired
		private AccountingBookService accountingBookService;
		@Autowired
		private CpaFinalLiquidationProportionService finalLiquidationProportionService;
		@Autowired
		private CpaVoucherSubjectService voucherSubjectService;
		@Autowired
		private CpaSalaryInfoService salaryInfoService; 
		@Autowired
		private CpaSalaryCompanyWelfareService salaryCompanyWelfareService;
		@Autowired 
		private CpaCustomerSubjectService customerSubjectService;
		@Autowired 
	    private CpaFinalLiquidationService finalLiquidationService;
		
		@Override
		public CpaVoucher showBuildVoucher(String bookId, String subKey) {
			

			 Map<String, BigDecimal> debitMap = new  HashMap<String,BigDecimal>();//借方
			 Map<String, BigDecimal> creditMap = new  HashMap<String,BigDecimal>();//贷方
			 
			 EnumFinalLiquidationCode code = Enum.valueOf(EnumFinalLiquidationCode.class, subKey);
			
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
			
			//凭证名称
			String abstractsName = "";
			
			if(EnumFinalLiquidationCode.JZ_SALE_COST == code){ // 结转销售成本
				abstractsName = "结转销售成本";
				build_JZ_SALE_COST(debitMap, creditMap, bookId, accountingSystemId, taxpayerPropertyId, code);
			}else if(EnumFinalLiquidationCode.JT_SALARY == code){ //计提工资
				abstractsName = "计提工资";
				build_JT_SALARY(debitMap, creditMap, bookId,customerId, accountingSystemId, taxpayerPropertyId, code);
			}else if(EnumFinalLiquidationCode.DK_SOCIAL_SECURITY == code){ //代扣五险一金
				abstractsName = "代扣五险一金";
				build_DK_SOCIAL_SECURITY(debitMap, creditMap,bookId, customerId, accountingSystemId, taxpayerPropertyId,code);	
			}else if(EnumFinalLiquidationCode.DK_INCOME_TAX == code){ //代扣个人所得税
				abstractsName = "代扣个人所得税";
				build_DK_INCOME_TAX(debitMap, creditMap, bookId,customerId, accountingSystemId, taxpayerPropertyId,code);	
			}else if(EnumFinalLiquidationCode.JT_COMPANY_SOCIAL_SECURITY == code){ //计提单位承担五险一金
				abstractsName = "计提单位承担五险一金";
				build_JT_COMPANY_SOCIAL_SECURITY(debitMap, creditMap, bookId,customerId, accountingSystemId, taxpayerPropertyId,code);
			}else if(EnumFinalLiquidationCode.GZ_CASH == code){ //现金发放工资
				abstractsName = "现金发放工资";
				build_GZ_CASH(debitMap, creditMap, bookId,customerId, accountingSystemId, taxpayerPropertyId,code);
			}else if(EnumFinalLiquidationCode.GZ_BANK == code){ //银行发放工资
				abstractsName = "银行发放工资";
				build_GZ_BANK(debitMap, creditMap, bookId,customerId, accountingSystemId, taxpayerPropertyId,code);
			}else if(EnumFinalLiquidationCode.JT_DEPRECIATION == code){ //计提折旧
				return  build_JT_DEPRECIATION(accountBook,"计提折旧");
			}else if(EnumFinalLiquidationCode.TX_INTANGIBLE_ASSETS == code){ //无形资产摊销
				return  build_TX_INTANGIBLE_ASSETS(accountBook,"无形资产摊销");
			}else if(EnumFinalLiquidationCode.TX_LONG_APPORTIONED == code){ //长期待摊费用摊销
			   return build_TX_LONG_APPORTIONED(accountBook,"长期待摊费用摊销");
			}else if(EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE == code){ //结转本期损益（收入）
				abstractsName = "结转本期损益";
				build_JZ_CURRENT_PROFIT_REVENUE(debitMap, creditMap, bookId, accountingSystemId, taxpayerPropertyId,code);	
			}else if(EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_COST == code){ //结转本期损益（成本费用）
				abstractsName = "结转本期损益";
				build_JZ_CURRENT_PROFIT_COST(debitMap, creditMap, bookId, accountingSystemId, taxpayerPropertyId,code);
			}else if(EnumFinalLiquidationCode.JT_TAX_ADDITIONAL == code){ //计提税金及附加
				abstractsName = "计提税金及附加";
				build_JT_TAX_ADDITIONAL(debitMap,creditMap,bookId, accountingSystemId, taxpayerPropertyId,EnumFinalLiquidationCode.JT_TAX_ADDITIONAL);
			}else if(EnumFinalLiquidationCode.JZ_VAT == code){ //结转增值税
				abstractsName = "结转增值税";	
				build_JZ_VAT(debitMap,creditMap,bookId, accountingSystemId, taxpayerPropertyId,EnumFinalLiquidationCode.JZ_VAT);	
			}else if(EnumFinalLiquidationCode.ZC_UNPAID_VAT == code ){ //转出未交增值税
				abstractsName = "计提未交增值税";
				build_JZ_VAT(debitMap,creditMap,bookId, accountingSystemId, taxpayerPropertyId,EnumFinalLiquidationCode.JZ_VAT);		
			}else if(EnumFinalLiquidationCode.JM_TAX_REVENUE == code){ //减免税款收入
				abstractsName = "减免税款收入";
				build_JM_TAX_REVENUE(debitMap,creditMap,bookId, accountingSystemId, taxpayerPropertyId,EnumFinalLiquidationCode.JM_TAX_REVENUE);	
			}else if(EnumFinalLiquidationCode.JM_TAX_REVENUE_SMALL == code){ //减免税款收入
				abstractsName = "减免税款收入";
				build_JM_TAX_REVENUE_SMALL(debitMap,creditMap,bookId, accountingSystemId, taxpayerPropertyId,EnumFinalLiquidationCode.JM_TAX_REVENUE_SMALL);
			}else if(EnumFinalLiquidationCode.JT_INCOME_TAX == code){ //计提所得税
				
			}else if(EnumFinalLiquidationCode.JZ_PROFIT == code){ //结转本年利润
				
			}else{
				
			}
			return getVoucherByMapAndName(debitMap, creditMap, abstractsName,accountBook) ;
		}


		/**
		 * 
		* @Description:构建生成凭证
		* @param debitMap 借方科目及金额
		* @param creditMap 贷方科目及金额
		* @param abstractsName 摘要
		* @param accountingBook 账薄 
		* @return CpaVoucher 返回类型   
		* @throws 
		* @author lixc
		* @date 2017年12月28日
		 */
		private CpaVoucher getVoucherByMapAndName( Map<String, BigDecimal> debitMap,
				 Map<String, BigDecimal> creditMap,String abstractsName,AccountingBook accountingBook){

			if(MapUtils.isEmpty(debitMap) || MapUtils.isEmpty(creditMap) || accountingBook == null)
				return null;
			
			CpaVoucher  voucher = new CpaVoucher();
			voucher.setBookId(accountingBook.getId());
			voucher.setCustomerId(accountingBook.getCustomerInfoId());
			voucher.setVoucherPeriod(accountingBook.getCurrentPeriod());
			voucher.setVoucherNo("000");
			voucher.setVoucherDate(new Date());
			voucher.setVoucherType(2);// 资产类凭证
			voucher.setTicketCount(0);// 票据数据voucher
			
			//生成借方科目
			CpaVoucherSubject  subject = null;  
			for ( Map.Entry<String , BigDecimal> entity :debitMap.entrySet()) {
				subject = new CpaVoucherSubject();
				subject.setSubjectId(entity.getKey());
				subject.setAmountDebit(entity.getValue());
				subject.setAbstracts(abstractsName);
				setSubjectNameAndSubjectNoByIdAndBookId(subject,accountingBook.getId());
				voucher.getSubjectLists().add(subject);
			}
			
			//生成贷方科目
			for ( Map.Entry<String , BigDecimal> entity :creditMap.entrySet()) {
				subject = new CpaVoucherSubject();
				subject.setSubjectId(entity.getKey());
				subject.setAmountCredit(entity.getValue());
				subject.setAbstracts(abstractsName);
				setSubjectNameAndSubjectNoByIdAndBookId(subject,accountingBook.getId());
				voucher.getSubjectLists().add(subject);
			}
			
			return voucher;
		}
		
		/**
		 * 
		* @Description: 获得科目名称
		* @param bookId
		* @param subjectParam
		* @throws 
		* @author lixc
		* @date 2017年12月29日
		 */
		private void setSubjectNameAndSubjectNoByIdAndBookId(CpaVoucherSubject subjectParam,String bookId){
			
			CpaCustomerSubject subject = customerSubjectService.getById(bookId, subjectParam.getSubjectId());
			if(null != subject){
				subjectParam.setSubjectName(subject.getSubjectName());
				subjectParam.setSubjectNo(subject.getSubjectNo());
			}
		}
		/**
		 * 
		* @Description: 计提折旧展示凭证
		* @param bookId
		* @param bstractsName 摘要名称
		* @param 凭证名称
		* @return CpaVoucher 返回类型    
		* @throws 
		* @author lixc
		* @date 2017年12月23日
		 */
		private CpaVoucher  build_JT_DEPRECIATION (AccountingBook accountingBook,String abstractsName){
			return getVoucherAmount(accountingBook, abstractsName,AssetType.FIXED_ASSET_TYPE);
		}
		
		/**
		 * 
		* @Description: 无形资产摊销
		* @param bookId
		* @param bstractsName 摘要名称
		* @param 凭证名称
		* @return CpaVoucher 返回类型    
		* @throws 
		* @author lixc
		* @date 2017年12月23日
		 */
		private CpaVoucher  build_TX_INTANGIBLE_ASSETS  (AccountingBook accountingBook,String abstractsName){
			return getVoucherAmount(accountingBook, abstractsName,AssetType.INTANGIBLE_ASSET_TYPE);
		}
		
		/**
		 * 
		* @Description: 长期待摊费用摊销
		* @param bookId
		* @param bstractsName 摘要名称
		* @param 凭证名称
		* @return CpaVoucher 返回类型    
		* @throws 
		* @author lixc
		* @date 2017年12月23日
		 */
		private CpaVoucher  build_TX_LONG_APPORTIONED  (AccountingBook accountingBook,String abstractsName){
			return getVoucherAmount(accountingBook, abstractsName,AssetType.APPORTIONED_ASSET_TYPE);
		}
		
		/**
		 * 
		* @Description:   
		* @param accountingBook
		* @param astractsName 摘要
		* @param type 1 固定 2 无形 3 长期摊销
		* @param voucher void 返回类型    
		* @throws 
		* @author lixc
		* @date 2017年12月23日
		 */
		private CpaVoucher getVoucherAmount(AccountingBook accountingBook,
				String astractsName, int type) {
			if (accountingBook == null) {
				throw new LogicException("账簿不能为空！");
			}
			CpaVoucher voucher = new CpaVoucher();
			voucher.setBookId(accountingBook.getId());
			voucher.setCustomerId(accountingBook.getCustomerInfoId());
			voucher.setVoucherPeriod(accountingBook.getCurrentPeriod());
			voucher.setVoucherNo("000");
			voucher.setVoucherDate(new Date());
			voucher.setVoucherType(2);// 资产类凭证
			voucher.setTicketCount(0);// 票据数据
			List<CpaVoucherSubject> list = fixedAssetService.getSubjectAndSumthisMonthDepreciation(accountingBook.getId(),
							type, astractsName);

			// 求和
			Iterator<CpaVoucherSubject> iterator = list.iterator();
			CpaVoucherSubject subject = null;
			while (iterator.hasNext()) {
				subject = iterator.next();
				if (null != subject.getAmountCredit()) {
					voucher.setAmountCredit(subject.getAmountCredit().add(voucher.getAmountCredit()));
				}

				if (null != subject.getAmountDebit()) {
					voucher.setAmountDebit(subject.getAmountDebit().add(voucher.getAmountDebit()));
				}
			}
			voucher.setAccountCapital(NumberToCN.number2CNMontrayUnit(voucher.getAmountDebit()));
			return voucher;
		}
		
		/**
		 * 
		* @Description: 结转增值税 只有三条 数据不能复用
		* @param debitMap
		* @param creditMap
		* @param bookId
		* @param accountingSystemId
		* @param taxpayerPropertyId
		* @param subKey void 返回类型    
		* @throws 
		* @author lixc
		* @date 2017年12月25日
		 */
		private void build_JZ_VAT( Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap,
				String bookId,String accountingSystemId,String taxpayerPropertyId,EnumFinalLiquidationCode subKey) {
			
			if (taxpayerPropertyId.equals("2")) { // 一般纳税人

				List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService
						.findAllListByTaxpayerProperty(accountingSystemId,
								EnumFinalLiquidationCode.JZ_VAT.toString());// 应交税费-应交增值税-销项税额

				if (CollectionUtils.isEmpty(lists)) return;

				BigDecimal debitSum = BigDecimal.ZERO;
				BigDecimal creditSum = BigDecimal.ZERO;
				List<String> summaryIds = null;
				
				CpaFinalLIquidationTool.sortCpaFinalLiquidationBasicListByTypeUp(lists);

				for (CpaFinalLiquidationBasic cpaFinalLiquidationBasic : lists) {
					// 处理借方
					if (1 == cpaFinalLiquidationBasic.getType()) {
						summaryIds=new ArrayList<String>();
						summaryIds.add(cpaFinalLiquidationBasic.getSubjectId());
						debitSum = voucherSummaryService.statisticsAmountBySummaryIds(summaryIds, bookId, CpaVoucherSummary.MONTH_BALANCE);
						debitMap.put(cpaFinalLiquidationBasic.getSubjectId(),debitSum);

					} else if (2 == cpaFinalLiquidationBasic.getType()) {// 处理贷方
						summaryIds=new ArrayList<String>();
						summaryIds.add(cpaFinalLiquidationBasic.getSubjectId());
						creditSum = voucherSummaryService.statisticsAmountBySummaryIds(summaryIds, bookId, CpaVoucherSummary.MONTH_BALANCE);
						creditMap.put(cpaFinalLiquidationBasic.getSubjectId(),creditSum);

					} else if (3 == cpaFinalLiquidationBasic.getType()) { // 处理贷方
						summaryIds=new ArrayList<String>();
						summaryIds.add(cpaFinalLiquidationBasic.getSubjectId());
						if (debitSum.compareTo(creditSum) >= 0) {
							debitMap.put(cpaFinalLiquidationBasic.getSubjectId(),debitSum.subtract(creditSum));
						} else {
							creditMap.put(cpaFinalLiquidationBasic.getSubjectId(),creditSum.subtract(debitSum));
						}
					}
				}
			}
			}
		
		
		/**
		* @Description: 15 转出未交增值税 只有三条 数据不能复用
		* @param debitMap
		* @param creditMap
		* @param bookId
		* @param accountingSystemId
		* @param taxpayerPropertyId
		* @param subKey void 返回类型    
		* @throws 
		* @author lixc
		* @date 2017年12月25日
		 */
		private void build_ZC_UNPAID_VAT( Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap,
				String bookId,String accountingSystemId,String taxpayerPropertyId,EnumFinalLiquidationCode subKey) {
			
			if (taxpayerPropertyId.equals("2")) { // 一般纳税人
				
				//应交税费-应交增值税-转出未交增值税
				List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerPropertyAndKeyAndType(
						accountingSystemId, EnumFinalLiquidationCode.JZ_VAT.toString(),3);
				if(CollectionUtils.isEmpty(lists)) return ;
				List<String> summaryIds = lists.stream().map(a -> a.getSubjectId()).collect(Collectors.toList());
	            BigDecimal sum = voucherSummaryService.statisticsAmountBySummaryIds(summaryIds, bookId, 1);
	            
	            
	             //应交税费-未交增值税
	            lists = finalLiquidationBasicService.findAllListByTaxpayerPropertyAndKeyAndType(
				accountingSystemId, EnumFinalLiquidationCode.JZ_VAT.toString(),3);
				if(CollectionUtils.isEmpty(lists)) return ;
				List<String> summaryIdsTmp = lists.stream().map(a -> a.getSubjectId()).collect(Collectors.toList());
	            BigDecimal sumTemp = voucherSummaryService.statisticsAmountBySummaryIds(summaryIds, bookId, 1);
	            
	            if(null != sum){
	            	if(sum.compareTo(BigDecimal.ZERO) >= 0){
	            		debitMap.put(summaryIds.get(0), sum);
	            		creditMap.put(summaryIdsTmp.get(0), sumTemp);
	            	}else{
	            		creditMap.put(summaryIds.get(0), sum);
	            		debitMap.put(summaryIdsTmp.get(0), sumTemp);
	            	}
	            }
			}
			}
		
		
		
		/**
		 * 
		* @Description: 计提税金及附加 
		* @param debitList 借方
		* @param creditList 贷方
		* @param bookId
		* @param accountingSystemId
		* @param taxpayerPropertyId
		* @param subKey
		* @throws 
		* @author lixc
		* @date 2017年12月25日
		 */
		private void build_JT_TAX_ADDITIONAL(
				Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,
				String accountingSystemId,String taxpayerPropertyId,EnumFinalLiquidationCode subKey){

			if(taxpayerPropertyId.equals("1")){ //小规模纳税人
				
				List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
						accountingSystemId, EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString());
				
				List<String> subjectIds = new ArrayList<String>();
				if(lists != null && lists.size() > 0){
					for (CpaFinalLiquidationBasic liquidationBasic : lists) {
						if(liquidationBasic != null && StringUtils.isNotEmpty(
								liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()){
							subjectIds.add(liquidationBasic.getSubjectId());
						}	
					}
				}
				
				if(CollectionUtils.isNotEmpty(subjectIds)){
					creditMap.putAll(customerTaxAlgorithmRuleService.customerAccruedTaxAdditiveSmallScale(bookId, subjectIds));  
				}
				
			}else{ //一般纳税人
				String outputSubjectId = "";//销项税额ID
				String inputSubjectId = "" ;//进项项税额ID
				
				List<CpaFinalLiquidationBasic> putLists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
						accountingSystemId,subKey.toString());
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
					creditMap.putAll(customerTaxAlgorithmRuleService.customerAccruedTaxAdditiveGeneral(bookId, inputSubjectId, outputSubjectId));  
				}
			}
			
			
			//处理借方数据 计提税金及附加
			List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerPropertyAndKeyAndType(
					accountingSystemId, EnumFinalLiquidationCode.JT_TAX_ADDITIONAL.toString(),1);
			
			CpaFinalLiquidationBasic cpaFinalLiquidationBasic = null;
			
	       for (Iterator<CpaFinalLiquidationBasic> iterator = lists.iterator(); iterator.hasNext();) {
			  cpaFinalLiquidationBasic = iterator.next();
			  if(Global.TAX_AND_ADDITIONAL.equals(cpaFinalLiquidationBasic.getSubjectNo())){//税金附加
				  debitMap.put(cpaFinalLiquidationBasic.getSubjectId(), CpaFinalLIquidationTool.sumMapValue(creditMap));
				  break;
			  }
		}
	       
		}
		
		/**
		 * 
		* @Description: 1 、结转销售成本  
		* @param debitList 借方
		* @param creditList 贷方
		* @param bookId
		* @param accountingSystemId
		* @param taxpayerPropertyId
		* @param subKey
		* @throws 
		* @author lixc
		* @date 2017年12月25日
		 */
	private void build_JZ_SALE_COST(Map<String, BigDecimal> debitMap,Map<String, BigDecimal> creditMap, String bookId,
			String accountingSystemId, String taxpayerPropertyId,EnumFinalLiquidationCode subKey) {
		
		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,subKey.toString());

		List<String> subjectIds = new ArrayList<String>();
		//借方科目ID
		String subjectId = "";
		if (CollectionUtils.isNotEmpty(lists)) {
			for (CpaFinalLiquidationBasic liquidationBasic : lists) {
				if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
					subjectIds.add(liquidationBasic.getSubjectId());
				}else{
					subjectId = liquidationBasic.getSubjectId();
				}
			}
		}

		//要生成的贷方科目
		List<CpaVoucherSubject> subjectList = voucherSubjectService.dealSaleCostVoucherSubjectInfo(bookId, subjectIds);
		
		CpaVoucherSubject cpaVoucherSubject = null;
		BigDecimal sum = BigDecimal.ZERO; 
		for (Iterator<CpaVoucherSubject> iterator = subjectList.iterator(); iterator.hasNext();) {
			cpaVoucherSubject = iterator.next();
			if(null != cpaVoucherSubject && StringUtils.isNotBlank(cpaVoucherSubject.getId())){
				creditMap.put(cpaVoucherSubject.getId(), cpaVoucherSubject.getSurplusAmount());
				sum = sum.add(cpaVoucherSubject.getSurplusAmount());
			}
		}
		 debitMap.put(subjectId, sum);
	}
	
	/**
	 * 
	* @Description: 2、计提工资
	* @param debitList 借方
	* @param creditList 贷方
	* @param customerId 客户ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_JT_SALARY(Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,String customerId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey) {

		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,subKey.toString());

		if (CollectionUtils.isNotEmpty(lists)) {

			Double d = salaryInfoService.getAccruedSalary(bookId,customerId);
			BigDecimal big = BigDecimal.ZERO;
			if (null != d) {
				big = new BigDecimal(d);
			}

			for (CpaFinalLiquidationBasic liquidationBasic : lists) {
				
				if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
					//借方
					debitMap.put(liquidationBasic.getSubjectId(), big);
				} else if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && !liquidationBasic.isDirection()) {
					//贷方
					creditMap.put(liquidationBasic.getSubjectId(), big);
				}
			}
		}

	}
	
	/**
	* @Description: 3、代扣五险一金
	* @param debitList 借方
	* @param creditList 贷方
	* @param customerId 客户ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_DK_SOCIAL_SECURITY(Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,String customerId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey) {
		
		Map<String, BigDecimal> map = salaryInfoService.getIndividualSocialSecurity(bookId,customerId);
		
		if(null == map) return ;
		 BigDecimal sum = BigDecimal.ZERO;
		 for (Iterator<BigDecimal> iterator = map.values().iterator(); iterator.hasNext();) {
			 sum = sum.add(iterator.next());
		}
		 

	List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,subKey.toString());
	if(CollectionUtils.isEmpty(lists)) return ;
	
	for (CpaFinalLiquidationBasic liquidationBasic : lists) {
		
		if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
			//借方
			debitMap.put(liquidationBasic.getSubjectId(), sum);
		} else if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && !liquidationBasic.isDirection()) {
			//贷方
			if(2 == liquidationBasic.getType()){//其他应付款-代扣社保及公积金-养老保险
			creditMap.put(liquidationBasic.getSubjectId(), map.get("yanglaoIndividual"));	
			}else if(3 == liquidationBasic.getType()){//其他应付款-代扣社保及公积金-医疗保险
			creditMap.put(liquidationBasic.getSubjectId(), map.get("yiliaoIndividual"));
			}else if(4 == liquidationBasic.getType()){//其他应付款-代扣社保及公积金-失业保险
			creditMap.put(liquidationBasic.getSubjectId(), map.get("shiyeIndividual"));
			}else if(5 == liquidationBasic.getType()){//其他应付款-代扣社保及公积金-住房公积金
			creditMap.put(liquidationBasic.getSubjectId(), map.get("gongjijinIndividual"));
			}
		}
	}
	}
	
	
	/**
	* @Description: 4、代扣个人所得税
	* @param debitList 借方
	* @param creditList 贷方
	* @param customerId 客户ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_DK_INCOME_TAX(
			Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,String customerId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey){
		
		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,subKey.toString());
		if(CollectionUtils.isEmpty(lists)) return ;
		
		Double d = salaryInfoService.getIndividualTax(bookId,customerId);
		BigDecimal sum = BigDecimal.ZERO;
		if(null != d){
			sum = new BigDecimal(d);
		}
		
		for (CpaFinalLiquidationBasic liquidationBasic : lists) {
			if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
				//借方
				debitMap.put(liquidationBasic.getSubjectId(), sum);
			} else if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && !liquidationBasic.isDirection()) {
				//贷方
				creditMap.put(liquidationBasic.getSubjectId(), sum);	
			}
		}
		
	}
	
	
	/**
	* @Description: 5、计提单位承担五险一金
	* @param debitList 借方
	* @param creditList 贷方
	* @param customerId 客户ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_JT_COMPANY_SOCIAL_SECURITY(
			Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,String customerId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey){
		
			Map<String, BigDecimal> map = salaryCompanyWelfareService.getCompanySocialSecurity(bookId,customerId);
			if(null == map) return ;
			 BigDecimal sum = BigDecimal.ZERO;
			 for (Iterator<BigDecimal> iterator = map.values().iterator(); iterator.hasNext();) {
				 sum = sum.add(iterator.next());
			}
			 

		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,subKey.toString());
		if(CollectionUtils.isEmpty(lists)) return ;
		
		for (CpaFinalLiquidationBasic liquidationBasic : lists) {
			
			if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
				//借方
				debitMap.put(liquidationBasic.getSubjectId(), sum);
			} else if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && !liquidationBasic.isDirection()) {
				//贷方
				if(2 == liquidationBasic.getType()){//应付职工薪酬-社保及公积金-养老保险
				creditMap.put(liquidationBasic.getSubjectId(), map.get("yanglaoCompany"));	
				}else if(3 == liquidationBasic.getType()){//应付职工薪酬-社保及公积金-医疗保险
				creditMap.put(liquidationBasic.getSubjectId(), map.get("yiliaoCompany"));
				}else if(4 == liquidationBasic.getType()){//应付职工薪酬-社保及公积金-失业保险
				creditMap.put(liquidationBasic.getSubjectId(), map.get("shiyeCompany"));
				}else if(5 == liquidationBasic.getType()){//应付职工薪酬-社保及公积金-工伤保险
				creditMap.put(liquidationBasic.getSubjectId(), map.get("gongshangCompany"));
				}else if(6 == liquidationBasic.getType()){//应付职工薪酬-社保及公积金-生育保险
				creditMap.put(liquidationBasic.getSubjectId(), map.get("shengyuCompany"));
				}else if(7 == liquidationBasic.getType()){//应付职工薪酬-社保及公积金-住房公积金
				creditMap.put(liquidationBasic.getSubjectId(), map.get("gongjijinCompany"));
				}
			}
		}
	}
	
	/**
	* @Description: 6、现金发放工资(当月计提当月发)
	* @param debitList 借方
	* @param creditList 贷方
	* @param customerId 客户ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_GZ_CASH(
			Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,String customerId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey){
		
		
		Double d = salaryInfoService.getCashSalary(bookId,customerId);
		BigDecimal sum = BigDecimal.ZERO;
		if(null != d){
			sum = new BigDecimal(d);
		}

		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,subKey.toString());
		if(CollectionUtils.isEmpty(lists)) return ;
		
		for (CpaFinalLiquidationBasic liquidationBasic : lists) {
			
			if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
				//借方
				debitMap.put(liquidationBasic.getSubjectId(), sum);
			} else if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && !liquidationBasic.isDirection()) {
				//贷方
				creditMap.put(liquidationBasic.getSubjectId(), sum);	
			}
		}
	}
	
	/**
	* @Description: 7、银行发放工资(当月计提下月发)
	* @param debitList 借方
	* @param creditList 贷方
	* @param customerId 客户ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_GZ_BANK(
			Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,String customerId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey){
		
		Double d = salaryInfoService.getBankSalary(bookId,customerId);
		BigDecimal sum = BigDecimal.ZERO;
		if(null != d){
			sum = new BigDecimal(d);
		}

		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,subKey.toString());
		if(CollectionUtils.isEmpty(lists)) return ;
		
		for (CpaFinalLiquidationBasic liquidationBasic : lists) {
			if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
				//借方
				debitMap.put(liquidationBasic.getSubjectId(), sum);
			} else if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && !liquidationBasic.isDirection()) {
				//贷方
				creditMap.put(liquidationBasic.getSubjectId(), sum);	
			}
		}
	}
	
	
	/**
	* @Description: 11、结转本期损益（收入）
	* @param debitList 借方
	* @param creditList 贷方
	* @param bookId 客户ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_JZ_CURRENT_PROFIT_REVENUE(
			Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey){
		
		//查询收入类四项ID
		List<String> subjectIds = finalLiquidationService.get_JZ_CURRENT_PROFIT_REVENUE_List(bookId);
		
		List<CpaVoucherSubject> subjectLists = voucherSubjectService.dealProfitRevenueVoucherSubjectInfo(bookId, subjectIds);
		
//	    BigDecimal resultSum = BigDecimal.ZERO;
//		 //公允价值变动损益
//		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,EnumFinalLiquidationCode.FAIR_VALUE_PROFIT.toString());
//		if(CollectionUtils.isNotEmpty(lists)){
//			CpaFinalLiquidationBasic liquidationBasic = lists.get(0);
//			List<String> tempList = new ArrayList<String>();
//			tempList.add(liquidationBasic.getSubjectId());
//			BigDecimal temp = voucherSummaryService.statisticsAmountBySummaryIds(tempList , bookId, 1);
//			if(null != temp && temp.compareTo(BigDecimal.ZERO) >= 0){
//					resultSum = resultSum.add(temp);
//					debitMap.put(liquidationBasic.getSubjectId(), temp);
//			}
//		}
//		
//		//基础
//		 lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString());
//		if(CollectionUtils.isEmpty(lists)) return ;
//		//排序
//		CpaFinalLIquidationTool.sortCpaFinalLiquidationBasicListByTypeUp(lists);
//		
//		for (CpaFinalLiquidationBasic liquidationBasic : lists) {
//			if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
//				
//				//借方
//				List<String> list = new ArrayList<String>();
//				list.add(liquidationBasic.getSubjectId());
//				
//				BigDecimal temp = voucherSummaryService.statisticsAmountBySummaryIds(list, bookId, CpaVoucherSummary.MONTH_BALANCE);
//				
//				debitMap.put(liquidationBasic.getSubjectId(), temp);	
//				resultSum = resultSum.add(temp);
//				
//			} else if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && !liquidationBasic.isDirection()) {
//				//贷方
//					creditMap.put(liquidationBasic.getSubjectId(), resultSum);	
//			}
//		}
		
	}
	/**
	* @Description: 12、结转本期损益（成本费用）
	* @param debitList 借方
	* @param creditList 贷方
	* @param bookId 账薄ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_JZ_CURRENT_PROFIT_COST(
			Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey){
		
		//查询收入类四项ID
		List<String> subjectIds = finalLiquidationService.get_JZ_CURRENT_PROFIT_REVENUE_List(bookId);
		List<CpaVoucherSubject> subjectLists = voucherSubjectService.dealProfitCostVoucherSubjectInfo(bookId, subjectIds);
		
//		//收入四项
//		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
//				accountingSystemId, EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString());
//		
//		List<String> subjectIds = new ArrayList<String>();
//		if(CollectionUtils.isNotEmpty(lists)){
//			for (CpaFinalLiquidationBasic liquidationBasic : lists) {
//				if(liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()){
//					subjectIds.add(liquidationBasic.getSubjectId());
//				}	
//			}
//		}
		
		//公允价值变动损益
//		lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,EnumFinalLiquidationCode.FAIR_VALUE_PROFIT.toString());
//		if(CollectionUtils.isNotEmpty(lists)){
//			CpaFinalLiquidationBasic liquidationBasic = lists.get(0);
//			
//			List<String> tempList = new ArrayList<String>();
//			tempList.add(liquidationBasic.getSubjectId());
//			BigDecimal temp = voucherSummaryService.statisticsAmountBySummaryIds(tempList , bookId, 1);
//			
//			if(null != temp && temp.compareTo(BigDecimal.ZERO) < 0){
//				subjectIds.add(liquidationBasic.getSubjectId()); 
//			}
//		}
		
		
//		BigDecimal resultSum = BigDecimal.ZERO;
//		if(CollectionUtils.isNotEmpty(subjectIds)){
//			//除去收入四项的列表
//			Map<String, BigDecimal> map = voucherSubjectService.findByParentSubjectIdProfitAndLssStatistics(bookId, subjectIds);
//			if(null != map){
//				resultSum = map.get("totalAamountDebit");
//			}
//		}
		
		//借方结转本期损益（成本费用） 借方 
//		lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_COST.toString());
//		if(CollectionUtils.isNotEmpty(lists)){
//			CpaFinalLiquidationBasic liquidationBasic = lists.get(0);
//			debitMap.put(liquidationBasic.getSubjectId(), resultSum);
//		}
//		
//		//贷方处理 除去基础收入四项
//		CpaVoucherSubject cpaVoucherSubject = null;
//		List<CpaVoucherSubject> sujbectList = voucherSubjectService.findByParentSubjectIdProfitAndLss(bookId, subjectIds);
//		
//		if(CollectionUtils.isNotEmpty(sujbectList)){
//			for (Iterator<CpaVoucherSubject> iterator = sujbectList.iterator(); iterator.hasNext();) {
//				  cpaVoucherSubject = iterator.next();
//				  if(null != cpaVoucherSubject){
//					  creditMap.put(cpaVoucherSubject.getSubjectId(), cpaVoucherSubject.getAmountCredit());
//				  }
//			}
//		}
	}
	
	/**
	* @Description: 16、减免税款收入
	* @param debitList 借方
	* @param creditList 贷方
	* @param bookId 账薄ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_JM_TAX_REVENUE(
			Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey){
		
		if("2".equals(taxpayerPropertyId)){//小规模
			List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
					taxpayerPropertyId, EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString());
			
			if(CollectionUtils.isEmpty(lists)) return ;
			//基础收入四项
			List<String> idLists = lists.stream().filter(a -> a.isDirection()).map(a -> a.getId()).collect(Collectors.toList());
			
			Map<String, BigDecimal> map = customerTaxAlgorithmRuleService.reductionTaxIncomeAdded(bookId, idLists);
			BigDecimal sum  = BigDecimal.ZERO;
			
			if(null != map){
				for (Iterator<BigDecimal> iterator = map.values().iterator(); iterator.hasNext();) {
					sum = sum.add(iterator.next());
				}
				debitMap.putAll(map);//借方
			}
			
			//贷方
			 lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
					taxpayerPropertyId, EnumFinalLiquidationCode.JM_TAX_REVENUE.toString());
				for (CpaFinalLiquidationBasic liquidationBasic : lists) {
					if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && !liquidationBasic.isDirection()) {
						creditMap.put(liquidationBasic.getSubjectId(), sum);	
					}  
				}
		}
	}
	
	/**
	* @Description: 17、减免税款收入
	* @param debitList 借方
	* @param creditList 贷方
	* @param bookId 账薄ID
	* @param accountingSystemId
	* @param taxpayerPropertyId
	* @param subKey
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	private void build_JM_TAX_REVENUE_SMALL(
			Map<String, BigDecimal> debitMap, Map<String, BigDecimal> creditMap, String bookId,
			String accountingSystemId, String taxpayerPropertyId, EnumFinalLiquidationCode subKey){
		
		if("2".equals(taxpayerPropertyId)){//小规模
			
			//收入四项
			List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
					taxpayerPropertyId, EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString());
			
			if(CollectionUtils.isEmpty(lists)) return ;
			
			BigDecimal sum = BigDecimal.ZERO;
			List<String> idLists = lists.stream().filter(a -> a.isDirection()).map(a -> a.getSubjectId()).collect(Collectors.toList());
			
			sum = customerTaxAlgorithmRuleService.reductionTaxIncome(bookId, idLists);
			
			 lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
					taxpayerPropertyId, EnumFinalLiquidationCode.JM_TAX_REVENUE_SMALL.toString());
				for (CpaFinalLiquidationBasic liquidationBasic : lists) {
					if (liquidationBasic != null && StringUtils.isNotEmpty(liquidationBasic.getSubjectId()) && liquidationBasic.isDirection()) {
						debitMap.put(liquidationBasic.getSubjectId(), sum);	
					}else{
						creditMap.put(liquidationBasic.getSubjectId(), sum);	
					}
				}
			
		}
		
	}
	
	}

