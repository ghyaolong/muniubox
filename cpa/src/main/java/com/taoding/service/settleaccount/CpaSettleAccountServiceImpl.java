package com.taoding.service.settleaccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.settleaccount.AssetClassBalanceVO;
import com.taoding.domain.settleaccount.CpaSettleAccount;
import com.taoding.domain.settleaccount.CpaSettleAccountSubjectBasic;
import com.taoding.domain.settleaccount.EnumSettleAccountCode;
import com.taoding.domain.user.User;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.mapper.settleaccount.CpaSettleAccountDao;
import com.taoding.mapper.settleaccount.CpaSettleAccountSubjectBasicDao;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.salary.CpaSalaryInfoService;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

@SuppressWarnings("all")
@Service
public class CpaSettleAccountServiceImpl implements CpaSettleAccountService{

	@Autowired
	private CpaSettleAccountSubjectBasicDao accountSubjectBasicDao;
	@Autowired
	private CpaVoucherSummaryService voucherSummaryService;
	@Autowired
	private AccountingBookService accountingBookService;
	@Autowired
	private CpaFinalLiquidationService finalLiquidationService;
	@Autowired
	private CpaSalaryInfoService salaryInfoService;
	@Autowired
	private CpaSettleAccountDao accountDao;
	
	/**
	 * 资产类科目余额
	 * bookId 账薄 id
	 */
	@Override
	public List<AssetClassBalanceVO> assetClassBalance(String bookId) {
		
		List<AssetClassBalanceVO> lists = new ArrayList<AssetClassBalanceVO>() ;
		
		AccountingBook accountBook = accountingBookService.get(bookId);
		if(accountBook == null || StringUtils.isEmpty(accountBook.getId())){
			throw new LogicException("获取不到当前账簿信息");
		}
		//企业性质
		String enterpriseType = accountBook.getAccountingSystemId();
		lists.add(assetClassBalanceMethod(bookId, enterpriseType, EnumSettleAccountCode.ASSETS_CASH.toString(),"库存现金"));
		lists.add(assetClassBalanceMethod(bookId, enterpriseType, EnumSettleAccountCode.ASSETS_DEPOSIT.toString(),"银行存款"));
		lists.add(assetClassBalanceMethod(bookId, enterpriseType, EnumSettleAccountCode.ASSETS_RAW_MATERIAL.toString(),"原材料"));
		lists.add(assetClassBalanceMethod(bookId, enterpriseType, EnumSettleAccountCode.ASSETS_INVENTORY_GOODS.toString(),"库存商品"));
		lists.add(assetClassBalanceMethod(bookId, enterpriseType, EnumSettleAccountCode.ASSETS_FIXED_ASSETS.toString(), EnumSettleAccountCode.ASSETS_DEPRECIATION.toString(), "固定资产"));
		lists.add(assetClassBalanceMethod(bookId, enterpriseType, EnumSettleAccountCode.ASSETS_INTANGIBLE_ASSETS.toString(), EnumSettleAccountCode.ASSETS_AMORTIZATION.toString(),"无形资产"));
		lists.add(assetClassBalanceMethod(bookId, enterpriseType, EnumSettleAccountCode.ASSETS_APPORTIONED_COST.toString(),"长期待摊费用"));
		
		return lists;
	}

	/**
	 * 处理资产类科目
	 * 2017年12月27日 上午10:06:39
	 * @param bookId
	 * @param enterpriseType
	 * @param subjectKey
	 * @return
	 */
	private AssetClassBalanceVO assetClassBalanceMethod(String bookId, String enterpriseType, String subjectKey,String subjectName){
		
		// 结算科目基础 信息
		List<CpaSettleAccountSubjectBasic> subjectBasicLists = accountSubjectBasicDao.getByEnterpriseTypeAndSubKey(enterpriseType, subjectKey);
		if(subjectBasicLists == null || subjectBasicLists.size() == 0){
			throw new LogicException("获取不到结算科目基础信息");
		}
		boolean flag = true ;
		String message = "无赤字";
		
		CpaVoucherSummary voucherSummary = voucherSummaryService.findInfoAndAmountById(subjectBasicLists.get(0).getSubjectId(), bookId,CpaVoucherSummary.CURRENT_DATA);
		if(voucherSummary != null && voucherSummary.getBalance() != null){
			int compare = voucherSummary.getBalance().compareTo(new BigDecimal(0)) ;
			if(compare < 0){
				flag = false ;
				message = "有赤字";
			}
		}
		return new AssetClassBalanceVO(subjectKey, subjectName, flag, message);
	}
	

