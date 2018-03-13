package com.taoding.mapper.subject;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;

/**
 * 科目期初
 * 
 * @author czy 2017年11月20日 上午11:15:33
 */
@Repository
@Mapper
public interface CpaCustomerSubjectDao extends CrudDao<CpaCustomerSubject> {

	/**
	 * 根据id查询期初
	 * 2017年11月30日 上午9:26:36
	 * @param id
	 * @return
	 */
	public CpaCustomerSubject getById(@Param("bookId") String bookId,@Param("id") String id);
	
	/**
	 * 根据账簿ID查询该账簿下所有科目信息 
	 * 2017年11月20日 下午1:55:43
	 * @param bookId
	 * @param hashChild
	 * @return
	 */
	public List<CpaCustomerSubject> findAllList(@Param("bookId") String bookId,
			@Param("hashChild") boolean hashChild);

	/**
	 * 批量新增科目 
	 * 2017年11月20日 上午11:45:37
	 * @param lists
	 * @return
	 */
	public int batchInsert(List<CpaCustomerSubject> lists);

	/**
	 * 根据父No查询最大子科目编号 
	 * 2017年11月20日 下午3:08:11
	 * @param bookId
	 * @param parentNo
	 * @return
	 */
	public String findMaxNoByParentNo(@Param("bookId") String bookId,
			@Param("parentNo") String parentNo);

	/**
	 * 查询当前父节点下科目名称唯一性
	 * 2017年11月20日 下午4:15:31
	 * @param bookId
	 * @param parentNo
	 * @param subjectName
	 * @return
	 */
	public CpaCustomerSubject findByParentNoAndName(
			@Param("bookId") String bookId,@Param("parentNo") String parentNo,
			@Param("subjectName") String subjectName);
	
	
	/**
	 * 根据父No查询 其下所有子科目信息 
	 * 2017年11月22日 上午9:18:11
	 * @param bookId
	 * @param parentNo
	 * @return
	 */
	public List<CpaCustomerSubject> findListByParentNo(@Param("bookId") String bookId,
			@Param("parentNo") String parentNo);
	
	/**
	 * 统计期初金额
	 * 2017年11月22日 上午10:41:43
	 * @param bookId
	 * @return
	 */
	public Object totalBeginningMoney(String bookId);

	/**
	 * 统计累计发生额
	 * 2017年11月22日 上午10:41:50
	 * @param bookId
	 * @return
	 */
	public Object totalCurrentYearMoney(String bookId);
	
	/**
	 * 根据企业ID修改结账状态
	 * 2017年11月22日 下午5:18:08
	 * @param finish
	 * @param bookId
	 * @return
	 */
	public int updateFinishByBookId(@Param("finish") boolean finish,
			@Param("bookId") String bookId);
	
	/**
	 * 修改科目期初金额
	 * 2017年11月24日 上午9:38:36
	 * @param customerSubject
	 */
	public int updateSubjectMoney(CpaCustomerSubject customerSubject);
	
	/**
	 * 根据科目编号查询科目信息
	 * 2017年11月24日 上午10:58:45
	 * @param bookId
	 * @param subjectNo
	 * @return
	 */
	public CpaCustomerSubject findBySubjectNo(@Param("bookId") String bookId,
			@Param("subjectNo") String subjectNo);
	
	/**
	 * 根据父节点统计金额
	 * 2017年11月24日 上午11:15:29
	 * @param bookId
	 * @param subjectNo
	 * @return
	 */
	public CpaCustomerSubject totalByParentNo(@Param("bookId") String bookId,
			@Param("subjectNo") String subjectNo);
	
	/**
	 * 根据id 查询科目信息及科目余额
	 * 2017年11月30日 上午9:29:24
	 * @param id
	 * @return
	 */
	public CpaCustomerSubject findInfoAndAmountById(@Param("bookId") String bookId,
			@Param("id") String id,@Param("nextPeriod") String nextPeriod);
	
	/**
	 * 根据ID查询科目信息，此方法只供凭证汇总模块使用
	 * 2017年12月13日 下午5:14:21
	 * @param bookId
	 * @param id
	 * @return
	 */
	public CpaVoucherSummary getVoucherSummarySubjectById(@Param("bookId") String bookId,
			@Param("id") String id);
	
	
	/**
	 * 通过科目编号查询科目信息，此方法只供凭证汇总模块使用
	 * 2017年12月14日 上午9:42:44
	 * @param bookId
	 * @param subjectNo
	 * @return
	 */
	public CpaVoucherSummary getVoucherSummaryBySubjectNo(@Param("bookId") String bookId,
			@Param("subjectNo") String subjectNo);
}
