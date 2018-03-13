package com.taoding.service.voucher;

import java.util.List;

import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.domain.vouchersummary.SummaryEntity;

/**
 * 凭证科目
 * 
 * @author czy 2017年11月28日 上午11:52:43
 */
public interface CpaVoucherSubjectService {

    /**
     * 为一个客户初始化一张凭证科目表
     * @param bookId
     * @return
     */
    int init(String bookId);
    
	/**
	 * 新增凭证科目
	 * 2017年11月29日 上午11:13:34
	 * @param bookId
	 * @param voucherSubject
	 * @return
	 */
	public int insert(String bookId,CpaVoucherSubject voucherSubject);
	
	/**
	 * 批量新增凭证科目
	 * 2017年11月29日 上午10:20:34
	 * @param bookId
	 * @param lists
	 * @return
	 */
	public int batchInsert(String bookId,List<CpaVoucherSubject> lists);
	
	
	/**
	 * 根据凭证ID查询凭证科目
	 * 2017年11月29日 上午10:21:41
	 * @param bookId
	 * @param voucherId
	 * @return
	 */
	public List<CpaVoucherSubject> findByVoucherId(String bookId,String voucherId);
	
	/**
	 * 通过科目id查询所有记账凭证所有科目信息
	 * 2017年12月1日 下午2:42:24
	 * @param bookId
	 * @param subjectId
	 * @return
	 */
	public List<CpaVoucherSubject> findBySubjectId(String bookId,String subjectId);
	
	/**
	 * 根据凭证ID 删除科目信息 
	 * 2017年12月4日 上午10:18:03
	 * @param bookId
	 * @param voucherId
	 * @return
	 */
	public int deleteByVoucherId(String bookId, String voucherId);
	
	/**
	 * 根据凭证ID批量删除凭证科目
	 * 2017年12月6日 上午10:08:24
	 * @param bookId
	 * @param deleteIds
	 * @return
	 */
	public int batchDeleteByVoucherId(String bookId,String [] deleteIds);
	
	/**
	 * 查询需要合并的科目，并将同科目合并
	 * 2017年12月7日 下午4:40:28
	 * @param bookId
	 * @param mergeIds
	 * @return
	 */
	public List<CpaVoucherSubject> findMergeVoucherSubject(String bookId,String [] mergeIds);
	
	/**
	 *  获取需要删除的科目的金额  此方法凭证汇总使用
	 * 2017年12月14日 上午11:28:16
	 * @param bookId
	 * @param voucherIds
	 * @return
	 */
	public List<SummaryEntity> findDeleteSubjectAmount(String bookId,String [] voucherIds);
	
	/**
	 * 处理结转销售成本 生成凭证科目信息
	 * @param bookId 
	 * @param subjectIds 
	 * @return
	 */
	public List<CpaVoucherSubject> dealSaleCostVoucherSubjectInfo(String bookId,List<String> subjectIds);
	
	/**
	 * 处理结转本期损益（收入） 生成凭证科目信息
	 * @param bookId 
	 * @param subjectIds 收入类四项ID
	 * @return
	 */
	public List<CpaVoucherSubject> dealProfitRevenueVoucherSubjectInfo(String bookId,List<String> subjectIds);
	
	/**
	 * 处理结转本期损益（成本费用） 生成凭证科目信息
	 * 2018年1月2日 下午2:06:44
	 * @param bookId
	 * @param subjectIds 收入类四项ID
	 * @return
	 */
	public List<CpaVoucherSubject> dealProfitCostVoucherSubjectInfo(String bookId,List<String> subjectIds);
	
}
