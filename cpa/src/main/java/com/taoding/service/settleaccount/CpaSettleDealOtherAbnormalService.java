package com.taoding.service.settleaccount;

import java.util.List;

import com.taoding.domain.settleaccount.AssetClassBalanceVO;

/**
 * 期末结账加载其他异常数据
 * @author admin
 *
 */
public interface CpaSettleDealOtherAbnormalService {
	
	/**
	 * 加载其他异常数据
	 * 2017年12月26日 下午2:56:14
	 * @param bookId
	 * @return
	 */
	public List<AssetClassBalanceVO> loadOtherAbnormalData(String bookId);
	
}
