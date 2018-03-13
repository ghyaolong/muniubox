package com.taoding.service.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.domain.settleaccount.CpaFinalLiquidationProportion;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.domain.vouchersummary.SummaryEntity;
import com.taoding.mapper.settleaccount.CpaFinalLiquidationProportionDao;
import com.taoding.mapper.voucher.CpaVoucherSubjectDao;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

/**
 * 凭证科目
 * @author czy
 * 2017年11月28日 下午3:09:43
 */
@Service
public class CpaVoucherSubjectServiceImpl implements CpaVoucherSubjectService {

	@Autowired
	private CpaVoucherSubjectDao voucherSubjectDao ;
	@Autowired
	private CpaFinalLiquidationProportionDao proportionDao;
	@Autowired
	private CpaVoucherSummaryService voucherSummaryService;
	
    /**
     * 为一个客户初始化一张凭证科目表
     * @param bookId
     * @return
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int init(String bookId) {
		return voucherSubjectDao.init(bookId);
	}

	/**
	 * 新增凭证科目
	 * 2017年11月29日 上午11:13:34
	 * @param bookId
	 * @param voucherSubject
	 * @return
	 */
	@Override
	@Transactional
	public int insert(String bookId, CpaVoucherSubject voucherSubject) {
		return voucherSubjectDao.insert(bookId, voucherSubject);
	}

	/**
	 * 批量新增凭证科目
	 * 2017年11月29日 上午10:20:34
	 * @param bookId
	 * @param lists
	 * @return
	 */
	@Override
	@Transactional
	public int batchInsert(String bookId,List<CpaVoucherSubject> lists) {
		return voucherSubjectDao.batchInsert(bookId,lists);
	}

	/**
	 * 根据凭证ID查询凭证科目
	 * 2017年11月29日 上午10:21:41
	 * @param bookId
	 * @param voucherId
	 * @return
	 */
	@Override
	public List<CpaVoucherSubject> findByVoucherId(String bookId,String voucherId) {
		List<CpaVoucherSubject> lists = voucherSubjectDao.findByVoucherId(bookId,voucherId);
		if (lists != null && lists.size() > 0) {
			for (CpaVoucherSubject voucherSubject : lists) {
				BigDecimal surplusAmount;
				if (voucherSubject.isDirection()) { // 借方  余额 = 期初  + 借方 - 贷方
					surplusAmount = voucherSubject.getBeginningBalances()
							.add(voucherSubject.getSumDebit())
							.subtract(voucherSubject.getSumCredit());
				} else { // 贷方  余额 = 期初  + 贷方 - 借方
					surplusAmount = voucherSubject.getBeginningBalances()
							.add(voucherSubject.getSumCredit())
							.subtract(voucherSubject.getSumDebit());
				}
				voucherSubject.setSurplusAmount(surplusAmount);
			}
		}
		return lists;
	}

	/**
	 * 通过科目id查询所有记账凭证所有科目信息
	 * 2017年12月1日 下午2:42:24
	 * @param bookId
	 * @param subjectId
	 * @return
	 */
	@Override
	public List<CpaVoucherSubject> findBySubjectId(String bookId,
			String subjectId) {
		return voucherSubjectDao.findBySubjectId(bookId, subjectId);
	}

	/**
	 * 根据凭证ID 删除科目信息 
	 * 2017年12月4日 上午10:18:03
	 * @param bookId
	 * @param voucherId
	 * @return
	 */
	@Override
	@Transactional
	public int deleteByVoucherId(String bookId, String voucherId) {
		return voucherSubjectDao.deleteByVoucherId(bookId, voucherId);
	}

	/**
	 * 根据凭证ID批量删除凭证科目
	 * 2017年12月6日 上午10:08:24
	 * @param bookId
	 * @param deleteIds
	 * @return
	 */
	@Override
	@Transactional
	public int batchDeleteByVoucherId(String bookId, String[] deleteIds) {
		return voucherSubjectDao.batchDeleteByVoucherId(bookId, deleteIds);
	}

	/**
	 * 查询需要合并的科目，并将同科目合并
	 * 2017年12月7日 下午4:45:28
	 * @param bookId
	 * @param mergeIds
	 * @return
	 */
	@Override
	public List<CpaVoucherSubject> findMergeVoucherSubject(String bookId,
			String[] mergeIds) {
		return voucherSubjectDao.findMergeVoucherSubject(bookId, mergeIds);
	}

	/**
	 *  获取需要删除的科目的金额  此方法凭证汇总使用
	 * 2017年12月14日 上午11:28:16
	 * @param bookId
	 * @param voucherIds
	 * @return
	 */
	@Override
	public List<SummaryEntity> findDeleteSubjectAmount(String bookId,
			String[] voucherIds) {
		return voucherSubjectDao.findDeleteSubjectAmount(bookId, voucherIds);
	}

