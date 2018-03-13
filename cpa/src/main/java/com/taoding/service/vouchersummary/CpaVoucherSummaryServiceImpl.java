package com.taoding.service.vouchersummary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.user.User;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.domain.vouchersummary.SummaryEntity;
import com.taoding.mapper.vouchersummary.CpaVoucherSummaryDao;
import com.taoding.service.subject.CpaCustomerSubjectService;

/**
 * 凭证汇总
 * @author czy
 * 2017年12月13日 下午1:56:41
 */
@Service
@Transactional
public class CpaVoucherSummaryServiceImpl implements CpaVoucherSummaryService{

	@Autowired
	private CpaVoucherSummaryDao voucherSummaryDao;
	@Autowired
	private CpaCustomerSubjectService customerSubjectService;
	
	/**
	 * 所需计算的 科目信息
	 * 2017年12月13日 下午4:13:29
	 * @param lists 
	 * @param bookId
	 * @param calculationType  计算方法  
	 *       calculationType == true  加   calculationType == false 减
	 * @return
	 */
	@Transactional
	public int dealVoucherSummary(List<SummaryEntity> lists ,String bookId,String voucherPeriod,boolean calculationType){
		if(lists != null && lists.size() > 0){
			for (SummaryEntity summaryEntity : lists) {
				List<CpaVoucherSummary> summaryLists = new ArrayList<CpaVoucherSummary>();
				
				String subjectId = summaryEntity.getSubjectId() ;
				BigDecimal amountDebit = summaryEntity.getAmountDebit();
				BigDecimal amountCredit = summaryEntity.getAmountCredit();
				
				CpaVoucherSummary voucherSummary = this.findById(bookId, subjectId, voucherPeriod);
				
				if(voucherSummary != null && StringUtils.isNotEmpty(voucherSummary.getId())){
					dealParentAmount(voucherSummary, amountDebit, amountCredit, calculationType);
				}else{
					if(calculationType){
						CpaVoucherSummary subjectSummary = customerSubjectService.getVoucherSummarySubjectById(bookId, subjectId);
						buildParentSubject(summaryLists,subjectSummary,voucherPeriod,bookId, amountDebit, amountCredit,calculationType);
						if(summaryLists != null && summaryLists.size() > 0){
							this.batchInsert(summaryLists);
						}
					}
				}
			}
		}
		return 0 ;
	}
	 
	/**
	 * 处理父节点金额
	 * 2017年12月14日 上午10:47:25
	 * @param voucherSummary
	 * @param amountDebit
	 * @param amountCredit
	 * @param calculationType
	 */
	@Transactional
	private void dealParentAmount(CpaVoucherSummary voucherSummary, BigDecimal amountDebit,
			BigDecimal amountCredit,boolean calculationType){
		if(calculationType){ //金额加
			voucherSummary.setCurrentPeriodDebit(voucherSummary.getCurrentPeriodDebit().add(amountDebit));
			voucherSummary.setCurrentPeriodCredit(voucherSummary.getCurrentPeriodCredit().add(amountCredit));
		}else{ //金额减
			voucherSummary.setCurrentPeriodDebit(voucherSummary.getCurrentPeriodDebit().subtract(amountDebit));
			voucherSummary.setCurrentPeriodCredit(voucherSummary.getCurrentPeriodCredit().subtract(amountCredit));
		}
		this.updateVoucherSummaryAmount(voucherSummary);
		if(!"0".equals(voucherSummary.getParent())){
			String voucherPeriod = DateUtils.formatDate(voucherSummary.getVoucherPeriod(), "yyyy-MM-dd");
			CpaVoucherSummary voucherSummary2 = this.findBySubjectNo(voucherSummary.getBookId(),
					voucherPeriod , voucherSummary.getParent());
			dealParentAmount(voucherSummary2, amountDebit, amountCredit, calculationType);
		}
	}

