	package com.taoding.service.settleaccount;

import java.util.List;

import com.taoding.domain.settleaccount.AssetClassBalanceVO;

/**
 * 账户结算
 * @author admin
 *
 */
public interface CpaSettleAccountService {
	
	/**
	 * 处理资产类科目
	 * @param bookId
	 * @return
	 */
	public List<AssetClassBalanceVO> assetClassBalance(String bookId);
	
	/**
	 * 费用控制及纳税调整
	 * @param bookId
	 * @return
	 */
	public List<AssetClassBalanceVO> costControlAndTaxAdjustment(String bookId);
	
	/**
	 * 结账 按钮保存的信息
	 * @param bootId
	 * @param customerId
	 * @return
	 */
	public Object saveSettleAccount(String bookId, String customerId);
	
	/**
	 * 反结账 按钮保存的信息
	 * @param bookId
	 * @param customerId
	 */
	public Object antiSettleAccount(String bookId, String currentPeriod);
	
	/**
	 * 判断 结账状态
	 * @param bookId
	 * @param customerId
	 * @return
	 */
	public Object dealSettleAccountState(String bookId, String currentPeriod);
	
}
