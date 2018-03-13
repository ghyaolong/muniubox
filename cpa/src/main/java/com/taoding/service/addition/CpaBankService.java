package com.taoding.service.addition;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.addition.CpaBank;
import com.taoding.domain.addition.CpaCustomerSubjectBank;
import com.taoding.mapper.addition.CpaBankDao;

/**
 * 银行信息Service
 * 
 * @author mhb
 * @version 2017-11-18
 */
public interface CpaBankService extends CrudService<CpaBankDao, CpaBank> {
	
	/**
	 * 查询银行信息
	 * @return
	 */
	List<CpaBank> getBankList(String bankName);
	/**
	 * 增加银行信息
	 * 
	 * @param cpaBank
	 *            银行Entity
	 * 
	 * @return
	 */
	Object saveBank(CpaBank cpaBank);

	/**
	 * 修改银行信息
	 * 
	 * @param cpaBank
	 *            银行Entity
	 * 
	 * @return
	 */

	Object editBank(CpaBank cpaBank);

	/**
	 * 删除银行信息
	 * 
	 * @param id
	 * 
	 * @return
	 */

	Object deleteBank(String id);
}
