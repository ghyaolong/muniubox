package com.taoding.service.settleaccount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoding.domain.settleaccount.CpaFinalLiquidationBasic;
import com.taoding.mapper.settleaccount.CpaFinalLiquidationBasicDao;


/**
 * 
 * @author czy
 * 2017年12月19日 下午3:56:10
 */
@Service
public class CpaFinalLiquidationBasicServiceImpl implements CpaFinalLiquidationBasicService{

	@Autowired
	private CpaFinalLiquidationBasicDao finalLiquidationBasicDao ;
	
	/**
	 * 根据纳税人规模查询 基础配置
	 * 2017年12月19日 下午3:55:45
	 * @param taxpayerProperty
	 *   1 小规模    2一般
	 * @param subKey
	 * @param type 类型自定义
	 * @return
	 */
	@Override
	public List<CpaFinalLiquidationBasic> findAllListByTaxpayerPropertyAndKeyAndType(String taxpayerProperty,String subKey,Integer type) {
		
		return finalLiquidationBasicDao.findAllListByTaxpayerPropertyAndKeyAndType(taxpayerProperty,subKey,type);
	}
	
	/**
	 * 根据纳税人规模查询 基础配置
	 * 2017年12月19日 下午3:55:45
	 * @param taxpayerProperty
	 *   1 小规模    2一般
	 * @param subKey
	 * @return
	 */
	public List<CpaFinalLiquidationBasic> findAllListByTaxpayerProperty(String taxpayerProperty,String subKey){
		return finalLiquidationBasicDao.findAllListByTaxpayerPropertyAndKeyAndType(taxpayerProperty,subKey,null);	
	}
}
