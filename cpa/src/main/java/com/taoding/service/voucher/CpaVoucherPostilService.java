package com.taoding.service.voucher;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.voucher.CpaVoucherPostil;
import com.taoding.mapper.voucher.CpaVoucherPostilDao;

/**
 * 凭证批注
 * 
 * @author czy 2017年11月28日 上午11:52:32
 */
public interface CpaVoucherPostilService extends
		CrudService<CpaVoucherPostilDao, CpaVoucherPostil> {

	/**
	 * 根据凭证ID查询批注
	 * 2017年12月8日 下午1:44:56
	 * @param voucherId
	 * @return
	 */
	List<CpaVoucherPostil> findListByVoucherId(String voucherId);
	
	/**
	 * 新增批注
	 * 2017年12月8日 下午2:13:11
	 * @param voucherPostil
	 * @return
	 */
	public Object insert(CpaVoucherPostil voucherPostil);
	
	/**
	 * 根据id删除批注
	 * 2017年12月8日 下午2:01:12
	 * @param id
	 * @return
	 */
	public Object deleteById(String id);
	
}