	/**
	 * 处理资产类科目---固定资产和无形资产
	 * 2017年12月27日 上午10:06:39
	 * @param bookId
	 * @param enterpriseType
	 * @param subjectKey
	 * @return
	 */
	private AssetClassBalanceVO assetClassBalanceMethod(String bookId, String enterpriseType, String subjectKey, String subjectKey2,String subjectName){
		
		// 结算科目基础 信息
		List<CpaSettleAccountSubjectBasic> subjectBasicLists = accountSubjectBasicDao.getByEnterpriseTypeAndSubKey(enterpriseType, subjectKey);
		if(subjectBasicLists == null || subjectBasicLists.size() == 0){
			throw new LogicException("获取不到结算科目基础信息");
		}
		boolean flag = true ;
		String message = "无赤字";
		
		CpaVoucherSummary voucherSummary = voucherSummaryService.findInfoAndAmountById(subjectBasicLists.get(0).getSubjectId(), bookId,2);
		
		// 结算科目基础 信息 2
		List<CpaSettleAccountSubjectBasic> subjectBasicLists2 = accountSubjectBasicDao.getByEnterpriseTypeAndSubKey(enterpriseType, subjectKey2);
		if(subjectBasicLists2 == null || subjectBasicLists2.size() == 0){
			throw new LogicException("获取不到结算科目基础信息");
		}
		CpaVoucherSummary voucherSummary2 = voucherSummaryService.findInfoAndAmountById(subjectBasicLists2.get(0).getSubjectId(), bookId,2);
		
		if(voucherSummary != null && voucherSummary.getBalance() != null && voucherSummary2 != null && voucherSummary2.getBalance() != null){
			// 计算 差额
			BigDecimal difference = voucherSummary.getBalance().subtract(voucherSummary2.getBalance());
			int compare = difference.compareTo(new BigDecimal(0)) ;
			if(compare < 0){
				flag = false ;
				message = "有赤字";
			}
		}
		return new AssetClassBalanceVO(subjectKey, subjectName, flag, message);
	}

	
	/**
	 * 费用控制及纳税调整
	 */
	@Override
	public List<AssetClassBalanceVO> costControlAndTaxAdjustment(String bookId) {
		
		List<AssetClassBalanceVO> listVO = new ArrayList<AssetClassBalanceVO>() ;
		AccountingBook accountBook = accountingBookService.get(bookId);
		String customerId = accountBook.getCustomerInfoId();
		if(accountBook == null || StringUtils.isEmpty(accountBook.getId()) || StringUtils.isEmpty(customerId)){
			throw new LogicException("获取不到当前账簿信息");
		}
		//企业性质
		String enterpriseType = accountBook.getAccountingSystemId();
		// 业务招待费
		listVO.add(businessHospitality(bookId, enterpriseType, "业务招待费"));
		// 广告和业务宣传费  
		listVO.add(advertisingAndBusinessPromotionFees(bookId, enterpriseType, "广告和业务宣传费"));
		// 工会经费
		listVO.add(unionFunds(bookId, enterpriseType, "工会经费", customerId));
		// 职工教育经费
		listVO.add(employeeEducationExpenses(bookId, enterpriseType, "职工教育经费", customerId));
		
		return listVO;
	}
	
	/**
	 * 结账 按钮 保存的信息
	 */
	@Override
	@Transactional
	public Object saveSettleAccount(String bookId, String customerId) {
		// 获取当前账期
		String period = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		// 查询是否存在 结账信息
		CpaSettleAccount cpaSettleAccount = accountDao.findByCustomerIdAndCurrentPeriod(bookId,  period);
		if(cpaSettleAccount != null){
			int count = accountDao.updateCpaSettleAccountByOne(bookId, period);
			if(count > 0){
				return true;
			}
			return false;
		}else {
		    CpaSettleAccount account = new CpaSettleAccount();
		    account.setId(UUIDUtils.getUUid());
		    account.setBookId(bookId);
		    account.setCustomerId(customerId);
		    
		    User createBy = new User();
		    createBy.setId(UserUtils.getCurrentUserId());
		    
		    account.setCreateBy(createBy);
		    account.setCreateDate(new Date());
		    account.setSettleAccounts(true);
		    account.setCurrentPeriod(period);
		    
	    	int count = accountDao.insertAccount(account);
		    if(count > 0){
		    	return true;
		    }	
		    return false ;
		}
	}

