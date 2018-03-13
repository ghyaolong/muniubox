package com.taoding.service.settleaccount;

import java.util.List;

import com.taoding.domain.settleaccount.CpaFinalLiquidationBasic;

/**
 * 
 * @author czy
 * 2017年12月19日 下午3:55:39
 */
public interface CpaFinalLiquidationBasicService {

	
	/**
	 * 根据纳税人规模查询 基础配置
	 * 2017年12月19日 下午3:55:45
	 * @param taxpayerProperty
	 *   1 小规模    2一般
	 * @param subKey
	 * @param type 自定义
	 * @return
	 */
	public List<CpaFinalLiquidationBasic> findAllListByTaxpayerPropertyAndKeyAndType(String taxpayerProperty,String subKey,Integer type);
	
	
	/**
	 * 根据纳税人规模查询 基础配置
	 * 2017年12月19日 下午3:55:45
	 * @param taxpayerProperty
	 *   1 小规模    2一般
	 * @param subKey
	 * @return
	 */
	public List<CpaFinalLiquidationBasic> findAllListByTaxpayerProperty(String taxpayerProperty,String subKey);
	
}
