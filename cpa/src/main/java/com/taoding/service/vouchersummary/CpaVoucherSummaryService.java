package com.taoding.service.vouchersummary;

import java.math.BigDecimal;
import java.util.List;

import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.domain.vouchersummary.SummaryEntity;

/**
 * 凭证汇总
 * @author czy
 * 2017年12月13日 下午1:52:47
 */
public interface CpaVoucherSummaryService {

	/**
	 * 批量新增
	 * 2017年12月13日 下午3:45:20
	 * @param summaryLists
	 * @return
	 */
	public int batchInsert(List<CpaVoucherSummary> summaryLists);
	
	/**
	 * 修改账簿 凭证汇总科目金额
	 * 2017年12月13日 下午3:46:16
	 * @param voucherSummary
	 * @return
	 */
	public int updateVoucherSummaryAmount(CpaVoucherSummary voucherSummary);

	/**
	 * 查询账簿 账期 凭证汇总信息
	 * 2017年12月13日 下午3:47:24
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	public List<CpaVoucherSummary> findAllList(String bookId,String voucherPeriod);
	
	/**
	 * 通过id 查询当前账期 凭证汇总科目信息
	 * 2017年12月13日 下午3:48:29
	 * @param bookId
	 * @param id
	 * @param voucherPeriod
	 * @return
	 */
	public CpaVoucherSummary findById(String bookId,String id,String voucherPeriod);
	
	/**
	 * 根据父科目编号 查询科目
	 * 2017年12月13日 下午3:49:33
	 * @param bookId
	 * @param voucherPeriod
	 * @param subjectNo
	 * @return
	 */
	public CpaVoucherSummary findBySubjectNo(String bookId,String voucherPeriod,String subjectNo);
	
	/**
	 * 所需计算的 科目信息
	 * 2017年12月13日 下午4:13:29
	 * @param lists 
	 * @param bookId 
	 * @param voucherPeriod 
	 * @param calculationType  计算方法  
	 *       calculationType == true  加   calculationType == false 减
	 * @return
	 */
	public int dealVoucherSummary(List<SummaryEntity> lists,String bookId,
			String voucherPeriod,boolean calculationType);
	
	
	/**
	 * 
	* @Description: TODO(统计 借方 贷方 金额) 
	* @param bookId 账薄Id
	* @param voucherPeriod 账期
	* @param parent 父科目编号
	* @return CpaVoucherSummary 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月19日
	 */
	public CpaVoucherSummary findSumDebitAndCredit(String bookId,String voucherPeriod,String parent);
	
	
	/**
	 * 统计 summaryIds  金额
	 * @param summaryIds  CpaVoucherSummary id
	 * @param bookId 账薄 ID
	 * @param accountPeriod 1 代表月，2 代表 季
	 * @return
	 */
	public BigDecimal statisticsAmountBySummaryIds(List<String> summaryIds, String bookId,Integer accountPeriod);
	
	/**
	 * 统计损益类除开收入类四项 金额
	 * 2018年1月2日 上午11:14:46
	 * @param summaryIds 收入类四项Ids
	 * @param bookId 账薄 ID
	 * @param accountPeriod 1 代表月，2 代表 季
	 * @return
	 */
	public BigDecimal statisticsAmountNotInSummaryIds(List<String> summaryIds, String bookId);
	
	/**
	 * 统计发生额（总额）
	 * @param summaryIds  CpaVoucherSummary id
	 * @param bookId 账薄 ID
	 * @param accountPeriod 1 代表月，2 代表 季
	 * @return
	 */
	public BigDecimal statisticsAmountBySummaryId(String summaryId, String bookId,Integer accountPeriod);
	

	/**
	 * 查询科目信息及发生额
	 * 2017年12月27日 上午9:55:09
	 * @param id
	 * @param bookId
	 * @param type  1 查询本期发生额  2、查询账期（包含当前账期及之前账期）
	 * @param nextPeriod
	 * @return
	 */
	public CpaVoucherSummary findInfoAndAmountById(String id,String bookId,Integer type);
	
	
	/**
	 * 统计本期损益类除开收入类科目的subjectId
	 * 2018年1月2日 下午2:25:43
	 * @param summaryIds  收入类四个科目subjectId
	 * @param bookId
	 * @param accountPeriod
	 * @return
	 */
	public List<CpaVoucherSummary> findCurrentPeriodProfitCostSummaryIds(List<String> summaryIds, String bookId);
	
}
