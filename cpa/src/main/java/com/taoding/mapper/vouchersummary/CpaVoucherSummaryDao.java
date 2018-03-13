package com.taoding.mapper.vouchersummary;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.vouchersummary.CpaVoucherSummary;

/**
 * 凭证汇总
 * 
 * @author czy 2017年12月13日 下午1:52:05
 */
@Repository
@Mapper
public interface CpaVoucherSummaryDao  {

	/**
	 * 批量新增 2017年12月13日 下午3:15:20
	 * 
	 * @param lists
	 * @return
	 */
	public int batchInsert(List<CpaVoucherSummary> lists);

	/**
	 * 修改账簿 凭证汇总科目金额 2017年12月13日 下午3:23:16
	 * 
	 * @param voucherSummary
	 * @return
	 */
	public int updateVoucherSummaryAmount(CpaVoucherSummary voucherSummary);

	/**
	 * 查询账簿 账期 凭证汇总信息 2017年12月13日 下午3:18:24
	 * 
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	public List<CpaVoucherSummary> findAllList(@Param("bookId") String bookId,
			@Param("voucherPeriod") String voucherPeriod);

	/**
	 * 通过id 查询当前账期 凭证汇总科目信息 2017年12月13日 下午3:18:29
	 * 
	 * @param bookId
	 * @param id
	 * @param voucherPeriod
	 * @return
	 */
	public CpaVoucherSummary findById(@Param("bookId") String bookId,
			@Param("id") String id, @Param("voucherPeriod") String voucherPeriod);

	/**
	 * 根据父科目编号 查询科目 2017年12月13日 下午3:42:33
	 * 
	 * @param bookId
	 * @param voucherPeriod
	 * @param subjectNo
	 * @return
	 */
	public CpaVoucherSummary findBySubjectNo(@Param("bookId") String bookId,
			@Param("voucherPeriod") String voucherPeriod,
			@Param("subjectNo") String subjectNo);

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
	public CpaVoucherSummary findSumDebitAndCredit(@Param("bookId") String bookId,
			@Param("voucherPeriod") String voucherPeriod,
			@Param("parent") String parent);
	
	/**
	 * 统计借方贷方金额	
	 * @param map
	 * @return
	 */
	public CpaVoucherSummary statisticsAmountBySummaryIds(Map<String, Object> map);
	
	/**
	 * 统计损益类除开收入类四项 金额
	 * 2018年1月2日 上午11:14:46
	 * @param map
	 * @return
	 */
	public CpaVoucherSummary statisticsAmountNotInSummaryIds(Map<String, Object> map);
	
	/**
	 * 统计单个科目借方贷方金额	
	 * @param map
	 * @return
	 */
	public CpaVoucherSummary statisticsAmountBySummaryId(Map<String, Object> map);
	
	/**
	 * 统计本期损益类除开收入类科目的subjectId
	 * 2018年1月2日 下午2:24:08
	 * @param map
	 * @return
	 */
	public List<CpaVoucherSummary> findCurrentPeriodProfitCostSummaryIds(Map<String, Object> map);
	
	/**
	 * 查询科目信息及发生额
	 * 2017年12月27日 上午9:55:09
	 * @param id
	 * @param bookId
	 * @param type  1 查询本期发生额  2、查询账期（包含当前账期及之前账期）
	 * @param nextPeriod
	 * @return
	 */
	public CpaVoucherSummary findInfoAndAmountById(@Param("id") String id,
			@Param("bookId") String bookId ,@Param("type") Integer type ,
			@Param("period") String period);
	
}