	/**
	 * 生成父节点科目
	 * 2017年12月14日 上午10:01:01
	 * @param lists
	 * @param voucherSummary
	 * @param voucherPeriod
	 * @param bookId
	 * @param amountDebit
	 * @param amountCredit
	 * @param calculationType
	 */
	@Transactional
	 void buildParentSubject(List<CpaVoucherSummary> lists,CpaVoucherSummary voucherSummary,String voucherPeriod,
			String bookId, BigDecimal amountDebit,BigDecimal amountCredit,boolean calculationType){
		
		if(voucherSummary != null && StringUtils.isNotEmpty(voucherSummary.getId())){
			voucherSummary.setCurrentPeriodDebit(amountDebit);
			voucherSummary.setCurrentPeriodCredit(amountCredit);
			voucherSummary.setVoucherPeriod(DateUtils.StringToDate(voucherPeriod, "yyyy-MM-dd"));
			User createBy = new User();
			createBy.setId(UserUtils.getCurrentUserId());
			voucherSummary.setCreateBy(createBy);
			voucherSummary.setCreateDate(new Date());
			lists.add(voucherSummary);
			if(!"0".equals(voucherSummary.getParent())){
				CpaVoucherSummary voucherSummary2 = this.findBySubjectNo(voucherSummary.getBookId(),voucherPeriod , voucherSummary.getParent());
				if(voucherSummary2 != null && StringUtils.isNotEmpty(voucherSummary2.getId())){
					dealParentAmount(voucherSummary2, amountDebit, amountCredit, calculationType);
				}else{
					CpaVoucherSummary voucherSummary3 = customerSubjectService.getVoucherSummaryBySubjectNo(bookId, voucherSummary.getParent());
					buildParentSubject(lists,voucherSummary3 ,voucherPeriod ,bookId, amountDebit, amountCredit,calculationType);
				}
			}
		}
	}
	
	/**
	 * 批量新增
	 * 2017年12月13日 下午3:45:20
	 * @param summaryLists
	 * @return
	 */
	@Override
	@Transactional
	public int batchInsert(List<CpaVoucherSummary> summaryLists){
		return voucherSummaryDao.batchInsert(summaryLists);
	}
	
	/**
	 * 修改账簿 凭证汇总科目金额
	 * 2017年12月13日 下午3:46:16
	 * @param voucherSummary
	 * @return
	 */
	@Override
	@Transactional
	public int updateVoucherSummaryAmount(CpaVoucherSummary voucherSummary){
		return voucherSummaryDao.updateVoucherSummaryAmount(voucherSummary);
	}

	/**
	 * 查询账簿 账期 凭证汇总信息
	 * 2017年12月13日 下午3:47:24
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	public List<CpaVoucherSummary> findAllList(String bookId,String voucherPeriod){
		return voucherSummaryDao.findAllList(bookId, voucherPeriod);
	}
	
	/**
	 * 通过id 查询当前账期 凭证汇总科目信息
	 * 2017年12月13日 下午3:48:29
	 * @param bookId
	 * @param id
	 * @param voucherPeriod
	 * @return
	 */
	public CpaVoucherSummary findById(String bookId,String id,String voucherPeriod){
		return voucherSummaryDao.findById(bookId, id, voucherPeriod);
	}
	
	/**
	 * 根据父科目编号 查询科目
	 * 2017年12月13日 下午3:49:33
	 * @param bookId
	 * @param voucherPeriod
	 * @param subjectNo
	 * @return
	 */
	public CpaVoucherSummary findBySubjectNo(String bookId,String voucherPeriod,String subjectNo){
		return voucherSummaryDao.findBySubjectNo(bookId, voucherPeriod, subjectNo);
	}

	/**
	 * 凭证汇总表最后一行金额合计
	 */
	@Override
	public CpaVoucherSummary findSumDebitAndCredit(String bookId,
			String voucherPeriod, String parent) {
		return voucherSummaryDao.findSumDebitAndCredit(bookId, voucherPeriod, parent);
	}

