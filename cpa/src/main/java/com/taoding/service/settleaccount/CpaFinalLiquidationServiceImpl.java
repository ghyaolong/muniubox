package com.taoding.service.settleaccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.settleaccount.CpaFinalLiquidation;
import com.taoding.domain.settleaccount.CpaFinalLiquidationBasic;
import com.taoding.domain.settleaccount.CpaFinalLiquidationVoucher;
import com.taoding.domain.settleaccount.EnumFinalLiquidationCode;
import com.taoding.domain.settleaccount.EnumSettleAccountCode;
import com.taoding.domain.user.User;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.mapper.settleaccount.CpaFinalLiquidationDao;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.customerTaxItem.CustomerTaxAlgorithmRuleService;
import com.taoding.service.voucher.CpaVoucherService;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;


/**
 * 期末转结
 * @author czy
 * 2017年12月21日 下午4:00:25
 */
@Service
public class CpaFinalLiquidationServiceImpl implements CpaFinalLiquidationService {

	@Autowired
	private CpaFinalLiquidationDao finalLiquidationDao;
	@Autowired
	private CpaDealFinalLIquidationService dealFinalLIquidationService;
	@Autowired
	private CpaBulidFinalLIquidationService bulidFinalLIquidationService;
	@Autowired
	private AccountingBookService accountingBookService;
	@Autowired
	private CpaFinalLiquidationBasicService finalLiquidationBasicService; //期末结账基础科目
	@Autowired 
    private CpaVoucherSummaryService voucherSummaryService; //凭证汇总
	@Autowired 
	private CpaVoucherService  voucherService;
	@Autowired 
	private CpaFinalLiquidationOperatingDataService finalLiquidationOperatingDataService; //经营数据分析基础数据
	@Autowired 
	private CustomerTaxAlgorithmRuleService customerTaxAlgorithmRuleService;
	
	/**
	 * 新增期末结转数据
	 * 2017年12月22日 下午4:30:23
	 * @param finalLiquidation
	 * @return
	 */
	@Override
	@Transactional
	public int insert(CpaFinalLiquidation finalLiquidation) {
		return finalLiquidationDao.insert(finalLiquidation);
	}
	
	/**
	 * 根据账期+账簿ID 判断是否期末结转
	 * 2017年12月22日 下午4:31:33
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	@Override
	public CpaFinalLiquidation findByBookId(String bookId, String currentPeriod) {
		return finalLiquidationDao.findByBookId(bookId, currentPeriod);
	}
	
	/**
	 * 查看要生成的凭证
	 * 2017年12月23日 上午10:09:34
	 * @param bookId
	 * @param subKey
	 * @return
	 */
	@Override
	public CpaVoucher showBuildVoucher(String bookId, String subKey) {

		return bulidFinalLIquidationService.showBuildVoucher(bookId, subKey) ;
	}

	/**
	 * 获取企业 期末结转数据
	 * 2017年12月21日 下午3:59:52
	 * @param bookId
	 * @return
	 */
	@Override
	public Object loadFinalLiquidationData(String bookId) {
		return dealFinalLIquidationService.loadFinalLiquidationData(bookId);
	}

	/**
	 * 生成凭证
	 */
	@Override
	public Object generateVoucher(CpaFinalLiquidation finalLiquidation) {
		
		String bookId = finalLiquidation.getBookId();
		String customerId = finalLiquidation.getCustomerId();
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		//当前账期
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
		AccountingBook accountBook = accountingBookService.get(bookId);
		if(accountBook == null || StringUtils.isEmpty(accountBook.getId())){
			throw new LogicException("获取不到当前账簿信息");
		}
		//企业性质
		String accountingSystemId = accountBook.getAccountingSystemId() ;
		//纳税人性质
		String taxpayerPropertyId = accountBook.getTaxpayerPropertyId() ;
		
		finalLiquidation.setId(UUIDUtils.getUUid());
		finalLiquidation.setCreateDate(new Date());
		finalLiquidation.setCurrentPeriod(DateUtils.StringToDate(currentPeriod,"yyyy-MM-dd"));
		finalLiquidation.setCreateBy(createBy);
		finalLiquidation.setSettleAccounts(true);
		
		//构建插入凭证 add lixc
		int flag = insertVoucherByFinalLiquidation(finalLiquidation);
		if(flag == 0){
			throw new LogicException("生成凭证失败！");
		}
		
		finalLiquidationDao.insert(finalLiquidation);
			
		//处理经营分析数据
		deal_operatingData(bookId,customerId, accountingSystemId,taxpayerPropertyId, currentPeriod);
		
		return null;
	}
	
