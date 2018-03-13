package com.taoding.service.settleaccount;

import java.util.List;

import com.taoding.domain.settleaccount.CpaFinalLiquidationTemplete;


/**
 * 期末结转基础模板
 * @author czy
 * 2017年12月22日 上午10:16:08
 */
public interface CpaFinalLiquidationTempleteService {

	/**
	 * 根据纳税人性质 获取 基础模板数据
	 * 2017年12月22日 上午10:13:32
	 * @param taxpayerProperty
	 * @return
	 */
	List<CpaFinalLiquidationTemplete> findListByTaxpayerProperty(String taxpayerProperty);
	
}
