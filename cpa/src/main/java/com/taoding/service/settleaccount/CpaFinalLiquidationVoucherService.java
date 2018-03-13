package com.taoding.service.settleaccount;

import java.util.List;

import com.taoding.domain.settleaccount.CpaFinalLiquidationVoucher;


/**
* @ClassName: CpaFinalLiquidationVoucherService 
* @Description:  
* @author lixc 
* @date 2017年12月23日 下午2:05:59 
 */
public interface CpaFinalLiquidationVoucherService {
	

	/**
	* @Description:查询 List<CpaFinalLiquidationVoucher> 列表
	* @param bookId
	* @param voucherPeriod
	* @return List<CpaFinalLiquidationVoucher> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月23日
	 */
	public List<CpaFinalLiquidationVoucher> findAllList(String bookId,String voucherPeriod);
	
}
