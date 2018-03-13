package com.taoding.service.subject;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.subject.CpaAssistingData;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.mapper.subject.CpaCustomerSubjectDao;


/**
 * 科目期初
 * @author czy
 * 2017年11月20日 上午11:38:41
 */
public interface CpaCustomerSubjectService extends CrudService<CpaCustomerSubjectDao, CpaCustomerSubject> {

	/**
	 * 初始化企业科目期初数据
	 * 2017年12月6日 下午6:12:53
	 * @param bookId
	 * @param customerId
	 */
	public void init(String bookId,String customerId);
	
	/**
	 * 根据账簿ID查询该账簿下所有科目信息
	 * 2017年11月20日 下午1:55:43
	 * @param bookId
	 * @param hashChild
	 * @return
	 */
	public List<CpaCustomerSubject> findAllList(String bookId,boolean hashChild);
	
	/**
	 * 
	 * 2017年11月20日 上午11:44:39
	 * @param customerSubject
	 * @return
	 */
	public Object insertCustomerSubject(CpaCustomerSubject customerSubject);
	
	
	/**
	 * 批量新增科目
	 * 2017年11月20日 上午11:45:37
	 * @param bookId
	 * @param customerId
	 * @return
	 */
	public int batchInsert(String bookId,String customerId);
	
	/**
	 * 根据父No生成下一个科目编号
	 * 2017年11月20日 下午3:08:11
	 * @param bookId
	 * @param parentNo
	 * @return
	 */
	public String getNextNo(String bookId,String parentNo);
	
	/**
	 * 查询当前父节点下科目名称唯一性
	 * 2017年11月20日 下午4:15:31
	 * @param bookId
	 * @param parentNo
	 * @param subjectName
	 * @return
	 */
	public CpaCustomerSubject findByParentNoAndName(String bookId,String parentNo,String subjectName);
	
	/**
	 * 修改科目
	 * 2017年11月20日 下午5:08:30
	 * @param customerSubject
	 * @return
	 */
	public Object updateCustomerSubject(CpaCustomerSubject customerSubject);
	
	/**
	 * 根据父No查询 其下所有子科目信息 
	 * 2017年11月22日 上午9:18:11
	 * @param bookId
	 * @param parentNo
	 * @return
	 */
	public List<CpaCustomerSubject> findListByParentNo(String bookId,String parentNo);
	
	/**
	 * 根据id删除科目
	 * 2017年11月22日 上午9:24:48
	 * @param bookId
	 * @param id
	 */
	public Object deleteById(String id);
	
	/**
	 * 企业金额试算平衡数据
	 * 2017年11月22日 上午10:47:11
	 * @param bookId
	 * @return
	 */
	public Object trialBalanceData(String bookId);
	
	/**
	 * 根据id查询科目
	 * 2017年11月22日 下午4:26:13
	 * @param id
	 * @param bookId
	 * @return
	 */
	public CpaCustomerSubject getById(String bookId,String id);
	
	/**
	 * 根据企业ID修改结账状态
	 * 2017年11月22日 下午5:18:08
	 * @param finish
	 * @param bookId
	 * @return
	 */
	public Object updateFinishByBookId(boolean finish,String bookId);
	
	/**
	 * 新增辅助核算项科目
	 * 2017年11月23日 下午4:12:58
	 * @param cpaAssistingData
	 * @return
	 */
	public Object insertAssistingSubject(CpaAssistingData cpaAssistingData);
	
	
	/**
	 * 修改科目期初金额
	 * 2017年11月24日 上午9:42:28
	 * @param customerSubject
	 * @return
	 */
	public Object updateSubjectMoney(CpaCustomerSubject customerSubject);
	
	/**
	 * 根据id 查询科目信息及科目余额
	 * 2017年11月30日 上午10:26:24
	 * @param id
	 * @return
	 */
//	public CpaCustomerSubject findInfoAndAmountById(String bookId ,String id);
	
	/**
	 * 通过科目编号查询科目信息 
	 * 2017年12月1日 下午3:55:57
	 * @param bookId
	 * @param subjectNo
	 * @return
	 */
	public CpaCustomerSubject findBySubjectNo(String bookId ,String subjectNo);
	
	/**
	 * 根据ID查询科目信息，此方法只供凭证汇总模块使用
	 * 2017年12月13日 下午5:14:21
	 * @param bookId
	 * @param id
	 * @return
	 */
	public CpaVoucherSummary getVoucherSummarySubjectById(String bookId,String id);
	
	/**
	 * 通过科目编号查询科目信息，此方法只供凭证汇总模块使用
	 * 2017年12月14日 上午9:42:44
	 * @param bookId
	 * @param subjectNo
	 * @return
	 */
	public CpaVoucherSummary getVoucherSummaryBySubjectNo(String bookId,String subjectNo);
	
}