	/**
	 * 反结账 
	 * @param bookId
	 * @param customerId
	 */
	@Override
	@Transactional
	public Object antiSettleAccount(String bookId, String currentPeriod) {
		int count = accountDao.updateCpaSettleAccountByList(bookId, currentPeriod);
		if(count > 0){
			return true ;
		}
		return false ;
	}

	/**
	 * 返回 结账 信息状态
	 * @param bookId
	 * @param customerId
	 * @return
	 */
	@Override
	public Object dealSettleAccountState(String bookId, String currentPeriod) {
		// 查询是否存在 结账信息
		CpaSettleAccount settleAccount = accountDao.findByCustomerIdAndCurrentPeriod(bookId, currentPeriod);
		if(settleAccount != null && StringUtils.isNotEmpty(settleAccount.getId())){
			return settleAccount.isSettleAccounts();
		}
		return false;
	}
	
	/**
	 * 业务招待费
	 * 2017年12月29日 下午1:58:48
	 * @param bookId
	 * @param enterpriseType
	 * @param subjectName
	 * @return
	 */
	private AssetClassBalanceVO businessHospitality(String bookId, String enterpriseType, String subjectName){
		String subjectKey = "";
		if(enterpriseType.equals("1")){
			subjectKey = EnumSettleAccountCode.COST_SMALL_ENTERPRISE_HOSPITALITY.toString(); // 小企业 - 业务招待费
		}
		if(enterpriseType.equals("2")){
			subjectKey = EnumSettleAccountCode.COST_ENTERPRISE_HOSPITALITY.toString(); // 企业 - 业务招待费
		}
		// 获取收入科目 list
		List<String> subjectIds = finalLiquidationService.get_JZ_CURRENT_PROFIT_REVENUE_List(bookId);
		// 统计收入余额
		BigDecimal incomeBalance = voucherSummaryService.statisticsAmountBySummaryIds(subjectIds, bookId, CpaVoucherSummary.MONTH_BALANCE);
		if(incomeBalance == null){
			incomeBalance = new BigDecimal(0);
		}
		// 结算科目基础 信息 
		List<CpaSettleAccountSubjectBasic> subjectBasicLists = accountSubjectBasicDao.getByEnterpriseTypeAndSubKey(enterpriseType, subjectKey);
		List<String> subjectIds2 = new ArrayList<String>();
		if(subjectBasicLists != null && subjectBasicLists.size() > 0){
			for(CpaSettleAccountSubjectBasic accountSubjectBasic:subjectBasicLists){
				subjectIds2.add(accountSubjectBasic.getSubjectId());
			}
		}
		// 业务招待费
		BigDecimal businessHospitality = voucherSummaryService.statisticsAmountBySummaryIds(subjectIds2, bookId, CpaVoucherSummary.MONTH_BALANCE);
		if(businessHospitality == null){
			businessHospitality = new BigDecimal(0);
		}
		BigDecimal incomeBalance2 = incomeBalance.multiply(new BigDecimal("0.005"));
		BigDecimal businessHospitality2 = businessHospitality.multiply(new BigDecimal("0.6"));
		
		
		boolean flag = true ;
		String message = "";
		// 1、业务招待费 = 0  不要有调增’不需要比较
		// 2、IncomeBalance2 < businessHospitality2 提示：未超过收入0.005，需要调增  businessHospitality -  IncomeBalance2
		// 3、IncomeBalance2 > businessHospitality2 提示：--------------，需要调增  businessHospitality -  businessHospitality2
		// 4、IncomeBalance2 = businessHospitality2 不需要调增
		int compare = businessHospitality.compareTo(new BigDecimal(0));
		if(compare == 0){
			message = "不需要调增";
		}
		if(incomeBalance2.compareTo(businessHospitality2) < 0){
			flag = false ;
			message = "未超过收入5‰，需要调增" + businessHospitality.subtract(incomeBalance2);
		}
		if(incomeBalance2.compareTo(businessHospitality2) > 0){
			flag = false ;
			message = "超过收入5‰，需要调增" + businessHospitality.subtract(businessHospitality2);
		}
		if(incomeBalance2.compareTo(businessHospitality2) == 0){
			message = "不需要调增";
		}
		return new AssetClassBalanceVO(subjectKey, subjectName, flag, message);
	}
	
