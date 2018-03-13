package com.taoding.mapper.voucher;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.domain.vouchersummary.SummaryEntity;


/**
 * 凭证科目
 * 
 * @author czy 2017年11月28日 上午11:22:04
 */
@Repository
@Mapper
public interface CpaVoucherSubjectDao extends CrudDao<CpaVoucherSubject>{

    /**
     * 为一个客户初始化一张凭证科目表
     * @param bookId
     * @return
     */
    int init(@Param("bookId") String bookId);
    
    /**
     * 新增凭证科目
     * 2017年11月29日 上午11:06:44
     * @param bookId
     * @param voucherSubject
     * @return
     */
    public int insert(@Param("bookId") String bookId,
    		@Param("voucherSubject") CpaVoucherSubject voucherSubject);
    
	/**
	 * 批量新增凭证科目
	 * 2017年11月29日 上午10:16:34
	 * @param bookId
	 * @param lists
	 * @return
	 */
	public int batchInsert(@Param("bookId") String bookId,
			@Param("lists") List<CpaVoucherSubject> lists);
	
	
	/**
	 * 根据凭证ID查询凭证科目
	 * 2017年11月29日 上午10:20:41
	 * @param voucherId
	 * @param bookId
	 * @return
	 */
	public List<CpaVoucherSubject> findByVoucherId(@Param("bookId") String bookId,
			@Param("voucherId") String voucherId);
	
	/**
	 * 通过科目id查询所有记账凭证所有科目信息
	 * 2017年12月1日 下午2:42:24
	 * @param bookId
	 * @param subjectId
	 * @return
	 */
	public List<CpaVoucherSubject> findBySubjectId(@Param("bookId") String bookId,
			@Param("subjectId") String subjectId);
	
	
	/**
	 * 根据凭证ID 删除科目信息 
	 * 2017年12月4日 上午10:18:03
	 * @param bookId
	 * @param voucherId
	 * @return
	 */
	public int deleteByVoucherId(@Param("bookId") String bookId,
			@Param("voucherId") String voucherId);
	
	/**
	 * 根据凭证ID批量删除凭证科目
	 * 2017年12月6日 上午10:08:24
	 * @param maps
	 * @return
	 */
	public int batchDeleteByVoucherId(@Param("bookId") String bookId,
			@Param("deleteIds") String [] deleteIds);
	
	/**
	 * 查询需要合并的科目，并将同科目合并
	 * 2017年12月7日 下午4:40:28
	 * @param bookId
	 * @param mergeIds  凭证科目集合
	 * @return
	 */
	public List<CpaVoucherSubject> findMergeVoucherSubject(@Param("bookId") String bookId,
			@Param("mergeIds") String [] mergeIds);
	
	/**
	 * 获取需要删除的科目的金额 此方法凭证汇总使用
	 * 2017年12月14日 上午11:28:16
	 * @param bookId
	 * @param voucherIds
	 * @return
	 */
	public List<SummaryEntity> findDeleteSubjectAmount(@Param("bookId") String bookId,
			@Param("voucherIds") String [] voucherIds);
	
	/**
	 * 根据 customerId 查询科目编号
	 * @param customerId
	 * @return
	 */
	public CpaCustomerSubject getSubjectNoById(@Param("customerId") String customerId);
	
	/**
	 * 所有子节点金额合计 按科目方向计算金额
	 * @param subjectNo 科目编号
	 * @param bookId 账薄 ID
	 * @param proportion 比例
	 * @return
	 */
	public List<CpaVoucherSubject> findByParentSubjectNo(
			@Param("bookId") String bookId, 
			@Param("currentPeriod") String currentPeriod, 
			@Param("subjectNo") String subjectNo );
	
	/**
	 * 单个节点 计算金额合计按科目方向计算金额
	 * @param customerId
	 * @return
	 */
	public CpaVoucherSubject getCpaVoucherSubjectBySubjectId(
			@Param("bookId") String bookId, 
			@Param("currentPeriod")String currentPeriod, 
			@Param("subjectId") String subjectId);
	
	/**
	 * 计算 损益 （直接使用的科目编号）。查询单个节点 
	 * @param bookId
	 * @param customerId
	 * @return
	 */
	public CpaVoucherSubject getCpaVoucherSubjectProfitAndLoss(
			@Param("bookId") String bookId, 
			@Param("currentPeriod")String currentPeriod, 
			@Param("subjectId") String subjectId);
	
	/**
	 * 计算 损益 （包含直接使用的科目编号的）。匹配所有子节点 /根据 bookId 查询所有节点
	 * @param subjectNo
	 * @param bookId
	 * @return
	 */
	public List<CpaVoucherSubject> findByParentSubjectNoProfitAndLoss(
			@Param("bookId") String bookId,
			@Param("currentPeriod")String currentPeriod, 
			@Param("subjectNo") String subjectNo);
	
	/**
	 * 计算 损益 （直接使用的科目编号）。查询单个节点 。 金额
	 * @param bookId
	 * @param customerId
	 * @return
	 */
//	public CpaVoucherSubject getCpaVoucherSubjectProfitAndLossMoney(
//			@Param("bookId") String bookId, 
//			@Param("currentPeriod") String currentPeriod, 
//			@Param("subjectId") String subjectId);
	
	/**
	 * 计算 损益 （包含直接使用的科目编号的）。匹配所有子节点 /根据 bookId 查询所有节点 。 统计总金额
	 * @param subjectNo
	 * @param bookId
	 * @return
	 */
//	public List<CpaVoucherSubject> findByParentSubjectNoProfitAndLossTotalMoney(
//			@Param("bookId") String bookId,
//			@Param("currentPeriod") String currentPeriod, 
//			@Param("subjectNo") String subjectNo); 
	
	
	
}
