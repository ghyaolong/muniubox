package com.taoding.mapper.settleaccount;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.settleaccount.CpaFinalLiquidationBasic;

/**
 * 企业期末结转基础数据
 * @author czy
 * 2017年12月19日 上午10:18:46
 */
@Repository
@Mapper
public interface CpaFinalLiquidationBasicDao {
	
	/**
	 * 根据纳税人规模查询 基础配置
	 * 2017年12月19日 下午3:49:45
	 * @param taxpayerProperty
	 *   1 小规模    2一般
	 * @param subKey
	 * @return
	 */
	public List<CpaFinalLiquidationBasic> findAllListByTaxpayerPropertyAndKeyAndType(
			@Param("taxpayerProperty") String taxpayerProperty,@Param("subKey") String subKey,@Param("type") Integer type);
	

}