	/**
	 * 处理经营分析数据
	 * 2017年12月28日 上午10:58:02
	 * @param bookId
	 * @param customerId
	 * @param accountingSystemId
	 * @param currentPeriod
	 */
	public void deal_operatingData(String bookId,String customerId,String accountingSystemId,String taxpayerPropertyId,String currentPeriod){
		//获取当期收入
		List<String> lists = this.get_JZ_CURRENT_PROFIT_REVENUE_List(bookId);
		BigDecimal monthRevenue = voucherSummaryService.statisticsAmountBySummaryIds(lists, bookId, CpaVoucherSummary.MONTH_BALANCE);
		if(monthRevenue == null ){
			monthRevenue = new BigDecimal(0);
		}
		//半年收入
		BigDecimal halfRevenue = voucherSummaryService.statisticsAmountBySummaryIds(lists, bookId, CpaVoucherSummary.HALF_YEAR_BALANCE);
		if(halfRevenue == null){
			halfRevenue = new BigDecimal(0);
		}
		
		//查询 应交税费-未交增值税 subjectId
		List<CpaFinalLiquidationBasic> unpaidvatKeyLists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,
				EnumSettleAccountCode.OPERATING_DATA_UNPAID_VAT.toString());
		//应交税费-未交增值税
		BigDecimal halfUnpaidvat = voucherSummaryService.statisticsAmountBySummaryId(
				unpaidvatKeyLists.get(0).getSubjectId(), bookId, CpaVoucherSummary.HALF_YEAR_BALANCE);
		if(halfUnpaidvat == null){
			halfUnpaidvat = new BigDecimal(0);
		}
		