	/**
	 * 统计总额  	
	 * @param summaryIds  CpaVoucherSummary id
	 * @param bookId 账薄 ID
	 * @param accountPeriod 1 代表月，2 代表 季
	 * @return
	 */
	@Override
	public BigDecimal statisticsAmountBySummaryIds(List<String> summaryIds, String bookId,
			Integer accountPeriod) {
		// 获取当前账期
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		String startPeriod = "";
		// 月账期
		if(accountPeriod == 1){
			startPeriod = currentPeriod ;
		}
		// 季账期
		if(accountPeriod == 2){
			startPeriod = DateUtils.getPre2Time(currentPeriod, -2);
		}
		//半年
		if(accountPeriod == 3){
			startPeriod = DateUtils.getPre2Time(currentPeriod, -5);
		}
		//年
		if(accountPeriod == 4){
			startPeriod = DateUtils.getPre2Time(currentPeriod, -11);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("summaryIds", summaryIds);
		map.put("bookId", bookId);
		map.put("currentPeriod", currentPeriod);
		map.put("startPeriod", startPeriod);
		CpaVoucherSummary cvs =  voucherSummaryDao.statisticsAmountBySummaryIds(map);
		return cvs.getBalance();
	}
	
	/**
	 * 统计损益类除开收入类四项 金额
	 * 2018年1月2日 上午11:14:46
	 * @param summaryIds 收入类四项Ids
	 * @param bookId 账薄 ID
	 * @param accountPeriod 1 代表月，2 代表 季
	 * @return
	 */
	@Override
	public BigDecimal statisticsAmountNotInSummaryIds(List<String> summaryIds,String bookId) {
		// 获取当前账期
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("summaryIds", summaryIds);
		map.put("bookId", bookId);
		map.put("currentPeriod", currentPeriod);
		CpaVoucherSummary cvs =  voucherSummaryDao.statisticsAmountNotInSummaryIds(map);
		return cvs.getBalance();
	}

	/**
	 * 统计单个科目余金额
	 * @param summaryId  CpaVoucherSummary id
	 * @param bookId 账薄 ID
	 * @param accountPeriod 1 代表月，2 代表 季
	 * @return
	 */
	@Override
	public BigDecimal statisticsAmountBySummaryId(String summaryId, String bookId,
			Integer accountPeriod) {
		// 获取当前账期
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		String startPeriod = "";
		// 月账期
		if(accountPeriod == 1){
			startPeriod = currentPeriod ;
		}
		// 季账期
		if(accountPeriod == 2){
			startPeriod = DateUtils.getPre2Time(currentPeriod, -2);
		}
		//半年
		if(accountPeriod == 3){
			startPeriod = DateUtils.getPre2Time(currentPeriod, -5);
		}
		//年
		if(accountPeriod == 4){
			startPeriod = DateUtils.getPre2Time(currentPeriod, -11);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("summaryId", summaryId);
		map.put("bookId", bookId);
		map.put("currentPeriod", currentPeriod);
		map.put("startPeriod", startPeriod);
		CpaVoucherSummary cvs =  voucherSummaryDao.statisticsAmountBySummaryId(map);
		return cvs.getBalance();
	}

	/**
	 * 查询科目信息及余额
	 * 2017年12月27日 上午9:55:09
	 * @param id
	 * @param bookId
	 * @param type  1 查询当前账期  2、查询当前（包含当前账期及之前账期）
	 * @return
	 */
	@Override
	public CpaVoucherSummary findInfoAndAmountById(String id, String bookId,Integer type) {
		String period = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		if(type == 2){
			period = DateUtils.getPerFirstDayOfMonth(DateUtils.StringToDate(period, "yyyy-MM-dd"));
		}
		CpaVoucherSummary voucherSummary = voucherSummaryDao.findInfoAndAmountById(id, bookId ,type ,period);
		if(voucherSummary != null){
			BigDecimal surplusAmount;
			if (voucherSummary.isDirection()) { // 借方  余额 = 期初  + 借方 - 贷方
				surplusAmount = voucherSummary.getBeginningBalances()
						.add(voucherSummary.getCurrentPeriodDebit()).subtract(voucherSummary.getCurrentPeriodCredit());
			} else { // 贷方  余额 = 期初  + 贷方 - 借方
				surplusAmount = voucherSummary.getBeginningBalances()
						.add(voucherSummary.getCurrentPeriodCredit()).subtract(voucherSummary.getCurrentPeriodDebit());
			}
			voucherSummary.setBalance(surplusAmount);
		}
		return voucherSummary ;
	}

	/**
	 * 统计本期损益类除开收入类科目的subjectId
	 * 2018年1月2日 下午2:25:43
	 * @param summaryIds  收入类四个科目subjectId
	 * @param bookId
	 * @param accountPeriod
	 * @return
	 */
	@Override
	public List<CpaVoucherSummary> findCurrentPeriodProfitCostSummaryIds(
			List<String> summaryIds, String bookId) {
		// 获取当前账期
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("summaryIds", summaryIds);
		map.put("bookId", bookId);
		map.put("currentPeriod", currentPeriod);

		return	voucherSummaryDao.findCurrentPeriodProfitCostSummaryIds(map);
	}
	
	
	
}
