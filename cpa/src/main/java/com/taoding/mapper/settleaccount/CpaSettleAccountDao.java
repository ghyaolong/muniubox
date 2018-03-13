package com.taoding.mapper.settleaccount;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.settleaccount.CpaSettleAccount;

/**
 * 结算 反算
 * @author admin
 *
 */
@Repository
@Mapper
public interface CpaSettleAccountDao {

	/**
	 * 结账 按钮保存的信息
	 * @param account
	 * @return
	 */
	public int insertAccount(CpaSettleAccount account);
	
	/**
	 * 根据账薄 id，客户 id，账期 查询唯一的一条数据
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	public CpaSettleAccount findByCustomerIdAndCurrentPeriod(
			@Param("bookId")String bookId, 
			@Param("currentPeriod")String currentPeriod);
	
	/**
	 * 根据账薄 id，客户 id，账期 更新一条数据
	 * 将数据更新为结账状态
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	public int updateCpaSettleAccountByOne(
			@Param("bookId")String bookId, 
			@Param("currentPeriod")String currentPeriod);
	
	
	/**
	 * 反结账  根据账薄 id，客户 id，账期 更新 传入账期以前的数据
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	public int updateCpaSettleAccountByList(
			@Param("bookId")String bookId, 
			@Param("currentPeriod")String currentPeriod);
	
	
}