		//查询应交税费-应交企业所得税 subjectId
		List<CpaFinalLiquidationBasic> incometaxKeyLists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
				String.valueOf(accountingSystemId),EnumSettleAccountCode.OPERATING_DATA_CORPORATE_INCOME_TAX.toString());
		//应交税费-应交企业所得税
		BigDecimal halfIncometax = voucherSummaryService.statisticsAmountBySummaryId(
				incometaxKeyLists.get(0).getSubjectId(), bookId, CpaVoucherSummary.HALF_YEAR_BALANCE);
		if(halfIncometax == null){
			halfIncometax = new BigDecimal(0);
		}
		
		//查询本年利润 subjectId
		List<CpaFinalLiquidationBasic> profitsyearLists = finalLiquidationBasicService.findAllListByTaxpayerProperty(
				String.valueOf(accountingSystemId),EnumSettleAccountCode.OPERATING_DATA_PROFITS_YEAR.toString());
		//本年利润
		BigDecimal profitsyear = voucherSummaryService.statisticsAmountBySummaryId(
				profitsyearLists.get(0).getSubjectId(), bookId, CpaVoucherSummary.MONTH_BALANCE);
		if(profitsyear == null){
			profitsyear = new BigDecimal(0);
		}
		
		//2、资产负债率：资产负债表中负债合计除以资产总计(当前账期)
		
		//3、增值税税负率：当前账期往前取一年时间段内的   增值税   除以  不含税营业收入
		BigDecimal vatProportion = halfUnpaidvat.divide(halfRevenue,2);
		
		//4、所得税税负率：当前账期往前取一年时间段内      所得税 除以不含税营业收入
		BigDecimal incometaxProportion = halfIncometax.divide(halfRevenue, 2);
		
		//5、毛利率：利润表中的利润总额 除以 不含税营业收入
		BigDecimal profitsyearProportion = profitsyear.divide(monthRevenue, 2);
		
		Date currentDate = DateUtils.StringToDate(currentPeriod,"yyyy-MM-dd");
		
		
		BigDecimal tax = new BigDecimal(0);
		//小规模纳税人
		if(taxpayerPropertyId != null && "1".equals(taxpayerPropertyId)){
			tax = customerTaxAlgorithmRuleService.suMSmallScale(bookId, lists);
		}else{ //一般纳税人
			//销项税额ID outputSubjectId  进项税额ID
			String outputSubjectId = "" , inputSubjectId = "" ;
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
			tax = customerTaxAlgorithmRuleService.suMGeneral(bookId, inputSubjectId, outputSubjectId, lists);
		}
		
		//生成凭证 删除之前保存的经营数据
		finalLiquidationOperatingDataService.deleteOperatingData(bookId, currentDate);
		
		finalLiquidationOperatingDataService.insertOperatingData(bookId, customerId,OperatingDataType.TAX_VALUE, tax, currentDate);
		//当前收入
		finalLiquidationOperatingDataService.insertOperatingData(bookId, customerId,OperatingDataType.CUMULATIVE_INCOME, monthRevenue, currentDate);
		//资产负债率
		finalLiquidationOperatingDataService.insertOperatingData(bookId, customerId,OperatingDataType.ASSET_LIABILITY_RATIO, new BigDecimal(0), currentDate);
		//增值税税负范围
		finalLiquidationOperatingDataService.insertOperatingData(bookId, customerId,OperatingDataType.VAT_TAX_RATE, vatProportion, currentDate);
		//所得税税负范围
		finalLiquidationOperatingDataService.insertOperatingData(bookId, customerId,OperatingDataType.INCOME_TAX_RATE, incometaxProportion, currentDate);
		//毛利率
		finalLiquidationOperatingDataService.insertOperatingData(bookId, customerId,OperatingDataType.GROSS_INTEREST_RATE, profitsyearProportion, currentDate);
		
	}
	
	/**
	 * 
	* @Description: 插入凭证 并且返写 CpaFinalLiquidationVoucher
	* @param FinalLiquidation 
	* @return void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月28日
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	private int  insertVoucherByFinalLiquidation(CpaFinalLiquidation finalLiquidation){
		if(null == finalLiquidation || CollectionUtils.isEmpty(finalLiquidation.getVoucherLists())){
			throw new LogicException("期末结转参数不能为空！");
		}
		
		int resultCount = 0;
		CpaFinalLiquidationVoucher  finalLiquidationVoucher= null;
		CpaVoucher voucher = null;
		Map<String,Object> maps = null;
		for (Iterator<CpaFinalLiquidationVoucher> iterator = finalLiquidation.getVoucherLists().iterator(); iterator.hasNext();) {
			finalLiquidationVoucher = iterator.next();
			voucher = bulidFinalLIquidationService.showBuildVoucher(finalLiquidation.getBookId(), finalLiquidationVoucher.getSubKey());
			if(null == voucher) continue; 
			//插入凭证
			 maps = (Map<String,Object>)voucherService.insertCpaVoucher(voucher);
			if(MapUtils.isNotEmpty(maps) && Global.TRUE.equals(String.valueOf(maps.get("state"))) ){
				 finalLiquidationVoucher.setBookId(finalLiquidation.getBookId());
				 finalLiquidationVoucher.setCustomerId(finalLiquidation.getCustomerId());
				 finalLiquidationVoucher.setCurrentPeriod(finalLiquidation.getCurrentPeriod());
				 finalLiquidationVoucher.setVoucherId(String.valueOf(maps.get("id")));
				 resultCount ++;
			}
		}
		return resultCount;
	}
	
	/**
	 * 查询收入类科目ID
	 */
	@Override
	public List<String> get_JZ_CURRENT_PROFIT_REVENUE_List(String bookId){
		
		AccountingBook accountingBook = accountingBookService.get(bookId);
		if(null == accountingBook){
			throw new LogicException("获取不到当前账簿信息");
		}
		String accountingSystemId = accountingBook.getAccountingSystemId();
		
		 //公允价值变动损益
		List<CpaFinalLiquidationBasic> lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,EnumFinalLiquidationCode.FAIR_VALUE_PROFIT.toString());
		List<String> subjectIds = new ArrayList<String>();
		
		if(CollectionUtils.isNotEmpty(lists)){
			CpaFinalLiquidationBasic liquidationBasic = lists.get(0);
			
			List<String> tempList = new ArrayList<String>();
			tempList.add(liquidationBasic.getSubjectId());
			BigDecimal temp = voucherSummaryService.statisticsAmountBySummaryIds(tempList , bookId, CpaVoucherSummary.MONTH_BALANCE);
			
			if(null != temp && temp.compareTo(BigDecimal.ZERO) >= 0){
				subjectIds.add(liquidationBasic.getSubjectId());
			}
		}
		
		//基础四项
		lists = finalLiquidationBasicService.findAllListByTaxpayerProperty(accountingSystemId,EnumFinalLiquidationCode.JZ_CURRENT_PROFIT_REVENUE.toString());
		
		if(CollectionUtils.isEmpty(lists)) return  null;
		for (CpaFinalLiquidationBasic liquidationBasic : lists) {
			if(liquidationBasic.isDirection()){
				subjectIds.add(liquidationBasic.getSubjectId());
			}
		}
		return subjectIds;
	}
	
}