	/**
	 * 广告和业务宣传费
	 * 2017年12月29日 下午1:58:34
	 * @param bookId
	 * @param enterpriseType
	 * @param subjectName
	 * @return
	 */
	private AssetClassBalanceVO advertisingAndBusinessPromotionFees(String bookId, String enterpriseType, String subjectName){
		String subjectKey = "";
		if(enterpriseType.equals("1")){
			subjectKey = EnumSettleAccountCode.COST_SMALL_ENTERPRISE_PUBLICITY.toString(); // 小企业 - 广告和业务宣传费
		}
		if(enterpriseType.equals("2")){
			subjectKey = EnumSettleAccountCode.COST_ENTERPRISE_PUBLICITY.toString(); // 企业 - 广告和业务宣传费
		}
		// 获取收入科目 list
		List<String> subjectIds = finalLiquidationService.get_JZ_CURRENT_PROFIT_REVENUE_List(bookId);
		// 统计收入余额
		BigDecimal incomeBalance = voucherSummaryService.statisticsAmountBySummaryIds(subjectIds, bookId, CpaVoucherSummary.MONTH_BALANCE);
		if(incomeBalance == null){
			incomeBalance = new BigDecimal(0);
		}
		// 结算科目基础 信息 
		List<CpaSettleAccountSubjectBasic> subjectBasicLists = accountSubjectBasicDao.getByEnterpriseTypeAndSubKey(enterpriseType, subjectKey);
		List<String> subjectIds2 = new ArrayList<String>();
		if(subjectBasicLists != null && subjectBasicLists.size() > 0){
			for(CpaSettleAccountSubjectBasic accountSubjectBasic:subjectBasicLists){
				subjectIds2.add(accountSubjectBasic.getSubjectId());
			}
		}
		// 广告和业务宣传费
		BigDecimal propagandaFee = voucherSummaryService.statisticsAmountBySummaryIds(subjectIds2, bookId, CpaVoucherSummary.MONTH_BALANCE);
		if(propagandaFee == null){
			propagandaFee = new BigDecimal(0);
		}
		BigDecimal incomeBalance2 = incomeBalance.multiply(new BigDecimal("0.15"));
		boolean flag = true ;
		String message = "不需要调增";
		// 1、广告和业务宣传费未超过收入 15%  不需要调增
		// 2、广告和业务宣传费超过收入 15% 需要调增 propagandaFee - IncomeBalance2
		if(propagandaFee.compareTo(incomeBalance2) < 0){
			message = "未超过收入15% ，不需要调增";
		}
		if(propagandaFee.compareTo(incomeBalance2) > 0){
			flag = false;
			message = "超过收入15% ，需要调增" + propagandaFee.subtract(incomeBalance2);
		}
		return new AssetClassBalanceVO(subjectKey, subjectName, flag, message);
	}
	
	/**
	 * 工会经费
	 * 2017年12月29日 下午1:58:24
	 * @param bookId
	 * @param enterpriseType
	 * @param subjectName
	 * @param customerId
	 * @return
	 */

