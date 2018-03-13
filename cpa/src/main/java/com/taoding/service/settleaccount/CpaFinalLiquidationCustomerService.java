package com.taoding.service.settleaccount;

import java.util.List;

import com.taoding.domain.settleaccount.CpaFinalLiquidationCustomer;


/**
 * 企业期末结转基础配置
 * @author czy
 * 2017年12月21日 下午3:39:41
 */
public interface CpaFinalLiquidationCustomerService {

	
	/**
	 * 初始化企业期末结转基础配置
	 * 2017年12月22日 上午10:19:24
	 * @param bookId
	 * @param customerId
	 * @return
	 */
	public int init(String bookId,String customerId);
	
	/**
	 * 通过账簿ID 获取 企业 期末结转配置
	 * 2017年12月21日 下午3:40:51
	 * @param bookId
	 * @return
	 */
	public List<CpaFinalLiquidationCustomer> findListByBookId(String bookId);
	
	
}