	/**
	 * 处理结转销售成本 生成凭证科目信息
	 * @param subjectIds 科目期初的 id
	 * @return
	 */
	@Override
	public List<CpaVoucherSubject> dealSaleCostVoucherSubjectInfo(String bookId,List<String> subjectIds) {
		
		List<CpaVoucherSubject> voucherSubjectList = new ArrayList<CpaVoucherSubject>();
		
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
		BigDecimal proportion = new BigDecimal("0.6");
		CpaFinalLiquidationProportion cpaProportion =  proportionDao.findProportionByBookId(bookId);
		if(cpaProportion != null && StringUtils.isNotEmpty(cpaProportion.getProportion().toString())){
			proportion = cpaProportion.getProportion().divide(new BigDecimal(100),2) ;
		}
		for(String subjectId : subjectIds){ 
			CpaVoucherSubject cvs = voucherSubjectDao.getCpaVoucherSubjectBySubjectId(bookId, currentPeriod , subjectId);
			// 如果凭证科目里面存在 最高节点，走这个
			if(cvs != null && StringUtils.isNotEmpty(cvs.getId())){
				voucherSubjectList.add(cvs);
			}else{
				CpaCustomerSubject obj = voucherSubjectDao.getSubjectNoById(subjectId);
				// 如果凭证科目里面不存在最高节点，走这个
				String subjectNo = "^" + obj.getSubjectNo();
				List<CpaVoucherSubject> objList = voucherSubjectDao.findByParentSubjectNo(bookId, currentPeriod,subjectNo);
				voucherSubjectList.addAll(objList);
			}
		}
		if(voucherSubjectList.size() > 0){
			for(CpaVoucherSubject voucherSubject: voucherSubjectList){
				if(voucherSubject.isDirection()){
					// 借方金额 - 贷方金额
					voucherSubject.setSurplusAmount(voucherSubject.getAmountDebit().subtract(voucherSubject.getAmountCredit()).multiply(proportion));
				}else{
					// 贷方金额 - 借方金额
					voucherSubject.setSurplusAmount(voucherSubject.getAmountCredit().subtract(voucherSubject.getAmountDebit()).multiply(proportion));
				}
			}
		}
		return voucherSubjectList;
	}
	
	/**
	 * 处理结转本期损益（收入） 生成凭证科目信息
	 * @param subjectIds 科目期初的 id
	 * @return
	 */
	@Override
	public List<CpaVoucherSubject> dealProfitRevenueVoucherSubjectInfo(String bookId,List<String> subjectIds) {
		
		List<CpaVoucherSubject> voucherSubjectList = new ArrayList<CpaVoucherSubject>();
		
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
		for(String subjectId : subjectIds){ 
			CpaVoucherSubject cvs = voucherSubjectDao.getCpaVoucherSubjectBySubjectId(bookId, currentPeriod , subjectId);
			// 如果凭证科目里面存在 最高节点，走这个
			if(cvs != null && StringUtils.isNotEmpty(cvs.getId())){
				voucherSubjectList.add(cvs);
			}else{
				CpaCustomerSubject obj = voucherSubjectDao.getSubjectNoById(subjectId);
				// 如果凭证科目里面不存在最高节点，走这个
				String subjectNo = "^" + obj.getSubjectNo();
				List<CpaVoucherSubject> objList = voucherSubjectDao.findByParentSubjectNo(bookId, currentPeriod,subjectNo);
				voucherSubjectList.addAll(objList);
			}
		}
		if(voucherSubjectList.size() > 0){
			for(CpaVoucherSubject voucherSubject: voucherSubjectList){
				if(voucherSubject.isDirection()){
					// 借方金额 - 贷方金额
					voucherSubject.setSurplusAmount(voucherSubject.getAmountDebit().subtract(voucherSubject.getAmountCredit()));
				}else{
					// 贷方金额 - 借方金额
					voucherSubject.setSurplusAmount(voucherSubject.getAmountCredit().subtract(voucherSubject.getAmountDebit()));
				}
			}
		}
		return voucherSubjectList;
	}

	
	/**
	 * 处理结转本期损益（成本费用） 生成凭证科目信息
	 * 2018年1月2日 下午2:06:44
	 * @param bookId
	 * @param subjectIds 收入类四项ID
	 * @return
	 */
	public List<CpaVoucherSubject> dealProfitCostVoucherSubjectInfo(String bookId,List<String> subjectIds){
		
		List<CpaVoucherSubject> voucherSubjectList = new ArrayList<CpaVoucherSubject>();
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
		List<CpaVoucherSummary> summaryLists = voucherSummaryService.findCurrentPeriodProfitCostSummaryIds(subjectIds, bookId);
		if(summaryLists != null && summaryLists.size() > 0){
			for (CpaVoucherSummary voucherSummary : summaryLists) {
				if(voucherSummary != null && StringUtils.isNotEmpty(voucherSummary.getId())){
					CpaVoucherSubject cvs = voucherSubjectDao.getCpaVoucherSubjectBySubjectId(bookId, currentPeriod , voucherSummary.getId());
					// 如果凭证科目里面存在 最高节点，走这个
					if(cvs != null && StringUtils.isNotEmpty(cvs.getId())){
						voucherSubjectList.add(cvs);
					}else{
						CpaCustomerSubject obj = voucherSubjectDao.getSubjectNoById(voucherSummary.getId());
						// 如果凭证科目里面不存在最高节点，走这个
						String subjectNo = "^" + obj.getSubjectNo();
						List<CpaVoucherSubject> objList = voucherSubjectDao.findByParentSubjectNo(bookId, currentPeriod,subjectNo);
						voucherSubjectList.addAll(objList);
					}
				}
			}
		}
		if(voucherSubjectList.size() > 0){
			for(CpaVoucherSubject voucherSubject: voucherSubjectList){
				if(voucherSubject.isDirection()){
					// 借方金额 - 贷方金额
					voucherSubject.setSurplusAmount(voucherSubject.getAmountDebit().subtract(voucherSubject.getAmountCredit()));
				}else{
					// 贷方金额 - 借方金额
					voucherSubject.setSurplusAmount(voucherSubject.getAmountCredit().subtract(voucherSubject.getAmountDebit()));
				}
			}
		}
		return voucherSubjectList;		
	}

}
