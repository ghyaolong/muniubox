package com.taoding.mapper.settleaccount;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.domain.settleaccount.CpaFinalLiquidationTemplete;

/**
 * 期末结转基础模板
 * @author czy
 * 2017年12月22日 上午10:12:47
 */
@Repository
@Mapper
public interface CpaFinalLiquidationTempleteDao {

	
	/**
	 * 根据纳税人性质 获取 基础模板数据
	 * 2017年12月22日 上午10:13:32
	 * @param taxpayerProperty
	 * @return
	 */
	List<CpaFinalLiquidationTemplete> findListByTaxpayerProperty(String taxpayerProperty);
	
}