	private AssetClassBalanceVO unionFunds(String bookId, String enterpriseType, String subjectName, String customerId){
		String subjectKey = "";
		if(enterpriseType.equals("1")){
			subjectKey = EnumSettleAccountCode.COST_SMALL_ENTERPRISE_UNION_FUNDS.toString(); // 小企业 - 广告和业务宣传费
		}
		if(enterpriseType.equals("2")){
			subjectKey = EnumSettleAccountCode.COST_ENTERPRISE_UNION_FUNDS.toString(); // 企业 - 广告和业务宣传费
		}
		
		// 结算科目基础 信息 
		List<CpaSettleAccountSubjectBasic> subjectBasicLists = accountSubjectBasicDao.getByEnterpriseTypeAndSubKey(enterpriseType, subjectKey);
		List<String> subjectIds2 = new ArrayList<String>();
		// 工资
		Double salary =  salaryInfoService.getAccruedSalary(bookId,customerId);
		if(salary == null){
			salary = new Double(0);
		}
		BigDecimal salaryDecimal = new BigDecimal(salary);
		if(subjectBasicLists != null && subjectBasicLists.size() > 0){
			for(CpaSettleAccountSubjectBasic accountSubjectBasic:subjectBasicLists){
				subjectIds2.add(accountSubjectBasic.getSubjectId());
			}
		}
		// 工会经费
		BigDecimal unionFunds = voucherSummaryService.statisticsAmountBySummaryIds(subjectIds2, bookId, CpaVoucherSummary.MONTH_BALANCE);
		if(unionFunds == null){
			unionFunds = new BigDecimal(0);
		}
		BigDecimal salary2 = salaryDecimal.multiply(new BigDecimal("0.02"));
		
		
		boolean flag = true ;
		String message = "不需要调增";
		// 1、未超过（工资 + 奖金）的2%  不需要调增
		// 2、超过（工资 + 奖金）的2%  需要调增 propagandaFee - IncomeBalance2
		
		
		if(unionFunds.compareTo(salary2) < 0){
			message = "未超过（工资 + 奖金）的2%  不需要调增";
		}
		if(unionFunds.compareTo(salary2) > 0){
			flag = false;
			message = "超过（工资 + 奖金）的2% ，需要调增" + unionFunds.subtract(salary2);
		}
		return new AssetClassBalanceVO(subjectKey, subjectName, flag, message);
	}

	/**
	 * 职工教育经费
	 * 2017年12月29日 下午2:00:23
	 * @param bookId
	 * @param enterpriseType
	 * @param subjectName
	 * @param customerId
	 * @return
	 */
	private AssetClassBalanceVO employeeEducationExpenses(String bookId, String enterpriseType, String subjectName, String customerId){
		String subjectKey = "";
		if(enterpriseType.equals("1")){
			subjectKey = EnumSettleAccountCode.COST_SMALL_ENTERPRISE_STAFF_TRAINING.toString(); // 小企业 - 职工教育经费
		}
		if(enterpriseType.equals("2")){
			subjectKey = EnumSettleAccountCode.COST_ENTERPRISE_STAFF_TRAINING.toString(); // 企业 - 职工教育经费
		}
		
		// 结算科目基础 信息 
		List<CpaSettleAccountSubjectBasic> subjectBasicLists = accountSubjectBasicDao.getByEnterpriseTypeAndSubKey(enterpriseType, subjectKey);
		List<String> subjectIds2 = new ArrayList<String>();
		// 工资
		Double salary = salaryInfoService.getAccruedSalary(bookId,customerId);
		if(salary == null){
			salary = new Double(0);
		}
		BigDecimal salaryDecimal = new BigDecimal(salary);
		if(subjectBasicLists != null && subjectBasicLists.size() > 0){
			for(CpaSettleAccountSubjectBasic accountSubjectBasic:subjectBasicLists){
				subjectIds2.add(accountSubjectBasic.getSubjectId());
			}
		}
		// 职工教育经费
		BigDecimal education = voucherSummaryService.statisticsAmountBySummaryIds(subjectIds2, bookId, CpaVoucherSummary.MONTH_BALANCE);
		if(education == null){
			education = new BigDecimal(0);
		}
		BigDecimal salary2 = salaryDecimal.multiply(new BigDecimal("0.025"));
		
		
		boolean flag = true ;
		String message = "不需要调增";
		// 1、未超过（工资 + 奖金）的2.5%  不需要调增
		// 2、超过（工资 + 奖金）的2.5%  需要调增 propagandaFee - IncomeBalance2
		
		if(education.compareTo(salary2) < 0){
			message = "未超过（工资 + 奖金）的2.5%  不需要调增";
		}
		if(education.compareTo(salary2) > 0){
			flag = false;
			message = "超过（工资 + 奖金）的2.5% ，需要调增" + education.subtract(salary2);
		}
		return new AssetClassBalanceVO(subjectKey, subjectName, flag, message);
	}
	
}
