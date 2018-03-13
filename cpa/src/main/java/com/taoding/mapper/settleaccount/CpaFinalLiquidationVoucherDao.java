package com.taoding.mapper.settleaccount;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.settleaccount.CpaFinalLiquidationVoucher;

/**
 * 
* @ClassName: CpaFinalLiquidationVoucherDao 
* @Description: 期末结转
* @author lixc 
* @date 2017年12月23日 上午11:48:29 
*
 */
@Repository
@Mapper
public interface CpaFinalLiquidationVoucherDao  {

	/**
	* @Description: 查询CpaFinalLiquidationVoucher列表
	* @param bookId
	* @param voucherPeriod
	* @param subKey
	* @return List<CpaFinalLiquidationVoucher> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月23日
	 */
	public List<CpaFinalLiquidationVoucher> findAllList(@Param("bookId") String bookId,
			@Param("voucherPeriod") String voucherPeriod);
 
}
