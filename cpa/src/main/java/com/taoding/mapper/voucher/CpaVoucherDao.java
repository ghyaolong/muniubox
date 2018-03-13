package com.taoding.mapper.voucher;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.voucher.CpaVoucher;

/**
 * 凭证
 * 
 * @author czy 2017年11月28日 上午11:45:43
 */
@Repository
@Mapper
public interface CpaVoucherDao extends CrudDao<CpaVoucher>{

	/**
	 * 为一个客户初始化一张凭证表
	 * 
	 * @param bookId
	 * @return
	 */
	int init(@Param("bookId") String bookId);

	/**
	 * 根据id查询凭证 2017年11月29日 上午10:54:56
	 * 
	 * @param bookId
	 * @param id
	 * @return
	 */
	public CpaVoucher getById(@Param("bookId") String bookId,
			@Param("id") String id);

	/**
	 * 新增凭证 2017年11月29日 上午11:04:39
	 * 
	 * @param bookId
	 * @param voucher
	 * @return
	 */
	public int insert(@Param("bookId") String bookId,
			@Param("voucher") CpaVoucher voucher);

	/**
	 * 通过账期查询最大编号
	 * 
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	public String getMaxNo(@Param("bookId") String bookId,
			@Param("voucherPeriod") String voucherPeriod);

	/**
	 * 通过账期+凭证编号查询凭证信息
	 * 
	 * @param bookId
	 * @param voucherNo
	 * @param voucherPeriod
	 * @return
	 */
	public CpaVoucher findByNoAndPeriod(@Param("bookId") String bookId,
			@Param("voucherNo") String voucherNo,
			@Param("voucherPeriod") String voucherPeriod);

	/**
	 * 修改凭证金额 2017年12月4日 上午10:32:17
	 * 
	 * @param bookId
	 * @param voucher
	 * @return
	 */
	public int updateVoucherAmount(@Param("bookId") String bookId,
			@Param("voucher") CpaVoucher voucher);

	/**
	 * 查询 当前账期 大于当前编号的数据 2017年12月4日 下午1:50:52
	 * 
	 * @param bookId
	 * @param voucherNo
	 * @param voucherPeriod
	 * @return
	 */
	public List<CpaVoucher> findGreaterThanVoucherNo(
			@Param("bookId") String bookId,
			@Param("voucherNo") String voucherNo,
			@Param("voucherPeriod") String voucherPeriod);

	/**
	 * 根据id删除凭证 2017年12月4日 下午2:02:43
	 * 
	 * @param bookId
	 * @param id
	 * @return
	 */
	public int deleteById(@Param("bookId") String bookId,
			@Param("id") String id);

	/**
	 * 修改凭证编号
	 * 2017年12月4日 下午2:04:10
	 * @param bookId
	 * @param id
	 * @param voucherNo
	 * @return
	 */
	public int updateVoucherNo(@Param("bookId") String bookId,
			@Param("id") String id, @Param("voucherNo") String voucherNo);
	
	
	/**
	 * 查询当前账期的所有编号
	 * 2017年12月4日 下午2:31:14
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	public List<String> findAllNoByPeriod(@Param("bookId") String bookId,
			@Param("voucherPeriod") String voucherPeriod);
	
	/**
	 * 查询调整编号所需的数据
	 * 2017年12月4日 下午3:45:38
	 * @param bookId
	 * @param startNo
	 * @param endNo
	 * @return
	 */
	public List<CpaVoucher> findAdjustmentVoucherNoData(@Param("bookId") String bookId,
			@Param("startNo") String startNo,@Param("endNo") String endNo);
	
	/**
	 * 批量删除凭证
	 * 2017年12月6日 上午10:08:24
	 * @param maps
	 * @return
	 */
	public int batchDelete(@Param("bookId") String bookId,
			@Param("deleteIds") String [] deleteIds);
	
	/**
	 * 查询当前账期所有数据
	 * 2017年12月6日 上午10:55:14
	 * @param maps
	 * @return
	 */
	public List<CpaVoucher> findAllListByPeriod(Map<String,String> maps);
	
	/**
	 * 查询需要合并的凭证基础数据
	 * 2017年12月6日 上午10:08:24
	 * @param bookId
	 * @param mergeIds
	 * @return
	 */
	public List<CpaVoucher> findMergeVoucherByIds(@Param("bookId") String bookId,
			@Param("mergeIds") String [] mergeIds);
	
	/**
	 * 查询当前账期凭证数量
	 * 2017年12月26日 下午6:18:47
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	public int findAllSize(@Param("bookId") String bookId,
			@Param("voucherPeriod") String voucherPeriod);

}