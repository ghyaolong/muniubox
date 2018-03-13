package com.taoding.service.settleaccount;

import com.taoding.domain.settleaccount.CpaFinalLiquidationProportion;

/**
 * 结转销售成本比例设置
 * 
 * @author admin
 *
 */
public interface CpaFinalLiquidationProportionService {

	/**
	 * 新增比例
	 */
	public Object insertProportion(
			CpaFinalLiquidationProportion finalLiquidationProportion);

	/**
	 * 查询比例
	 */
	public CpaFinalLiquidationProportion findProportionByBookId(String bookId);

	/**
	 * 修改比例 2017年12月29日 下午2:27:41
	 * 
	 * @param finalLiquidationProportion
	 * @return
	 */
	public Object updateProportion(
			CpaFinalLiquidationProportion finalLiquidationProportion);
}
