package com.taoding.service.settleaccount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoding.domain.settleaccount.CpaFinalLiquidationTemplete;
import com.taoding.mapper.settleaccount.CpaFinalLiquidationTempleteDao;

/**
 * 期末结转基础模板
 * @author czy
 * 2017年12月22日 上午10:17:34
 */
@Service
public class CpaFinalLiquidationTempleteServiceImpl implements CpaFinalLiquidationTempleteService {

	@Autowired
	private CpaFinalLiquidationTempleteDao finalLiquidationTempleteDao;
	
	/**
	 * 根据纳税人性质 获取 基础模板数据
	 * 2017年12月22日 上午10:18:32
	 * @param taxpayerProperty
	 * @return
	 */
	@Override
	public List<CpaFinalLiquidationTemplete> findListByTaxpayerProperty(
			String taxpayerProperty) {
		return finalLiquidationTempleteDao.findListByTaxpayerProperty(taxpayerProperty);
	}

}
